package team18.pharmacyapp.service.interfaces;

import team18.pharmacyapp.model.Pharmacy;
import team18.pharmacyapp.model.WorkSchedule;
import team18.pharmacyapp.model.dtos.DoctorDTO;
import team18.pharmacyapp.model.users.Doctor;

import java.util.List;
import java.util.UUID;

public interface DoctorService {

    public List<DoctorDTO> findAllDermatologist();

    public List<DoctorDTO> findAllDermatologistForPharmacy(UUID pharmacyId);

}
