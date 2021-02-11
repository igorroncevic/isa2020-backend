package team18.pharmacyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team18.pharmacyapp.model.Pharmacy;
import team18.pharmacyapp.model.dtos.DoctorMarkPharmaciesDTO;
import team18.pharmacyapp.model.dtos.PatientDoctorRoleDTO;
import team18.pharmacyapp.model.dtos.DoctorsPatientDTO;
import team18.pharmacyapp.model.dtos.PharmacyDTO;
import team18.pharmacyapp.model.enums.UserRole;
import team18.pharmacyapp.model.users.Doctor;
import team18.pharmacyapp.model.users.RegisteredUser;
import team18.pharmacyapp.repository.users.DoctorRepository;
import team18.pharmacyapp.repository.MarkRepository;
import team18.pharmacyapp.service.interfaces.DoctorService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;
    private final MarkRepository markRepository;


    @Autowired
    public DoctorServiceImpl(DoctorRepository doctorRepository, MarkRepository markRepository) {
        this.doctorRepository = doctorRepository;
        this.markRepository = markRepository;
    }


    @Override
    public List<DoctorMarkPharmaciesDTO> findAllDoctors(UserRole role) {
        List<Doctor> dermatologists = doctorRepository.findAllDoctors(role);
        List<DoctorMarkPharmaciesDTO> doctorMarkPharmaciesDTOS = new ArrayList<>();
        for(Doctor doctor : dermatologists) {
            List<String> pharmacies = doctorRepository.findAllDoctorsPharmaciesNames(doctor.getId());
            Float averageMark = markRepository.getAverageMarkForDoctor(doctor.getId());
            DoctorMarkPharmaciesDTO doctorMarkPharmaciesDTO = new DoctorMarkPharmaciesDTO();
            doctorMarkPharmaciesDTO.setId(doctor.getId());
            doctorMarkPharmaciesDTO.setName(doctor.getName());
            doctorMarkPharmaciesDTO.setSurname(doctor.getSurname());
            doctorMarkPharmaciesDTO.setAverageMark(averageMark);
            doctorMarkPharmaciesDTO.setPharmacies(pharmacies);
            doctorMarkPharmaciesDTOS.add(doctorMarkPharmaciesDTO);
        }
        return doctorMarkPharmaciesDTOS;
    }

    public List<DoctorMarkPharmaciesDTO> findAllDoctorsForPharmacy(UUID pharmacyId, UserRole role) {
        List<Doctor> dermatologists = doctorRepository.findAllDoctors(role);
        Pharmacy pharmacy = new Pharmacy();
        pharmacy.setId(pharmacyId);
        List<DoctorMarkPharmaciesDTO> dermatologistsForPharmacy = new ArrayList<>();
        for (Doctor doctor : dermatologists) {
            List<Pharmacy> pharmacies = doctorRepository.findAllDoctorsPharmacies(doctor.getId());
            if(pharmacies.contains(pharmacy)) {
                List<String> pharmaciesNames = doctorRepository.findAllDoctorsPharmaciesNames(doctor.getId());
                Float averageMark = markRepository.getAverageMarkForDoctor(doctor.getId());
                DoctorMarkPharmaciesDTO doctorMarkPharmaciesDTO = new DoctorMarkPharmaciesDTO();
                doctorMarkPharmaciesDTO.setId(doctor.getId());
                doctorMarkPharmaciesDTO.setName(doctor.getName());
                doctorMarkPharmaciesDTO.setSurname(doctor.getSurname());
                doctorMarkPharmaciesDTO.setAverageMark(averageMark);
                doctorMarkPharmaciesDTO.setPharmacies(pharmaciesNames);
                dermatologistsForPharmacy.add(doctorMarkPharmaciesDTO);
            }
        }
        return dermatologistsForPharmacy;
    }

    public Doctor getById(UUID id) {
        return doctorRepository.findById(id).orElse(null);
    }

    @Override
    public Doctor update(Doctor doctor) {
        Doctor doc=getById(doctor.getId());
        if(doc!=null) {
            doc.setName(doctor.getName());
            doc.setSurname(doctor.getSurname());
            doc.setEmail(doctor.getEmail());
            doc.setPhoneNumber(doctor.getPhoneNumber());
            return doctorRepository.save(doc);
        }
        return null;
    }

    @Override
    public Doctor registerDermatologist(RegisteredUser user) {
        Doctor derm = new Doctor();
        derm.setRole(UserRole.dermatologist);
        derm.setName(user.getName());
        derm.setSurname(user.getSurname());
        derm.setPhoneNumber(user.getPhoneNumber());
        derm.setEmail(user.getEmail());
        derm.setPassword(user.getPassword());
        derm.setAddress(user.getAddress());
        derm = doctorRepository.save(derm);
        doctorRepository.setId(derm.getId(),user.getId());
        return derm;
    }

    @Override
    public List<PharmacyDTO> getDoctorPharmacyList(UUID doctorId) {
        List<PharmacyDTO> ret=new ArrayList<>();
        for(Pharmacy pharmacy:doctorRepository.getDoctorPharmacyList(doctorId)){
            ret.add(new PharmacyDTO(pharmacy.getId(),pharmacy.getName(),pharmacy.getAddress().getCountry(),pharmacy.getAddress().getCity(),pharmacy.getAddress().getStreet()));
        }
        return ret;
    }

    public List<DoctorsPatientDTO> findDoctorsPatients(UUID doctorId) {
        return doctorRepository.findDoctorPatients(doctorId);

    }

    @Override
    public List<DoctorMarkPharmaciesDTO> getPatientsDoctors(PatientDoctorRoleDTO patientDoctorRoleDTO) {
        List<Doctor> doctors = doctorRepository.getPatientsDoctors(patientDoctorRoleDTO.getPatientId(),
                patientDoctorRoleDTO.getDoctorRole(), new Date(System.currentTimeMillis()));
        List<DoctorMarkPharmaciesDTO> doctorMarkDTOS = new ArrayList<>();
        for (Doctor d : doctors) {
            DoctorMarkPharmaciesDTO doctorMarkPharmaciesDTO = new DoctorMarkPharmaciesDTO();
            doctorMarkPharmaciesDTO.setId(d.getId());
            doctorMarkPharmaciesDTO.setName(d.getName());
            doctorMarkPharmaciesDTO.setSurname(d.getSurname());
            Float averageMark = markRepository.getAverageMarkForDoctor(d.getId());
            doctorMarkPharmaciesDTO.setAverageMark(averageMark);
            doctorMarkDTOS.add(doctorMarkPharmaciesDTO);
        }

        return doctorMarkDTOS;
    }
}
