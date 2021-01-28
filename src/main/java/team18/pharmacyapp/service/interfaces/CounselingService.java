package team18.pharmacyapp.service.interfaces;

import team18.pharmacyapp.model.dtos.DateTimeRangeDTO;
import team18.pharmacyapp.model.dtos.PharmacyMarkPriceDTO;
import team18.pharmacyapp.model.users.Doctor;

import java.util.List;
import java.util.UUID;

public interface CounselingService {
    List<PharmacyMarkPriceDTO> getPharmaciesWithAvailableCounselings(DateTimeRangeDTO timeRange);

    List<Doctor> getFreeDoctorsForPharmacy(UUID pharmacyId, DateTimeRangeDTO timeRange);
}
