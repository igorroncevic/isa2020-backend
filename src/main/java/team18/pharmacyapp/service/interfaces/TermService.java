package team18.pharmacyapp.service.interfaces;


import team18.pharmacyapp.model.Term;
import team18.pharmacyapp.model.dtos.DoctorScheduleTermDTO;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface TermService {
    boolean isDoctorFree(UUID doctorId, Date startTime, Date endTime);
    boolean isPatientFree(UUID patientId, Date startTime, Date endTime);
    Term scheduleTerm(DoctorScheduleTermDTO termDTO);
    List<Term> getAllPatientTerms(UUID patientId);
    List<Term> getAllDoctorTerms(UUID doctorId);
    List<Term> getAllDoctorTermsInPharmacy(UUID doctorId,UUID pharmacyId);

}
