package team18.pharmacyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team18.pharmacyapp.model.dtos.DateTimeRangeDTO;
import team18.pharmacyapp.model.dtos.PharmacyMarkPriceDTO;
import team18.pharmacyapp.repository.CounselingRepository;
import team18.pharmacyapp.service.interfaces.CounselingService;

import java.util.List;

@Service
public class CounselingServiceImpl implements CounselingService {
    private final CounselingRepository counselingRepository;

    @Autowired
    public CounselingServiceImpl(CounselingRepository counselingRepository) {
        this.counselingRepository = counselingRepository;
    }

    @Override
    public List<PharmacyMarkPriceDTO> getPharmaciesWithAvailableCounselings(DateTimeRangeDTO timeRange) {
        return counselingRepository.getPharmaciesWithAvailableCounselings(timeRange.getFromTime(), timeRange.getToTime());
    }
}
