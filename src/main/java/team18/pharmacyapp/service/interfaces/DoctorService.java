package team18.pharmacyapp.service.interfaces;

import team18.pharmacyapp.model.dtos.*;
import team18.pharmacyapp.model.enums.UserRole;
import team18.pharmacyapp.model.users.Doctor;
import team18.pharmacyapp.model.users.RegisteredUser;

import java.util.List;
import java.util.UUID;

public interface DoctorService {

    List<DoctorMarkPharmaciesDTO> findAllDoctors(UserRole role);

    List<DoctorMarkPharmaciesDTO> findAllDoctorsForPharmacy(UUID pharmacyId, UserRole role);

    Doctor getById(UUID id);

    DoctorDTO getDTOyId(UUID id);

    Doctor update(Doctor doctor);

    List<DoctorsPatientDTO> findDoctorsPatients(UUID doctorId);

    List<DoctorMarkPharmaciesDTO> getPatientsDoctors(PatientDoctorRoleDTO id);

    Doctor registerDermatologist(RegisteredUser user);

    List<PharmacyDTO> getDoctorPharmacyList(UUID doctorId);

    PharmacyDTO getCurrentPharmacy(UUID doctorId);

    PharmacyDTO getPharmacistPharmacy(UUID id);

    WorkScheduleDTO getDoctorWorkScheduleInPharmacy(UUID doctorId, UUID pharmacyId);
}
