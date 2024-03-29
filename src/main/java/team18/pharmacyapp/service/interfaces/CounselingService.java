package team18.pharmacyapp.service.interfaces;

import team18.pharmacyapp.model.dtos.*;
import team18.pharmacyapp.model.exceptions.*;

import java.util.List;
import java.util.UUID;

public interface CounselingService {
    List<PharmacyMarkPriceDTO> getPharmaciesWithAvailableCounselings(DateTimeRangeDTO timeRange) throws BadTimeRangeException;

    List<DoctorMarkPharmaciesDTO> getFreeDoctorsForPharmacy(UUID pharmacyId, DateTimeRangeDTO timeRange);

    boolean patientScheduleCounseling(ScheduleCounselingDTO term) throws AlreadyScheduledException, ScheduleTermException, RuntimeException;

    boolean patientCancelCounseling(CancelTermDTO term) throws EntityNotFoundException, ActionNotAllowedException, RuntimeException;

    List<TermDTO> findAllPatientsCounselings(UUID id);
}
