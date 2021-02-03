package team18.pharmacyapp.service.interfaces;

import team18.pharmacyapp.model.Pharmacy;
import team18.pharmacyapp.model.WorkSchedule;
import team18.pharmacyapp.model.dtos.DoctorDTO;
import team18.pharmacyapp.model.dtos.DoctorsPatientDTO;
import team18.pharmacyapp.model.dtos.PatientDoctorRoleDTO;
import team18.pharmacyapp.model.enums.UserRole;
import team18.pharmacyapp.model.users.Doctor;

import java.util.List;
import java.util.UUID;

public interface DoctorService {

    public List<DoctorDTO> findAllDoctors(UserRole role);

    public List<DoctorDTO> findAllDoctorsForPharmacy(UUID pharmacyId, UserRole role);

    public Doctor getById(UUID id);

    public Doctor update(Doctor doctor);

    List<DoctorsPatientDTO> findDoctorsPatients(UUID doctorId);

    List<DoctorDTO> getPatientsDoctors(PatientDoctorRoleDTO id);

}
