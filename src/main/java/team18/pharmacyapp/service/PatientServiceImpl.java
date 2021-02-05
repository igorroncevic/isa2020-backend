package team18.pharmacyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team18.pharmacyapp.model.Address;
import team18.pharmacyapp.model.dtos.LoginPatientDTO;
import team18.pharmacyapp.model.dtos.RegisterUserDTO;
import team18.pharmacyapp.model.dtos.UpdateProfileDataDTO;
import team18.pharmacyapp.model.enums.UserRole;
import team18.pharmacyapp.model.exceptions.ActionNotAllowedException;
import team18.pharmacyapp.model.exceptions.EntityNotFoundException;
import team18.pharmacyapp.model.medicine.Medicine;
import team18.pharmacyapp.model.users.Patient;
import team18.pharmacyapp.repository.AddressRepository;
import team18.pharmacyapp.repository.LoyaltyRepository;
import team18.pharmacyapp.repository.MedicineRepository;
import team18.pharmacyapp.repository.PatientRepository;
import team18.pharmacyapp.service.interfaces.EmailService;
import team18.pharmacyapp.service.interfaces.LoyaltyService;
import team18.pharmacyapp.service.interfaces.PatientService;

import java.util.List;
import java.util.UUID;

@Service
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;
    private final MedicineRepository medicineRepository;
    private final AddressRepository addressRepository;
    private final LoyaltyService loyaltyService;
    private final EmailService emailService;

    @Autowired
    public PatientServiceImpl(PatientRepository patientRepository, MedicineRepository medicineRepository, AddressRepository addressRepository, LoyaltyRepository loyaltyRepository, EmailService emailService){
        this.patientRepository = patientRepository;
        this.medicineRepository = medicineRepository;
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

    @Override
    public List<Medicine> getAlergicTo(UUID patientId) {
        return  patientRepository.getAlergicMedicines(patientId);
    }

    @Override
    public Patient getPatientProfileInfo(UUID id) {
        Patient patient = patientRepository.getPatientForProfile(id);
        List<Medicine> allergicTo = medicineRepository.getMedicinesPatientsAllergicTo(id);
        patient.setAlergicMedicines(allergicTo);
        return patient;
    }

    @Override
    @Transactional(rollbackFor = {ActionNotAllowedException.class, EntityNotFoundException.class, RuntimeException.class})
    public boolean updatePatientProfileInfo(UpdateProfileDataDTO patient) throws ActionNotAllowedException, EntityNotFoundException, RuntimeException {
        Patient pat = patientRepository.findById(patient.getId()).orElse(null);
        if(pat == null) throw new EntityNotFoundException("Not found");

        if(!patient.getNewPassword().equals(null) && !patient.getConfirmPassword().equals(null)){
            if(!patient.getConfirmPassword().equals(patient.getNewPassword())) throw new ActionNotAllowedException("Passwords do not match");
        }
        if(!patient.getName().equals("")) pat.setName(patient.getName());
        if(!patient.getSurname().equals("")) pat.setSurname(patient.getSurname());
        if(!patient.getPhoneNumber().equals("")) pat.setPhoneNumber(patient.getPhoneNumber());
        if(!patient.getNewPassword().equals("")) pat.setPassword(patient.getNewPassword());

        patientRepository.save(pat);

        return true;
    }

    @Override
    public Patient getById(UUID id) {
        return patientRepository.findById(id).orElse(null);
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

    public Patient register(RegisterUserDTO patient){
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
