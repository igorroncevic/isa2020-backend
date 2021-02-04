package team18.pharmacyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team18.pharmacyapp.model.Address;
import team18.pharmacyapp.model.Pharmacy;
import team18.pharmacyapp.model.dtos.DoctorDTO;
import team18.pharmacyapp.model.dtos.RegisterUserDTO;
import team18.pharmacyapp.model.enums.UserRole;
import team18.pharmacyapp.model.users.Doctor;
import team18.pharmacyapp.repository.AddressRepository;
import team18.pharmacyapp.repository.DoctorRepository;
import team18.pharmacyapp.service.interfaces.DoctorService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;
    private final AddressRepository addressRepository;

    @Autowired
    public DoctorServiceImpl(DoctorRepository doctorRepository, AddressRepository addressRepository) {
        this.doctorRepository = doctorRepository;
        this.addressRepository = addressRepository;
    }


    @Override
    public List<DoctorDTO> findAllDoctors(UserRole role) {
        List<Doctor> dermatologists = doctorRepository.findAllDoctors(role);
        List<DoctorDTO> doctorDTOs = new ArrayList<>();
        for(Doctor doctor : dermatologists) {
            List<String> pharmacies = doctorRepository.findAllDoctorsPharmaciesNames(doctor.getId());
            Float averageMark = doctorRepository.getAverageMarkForDoctor(doctor.getId());
            DoctorDTO doctorDTO = new DoctorDTO();
            doctorDTO.setId(doctor.getId());
            doctorDTO.setName(doctor.getName());
            doctorDTO.setSurname(doctor.getSurname());
            doctorDTO.setAverageMark(averageMark);
            doctorDTO.setPharmacies(pharmacies);
            doctorDTOs.add(doctorDTO);
        }
        return doctorDTOs;
    }

    public List<DoctorDTO> findAllDoctorsForPharmacy(UUID pharmacyId, UserRole role) {
        List<Doctor> dermatologists = doctorRepository.findAllDoctors(role);
        Pharmacy pharmacy = new Pharmacy();
        pharmacy.setId(pharmacyId);
        List<DoctorDTO> dermatologistsForPharmacy = new ArrayList<>();
        for (Doctor doctor : dermatologists) {
            List<Pharmacy> pharmacies = doctorRepository.findAllDoctorsPharmacies(doctor.getId());
            if(pharmacies.contains(pharmacy)) {
                List<String> pharmaciesNames = doctorRepository.findAllDoctorsPharmaciesNames(doctor.getId());
                Float averageMark = doctorRepository.getAverageMarkForDoctor(doctor.getId());
                DoctorDTO doctorDTO = new DoctorDTO();
                doctorDTO.setId(doctor.getId());
                doctorDTO.setName(doctor.getName());
                doctorDTO.setSurname(doctor.getSurname());
                doctorDTO.setAverageMark(averageMark);
                doctorDTO.setPharmacies(pharmaciesNames);
                dermatologistsForPharmacy.add(doctorDTO);
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
    public Doctor registerDermatologist(RegisterUserDTO dermatologist) {
        Doctor derm = new Doctor();
        Address address=addressRepository.findByCountryAndCityAndStreet(dermatologist.getCountry(), dermatologist.getCity(), dermatologist.getStreet());
        if(address == null){
            address = new Address();
            address.setStreet(dermatologist.getStreet());
            address.setCity(dermatologist.getCity());
            address.setCountry(dermatologist.getCountry());
            address = addressRepository.save(address);
        }
        derm.setRole(UserRole.dermatologist);
        derm.setName(dermatologist.getName());
        derm.setSurname(dermatologist.getSurname());
        derm.setPhoneNumber(dermatologist.getPhoneNumber());
        derm.setEmail(dermatologist.getEmail());
        derm.setPassword(dermatologist.getPassword());
        derm.setAddress(address);
        derm = doctorRepository.save(derm);
        return derm;
    }
}
