package team18.pharmacyapp.service.interfaces;


import team18.pharmacyapp.model.Term;
import team18.pharmacyapp.model.dtos.DoctorScheduleTermDTO;
import team18.pharmacyapp.model.dtos.DoctorTermDTO;
import team18.pharmacyapp.model.dtos.TermDTO;
import team18.pharmacyapp.model.dtos.TermPaginationDTO;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface TermService {
    boolean isDoctorFree(UUID doctorId, Date startTime, Date endTime);
    boolean isPatientFree(UUID patientId, Date startTime, Date endTime);
    Term scheduleTerm(DoctorScheduleTermDTO termDTO);
    List<Term> getAllPatientTerms(UUID patientId);
    List<Term> getAllDoctorTerms(UUID doctorId);
    TermPaginationDTO findAllPatientsPastTermsPaginated(UUID id, String sort, String termType, int page);
    TermPaginationDTO findPatientsUpcomingTermsByTypePaginated(UUID id, String sort, String termType, int page);
    List<DoctorTermDTO> getAllDoctorTermsInPharmacy(UUID doctorId, UUID pharmacyId);
    List<TermDTO> findAllPatientsUpcomingTerms(UUID id);
}
