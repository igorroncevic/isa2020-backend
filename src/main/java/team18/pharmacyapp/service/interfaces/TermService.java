package team18.pharmacyapp.service.interfaces;


import team18.pharmacyapp.model.Term;
import team18.pharmacyapp.model.dtos.DoctorScheduleTermDTO;

import java.util.Date;
import java.util.UUID;

public interface TermService {
    boolean isDoctorFree(UUID doctorId, Date startTime, Date endTime);
    boolean isPacientFree(UUID patientId,Date startTime,Date endTime);
    Term scheduleTerm(DoctorScheduleTermDTO termDTO);

}
