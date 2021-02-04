package team18.pharmacyapp.service.interfaces;

import team18.pharmacyapp.model.dtos.DoctorDTO;
import team18.pharmacyapp.model.dtos.RegisterUserDTO;
import team18.pharmacyapp.model.enums.UserRole;
import team18.pharmacyapp.model.users.Doctor;

import java.util.List;
import java.util.UUID;

public interface DoctorService {

    List<DoctorDTO> findAllDoctors(UserRole role);

    List<DoctorDTO> findAllDoctorsForPharmacy(UUID pharmacyId, UserRole role);

    Doctor getById(UUID id);

    Doctor update(Doctor doctor);

    Doctor registerDermatologist(RegisterUserDTO dermatologist);
}
