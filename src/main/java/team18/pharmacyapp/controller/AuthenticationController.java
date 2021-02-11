package team18.pharmacyapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import team18.pharmacyapp.model.dtos.RegisterUserDTO;
import team18.pharmacyapp.model.dtos.security.ChangePassDTO;
import team18.pharmacyapp.model.dtos.security.LoginDTO;
import team18.pharmacyapp.model.dtos.security.UserTokenDTO;
import team18.pharmacyapp.model.enums.UserRole;
import team18.pharmacyapp.model.users.RegisteredUser;
import team18.pharmacyapp.security.TokenUtils;
import team18.pharmacyapp.service.CustomUserDetailsService;
import team18.pharmacyapp.service.interfaces.PatientService;
import team18.pharmacyapp.service.interfaces.RegisteredUserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@CrossOrigin(origins = {"http://localhost:8080","http://localhost:8081"})
@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {
    private TokenUtils tokenUtils;
    private AuthenticationManager authenticationManager;
    private CustomUserDetailsService userDetailsService;
    private RegisteredUserService userService;
    private PatientService patientService;

    @Autowired
    public AuthenticationController(TokenUtils tokenUtils, AuthenticationManager authenticationManager, CustomUserDetailsService userDetailsService,
                                    RegisteredUserService userService, PatientService patientService){
        this.tokenUtils = tokenUtils;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.patientService = patientService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserTokenDTO> createAuthenticationToken(@RequestBody LoginDTO authenticationRequest,
                                                                    HttpServletResponse response) {
        Authentication authentication;

        try{
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),
                            authenticationRequest.getPassword()));
        }catch(BadCredentialsException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        RegisteredUser user = (RegisteredUser) authentication.getPrincipal();
        if(user.getRole() == UserRole.patient){
            if(!patientService.isActivated(user.getId()))
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }else if(user.getFirstLogin()) {
            return new ResponseEntity<>(HttpStatus.LOCKED);
        }
        String jwt = tokenUtils.generateToken(user.getUsername());
        int expiresIn = tokenUtils.getExpiredIn();

        return ResponseEntity.ok(new UserTokenDTO(jwt,expiresIn,user.getId(),user.getRole(),user.getName(),user.getSurname(),user.getEmail()));
    }


    @PostMapping("/signup/patient")
    public ResponseEntity<RegisteredUser> addUser(@RequestBody RegisterUserDTO dto) {
        RegisteredUser existUser = this.userService.findByEmail(dto.getEmail());
        if (existUser != null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        UserRole role=UserRole.patient;
        String USER_ROLE="ROLE_PATIENT";
        RegisteredUser user = this.userService.save(dto,role,USER_ROLE);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping("/signup/derm")
    public ResponseEntity<RegisteredUser> addDerm(@RequestBody RegisterUserDTO dto) {
        RegisteredUser existUser = this.userService.findByEmail(dto.getEmail());
        if (existUser != null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        UserRole role = UserRole.dermatologist;
        String USER_ROLE = "ROLE_DERMATOLOGIST";
        RegisteredUser user = this.userService.save(dto,role,USER_ROLE);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping("/signup/sysadmin")
    public ResponseEntity<RegisteredUser> addSysAdmin(@RequestBody RegisterUserDTO dto) {
        RegisteredUser existUser = this.userService.findByEmail(dto.getEmail());
        if (existUser != null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        UserRole role = UserRole.sysAdmin;
        String USER_ROLE = "ROLE_SYSADMIN";
        RegisteredUser user = this.userService.save(dto,role,USER_ROLE);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping("/signup/supplier")
    public ResponseEntity<RegisteredUser> addSupplier(@RequestBody RegisterUserDTO dto) {
        RegisteredUser existUser = this.userService.findByEmail(dto.getEmail());
        if (existUser != null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        UserRole role = UserRole.supplier;
        String USER_ROLE = "ROLE_SUPPLIER";
        RegisteredUser user = this.userService.save(dto,role,USER_ROLE);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping("/signup/pharmadmin")
    public ResponseEntity<RegisteredUser> addPharmAdmin(@RequestBody RegisterUserDTO dto) {
        RegisteredUser existUser = this.userService.findByEmail(dto.getEmail());
        if (existUser != null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        UserRole role = UserRole.pharmacyAdmin;
        String USER_ROLE = "ROLE_PHADMIN";
        RegisteredUser user = this.userService.save(dto,role,USER_ROLE);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping("/firstLoginPass")
    public ResponseEntity<Boolean> changePass(@RequestBody ChangePassDTO dto) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(),
                        dto.getOldPass()));
        userService.changeFirstPass(dto);

        return new ResponseEntity<>(true,HttpStatus.OK);
    }

    @PostMapping(value = "/refresh")
    public ResponseEntity<UserTokenDTO> refreshAuthenticationToken(HttpServletRequest request) {
        String token = tokenUtils.getToken(request);
        String username = this.tokenUtils.getUsernameFromToken(token);
        RegisteredUser user = (RegisteredUser) this.userDetailsService.loadUserByUsername(username);

        String refreshedToken = tokenUtils.refreshToken(token);
        int expiresIn = tokenUtils.getExpiredIn();

        return ResponseEntity.ok(new UserTokenDTO(refreshedToken, expiresIn,user.getId(),user.getRole(),user.getName(),user.getSurname(),user.getEmail()));
    }

}
