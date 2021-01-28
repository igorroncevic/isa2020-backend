package team18.pharmacyapp.service.interfaces;

import team18.pharmacyapp.model.dtos.DateTimeRangeDTO;
import team18.pharmacyapp.model.dtos.DoctorDTO;
import team18.pharmacyapp.model.dtos.PharmacyMarkPriceDTO;
import team18.pharmacyapp.model.dtos.ScheduleCounselingDTO;
import team18.pharmacyapp.model.exceptions.ScheduleTermException;

import java.util.List;
import java.util.UUID;

public interface CounselingService {
    List<PharmacyMarkPriceDTO> getPharmaciesWithAvailableCounselings(DateTimeRangeDTO timeRange);

    List<DoctorDTO> getFreeDoctorsForPharmacy(UUID pharmacyId, DateTimeRangeDTO timeRange);

    boolean patientScheduleCounseling(ScheduleCounselingDTO term) throws ScheduleTermException, RuntimeException;
}
