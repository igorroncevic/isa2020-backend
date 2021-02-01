package team18.pharmacyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team18.pharmacyapp.model.Address;
import team18.pharmacyapp.model.dtos.LoginPatientDTO;
import team18.pharmacyapp.model.dtos.RegisterPatientDTO;
import team18.pharmacyapp.model.enums.UserRole;
import team18.pharmacyapp.model.users.Patient;
import team18.pharmacyapp.repository.AddressRepository;
import team18.pharmacyapp.repository.LoyaltyRepository;
import team18.pharmacyapp.repository.PatientRepository;
import team18.pharmacyapp.service.interfaces.EmailService;
import team18.pharmacyapp.service.interfaces.LoyaltyService;
import team18.pharmacyapp.service.interfaces.PatientService;

import java.util.List;
import java.util.UUID;

@Service
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;
    private final AddressRepository addressRepository;
    private final LoyaltyService loyaltyService;
    private final EmailService emailService;

    @Autowired
    public PatientServiceImpl(PatientRepository patientRepository, AddressRepository addressRepository, LoyaltyRepository loyaltyRepository, EmailService emailService){
        this.patientRepository = patientRepository;
        this.addressRepository = addressRepository;
        this.loyaltyService = new LoyaltyServiceImpl(loyaltyRepository);
        this.emailService = emailService;
    }

    @Override
    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    @Override
    public int addPenalty(UUID patientId) {
        return patientRepository.addPenalty(patientId);
    }

    public Patient findRegisteredPatient(LoginPatientDTO patient){
        return patientRepository.findByEmailAndPassword(patient.getEmail(),patient.getPassword());
    }

    public boolean activateAccount(UUID id){
        Patient p = patientRepository.findById(id).orElse(null);
        if(p != null){
            p.setActivated(true);
            patientRepository.save(p);
            return true;
        }
        return false;
    }

    public Patient register(RegisterPatientDTO patient){
        Patient pat = new Patient();
        Address address=addressRepository.findByCountryAndCityAndStreet(patient.getCountry(), patient.getCity(), patient.getStreet());
        if(address == null){
            address = new Address();
            address.setStreet(patient.getStreet());
            address.setCity(patient.getCity());
            address.setCountry(patient.getCountry());
            address = addressRepository.save(address);
        }
        pat.setRole(UserRole.patient);
        pat.setName(patient.getName());
        pat.setSurname(patient.getSurname());
        pat.setPhoneNumber(patient.getPhoneNumber());
        pat.setEmail(patient.getEmail());
        pat.setPassword(patient.getPassword());
        pat.setAddress(address);
        pat.setLoyalty(loyaltyService.findOneByCategory("Regular"));
        Patient newPatient= patientRepository.save(pat);
        UUID id= patientRepository.findByEmail(newPatient.getEmail()).getId();
        String userMail = patient.getEmail();   // zakucano za sada
        String subject = "[ISA Pharmacy]  Account activation";
        String body = "You have successfully registred on our site.\n" +
                "Your activation link is : http://localhost:8080/#/activate/" +id;
        new Thread(() -> emailService.sendMail(userMail, subject, body)).start();
        return newPatient;
    }
}
