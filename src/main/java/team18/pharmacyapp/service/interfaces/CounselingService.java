package team18.pharmacyapp.service.interfaces;

import team18.pharmacyapp.model.dtos.DateTimeRangeDTO;
import team18.pharmacyapp.model.dtos.PharmacyMarkPriceDTO;

import java.util.List;

public interface CounselingService {
    List<PharmacyMarkPriceDTO> getPharmaciesWithAvailableCounselings(DateTimeRangeDTO timeRange);
}
