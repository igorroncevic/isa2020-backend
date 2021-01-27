package team18.pharmacyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team18.pharmacyapp.model.Pharmacy;
import team18.pharmacyapp.model.dtos.DoctorDTO;
import team18.pharmacyapp.model.users.Doctor;
import team18.pharmacyapp.repository.DoctorRepository;
import team18.pharmacyapp.service.interfaces.DoctorService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;

    @Autowired
    public DoctorServiceImpl(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }


    @Override
    public List<DoctorDTO> findAllDermatologist() {
        List<Doctor> dermatologists = doctorRepository.findAllDermatologist();
        List<DoctorDTO> doctorDTOs = new ArrayList<>();
        for(Doctor doctor : dermatologists) {
            List<Pharmacy> pharmacies = doctorRepository.findAllDermatologistPharmacies(doctor.getId());
            Float averageMark = doctorRepository.getAverageMarkForDoctor(doctor.getId());
            DoctorDTO doctorDTO = new DoctorDTO();
            doctorDTO.setName(doctor.getName());
            doctorDTO.setSurname(doctor.getSurname());
            doctorDTO.setAverageMark(averageMark);
            doctorDTO.setPharmacies(pharmacies);
            doctorDTOs.add(doctorDTO);
        }
        return doctorDTOs;
    }

    public List<DoctorDTO> findAllDermatologistForPharmacy(UUID pharmacyId) {
        List<Doctor> dermatologists = doctorRepository.findAllDermatologist();
        Pharmacy pharmacy = new Pharmacy();
        pharmacy.setId(pharmacyId);
        List<DoctorDTO> dermatologistsForPharmacy = new ArrayList<>();
        for(Doctor doctor : dermatologists) {
            List<Pharmacy> pharmacies = doctorRepository.findAllDermatologistPharmacies(doctor.getId());
            if(pharmacies.contains(pharmacy)) {
                Float averageMark = doctorRepository.getAverageMarkForDoctor(doctor.getId());
                DoctorDTO doctorDTO = new DoctorDTO();
                doctorDTO.setName(doctor.getName());
                doctorDTO.setSurname(doctor.getSurname());
                doctorDTO.setAverageMark(averageMark);
                doctorDTO.setPharmacies(pharmacies);
                dermatologistsForPharmacy.add(doctorDTO);
            }
        }
        return dermatologistsForPharmacy;
    }

    private List<DoctorDTO>  getDoctorDTOs(List<Doctor> doctors) {
        List<DoctorDTO> doctorDTOs = new ArrayList<>();
        for(Doctor doctor : doctors) {
            List<Pharmacy> pharmacies = doctorRepository.findAllDermatologistPharmacies(doctor.getId());
            Float averageMark = doctorRepository.getAverageMarkForDoctor(doctor.getId());
            DoctorDTO doctorDTO = new DoctorDTO();
            doctorDTO.setName(doctor.getName());
            doctorDTO.setSurname(doctor.getSurname());
            doctorDTO.setAverageMark(averageMark);
            doctorDTO.setPharmacies(pharmacies);
            doctorDTOs.add(doctorDTO);
        }
        return doctorDTOs;
    }
}
