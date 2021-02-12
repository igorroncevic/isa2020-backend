package team18.pharmacyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team18.pharmacyapp.model.Loyalty;
import team18.pharmacyapp.model.dtos.*;
import team18.pharmacyapp.model.dtos.security.LoginDTO;
import team18.pharmacyapp.model.enums.UserRole;
import team18.pharmacyapp.model.exceptions.ActionNotAllowedException;
import team18.pharmacyapp.model.exceptions.EntityNotFoundException;
import team18.pharmacyapp.model.medicine.Medicine;
import team18.pharmacyapp.model.users.Doctor;
import team18.pharmacyapp.model.users.Patient;
import team18.pharmacyapp.model.users.RegisteredUser;
import team18.pharmacyapp.repository.AddressRepository;
import team18.pharmacyapp.repository.LoyaltyRepository;
import team18.pharmacyapp.repository.MedicineRepository;
import team18.pharmacyapp.repository.users.PatientRepository;
import team18.pharmacyapp.repository.users.RegisteredUserRepository;
import team18.pharmacyapp.service.interfaces.EmailService;
import team18.pharmacyapp.service.interfaces.LoyaltyService;
import team18.pharmacyapp.service.interfaces.PatientService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;
    private final MedicineRepository medicineRepository;
    private final AddressRepository addressRepository;
    private final LoyaltyService loyaltyService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final RegisteredUserRepository userRepository;

    @Autowired
    public PatientServiceImpl(PatientRepository patientRepository, MedicineRepository medicineRepository, AddressRepository addressRepository, LoyaltyRepository loyaltyRepository, EmailService emailService, PasswordEncoder passwordEncoder, RegisteredUserRepository userRepository1) {
        this.patientRepository = patientRepository;
        this.medicineRepository = medicineRepository;
        this.addressRepository = addressRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository1;
        this.loyaltyService = new LoyaltyServiceImpl(loyaltyRepository, patientRepository);
        this.emailService = emailService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    @Override
    @Transactional
    public int addPenalty(UUID patientId) {
        return patientRepository.addPenalty(patientId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicineIdNameDTO> getAlergicTo(UUID patientId) {
        List<MedicineIdNameDTO> list = new ArrayList<>();
        for (Medicine m : patientRepository.getAlergicMedicines(patientId)) {
            list.add(new MedicineIdNameDTO(m.getId(), m.getName()));
        }
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public PatientDTO getPatientProfileInfo(UUID id) {
        Patient patient = patientRepository.getPatientForProfile(id);

        Loyalty l = patient.getLoyalty();
        PatientDTO patientDto = new PatientDTO(patient.getId(), patient.getName(), patient.getSurname(), patient.getEmail(), patient.getPhoneNumber(),
                patient.getRole(), patient.getAddress(), patient.getLoyaltyPoints(),
                new LoyaltyDTO(l.getCategory(), l.getMinPoints(), l.getMaxPoints(), l.getDiscount(), 0, 0),
                patient.getPenalties());

        return patientDto;
    }

    @Override
    @Transactional(rollbackFor = {ActionNotAllowedException.class, EntityNotFoundException.class, RuntimeException.class})
    public boolean updatePatientProfileInfo(UpdateProfileDataDTO patient) throws ActionNotAllowedException, EntityNotFoundException, RuntimeException {
        Patient pat = patientRepository.findById(patient.getId()).orElse(null);
        if (pat == null) throw new EntityNotFoundException("Not found");

        if (!patient.getNewPassword().equals(null) && !patient.getConfirmPassword().equals(null)) {
            if (!patient.getConfirmPassword().equals(patient.getNewPassword()))
                throw new ActionNotAllowedException("Passwords do not match");
        }
        if (!patient.getName().equals("")) pat.setName(patient.getName());
        if (!patient.getSurname().equals("")) pat.setSurname(patient.getSurname());
        if (!patient.getPhoneNumber().equals("")) pat.setPhoneNumber(patient.getPhoneNumber());
        if (!patient.getNewPassword().equals("")) pat.setPassword(passwordEncoder.encode(patient.getNewPassword()));

        pat = patientRepository.save(pat);
        int updated = userRepository.update(pat.getId(), pat.getName(), pat.getSurname(), pat.getPassword(), pat.getPhoneNumber());
        if(updated != 1) throw new RuntimeException("Couldnt change password");

        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public Patient getById(UUID id) {
        return patientRepository.findById(id).orElse(new Patient());
    }

    @Override
    @Transactional(readOnly = true)
    public int getPatientPenalties(UUID id) {
        return patientRepository.getPatientPenalties(id);
    }

    public Patient findRegisteredPatient(LoginDTO patient) {
        return patientRepository.findByEmailAndPassword(patient.getEmail(), patient.getPassword());
    }

    public boolean activateAcc(UUID id) {
        Patient p = patientRepository.findById(id).orElse(null);
        if (p != null) {
            p.setActivated(true);
            patientRepository.save(p);
            return true;
        }
        return false;
    }

    @Override
    public Patient save(Patient patientForUpdate) {
        return patientRepository.save(patientForUpdate);
    }

    @Override
    public Patient register(RegisteredUser user) {
        Patient pat = new Patient();
        pat.setRole(UserRole.patient);
        pat.setName(user.getName());
        pat.setSurname(user.getSurname());
        pat.setPhoneNumber(user.getPhoneNumber());
        pat.setEmail(user.getEmail());
        pat.setPassword(user.getPassword());
        pat.setAddress(user.getAddress());
        pat.setLoyalty(loyaltyService.findOneByCategory("Regular"));
        Patient newPatient = patientRepository.save(pat);
        patientRepository.setId(newPatient.getId(), user.getId());
        UUID id = user.getId();
        String userMail = pat.getEmail();
        String subject = "[ISA Pharmacy]  Account activation";
        String body = "You have successfully registred on our site.\n" +
                "Your activation link is : http://localhost:8080/#/activate/" + id;
        new Thread(() -> emailService.sendMail(userMail, subject, body)).start();
        return newPatient;
    }

    @Override
    public boolean isActivated(UUID patientId) {
        return getById(patientId).isActivated();
    }

    @Override
    public List<DoctorDTO> getAllPatientDoctors(UUID patientId,UserRole role){
        List<DoctorDTO> list=new ArrayList<>();
        for(Doctor d : patientRepository.getAllPatientsDoctors(patientId)){
            if(d.getRole()==role){
                DoctorDTO dto=new DoctorDTO();
                dto.setName(d.getName());
                dto.setSurname(d.getSurname());
                dto.setId(d.getId());
                list.add(dto);
            }
        }
        return list;
    }
}
