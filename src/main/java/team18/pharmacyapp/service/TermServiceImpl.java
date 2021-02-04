package team18.pharmacyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team18.pharmacyapp.helpers.DateTimeHelpers;
import team18.pharmacyapp.model.Term;
import team18.pharmacyapp.model.WorkSchedule;
import team18.pharmacyapp.model.dtos.DoctorScheduleTermDTO;
import team18.pharmacyapp.model.enums.TermType;
import team18.pharmacyapp.repository.TermRepository;
import team18.pharmacyapp.repository.WorkScheduleRepository;
import team18.pharmacyapp.service.interfaces.DoctorService;
import team18.pharmacyapp.service.interfaces.PatientService;
import team18.pharmacyapp.service.interfaces.TermService;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class TermServiceImpl implements TermService {
    private final WorkScheduleRepository workScheduleRepository;
    private final TermRepository termRepository;
    private final DoctorService doctorService;
    private final PatientService patientService;

    @Autowired
    public TermServiceImpl(WorkScheduleRepository workScheduleRepository, TermRepository termRepository, DoctorService doctorService, PatientService patientService) {
        this.workScheduleRepository = workScheduleRepository;
        this.termRepository = termRepository;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    public List<Term> getAllDoctorTerms(UUID doctorId){
        return termRepository.findAllTermsForDoctor(doctorId);
    }

    public List<Term> getAllPatientTerms(UUID patientId){
        return termRepository.findAllTermsForPatient(patientId);
    }

    public boolean isDoctorWorking(UUID doctorId, UUID pharmacyId, Date startTime,Date endTime) {
        WorkSchedule workSchedule=workScheduleRepository.getDoctorSchedule(doctorId,pharmacyId);
        if(workSchedule==null){
            System.out.println("Ne radi u toj apoteci tad");
            return false;
        }
        LocalTime startWorking =DateTimeHelpers.getTimeWithoutDate(workSchedule.getFromHour());
        LocalTime endWorking =DateTimeHelpers.getTimeWithoutDate(workSchedule.getToHour());
        LocalTime start =DateTimeHelpers.getTimeWithoutDate(startTime);
        LocalTime end =DateTimeHelpers.getTimeWithoutDate(endTime);
        if(startTime.before(workSchedule.getFromHour()) || endTime.after(workSchedule.getToHour())){
            System.out.println("Nije u radnom vremenu");
            return false;
        }
        if(start.isBefore(startWorking) || start.isAfter(endWorking) ||end.isBefore(startWorking) || end.isAfter(endWorking) ){
            System.out.println("Nije u radnom vremenu");
            return false;
        }
        return true;
    }


    @Override
    public boolean isDoctorFree(UUID doctorId,Date startTime,Date endTime){
        for (Term term:getAllDoctorTerms(doctorId)) {
            if(DateTimeHelpers.checkIfTimesIntersect(startTime,endTime,term.getStartTime(),term.getEndTime())){
                System.out.println("Doktor zauzet");
                return  false;
            }
        }
        return true;
    }

    @Override
    public boolean isPacientFree(UUID patientId,Date startTime,Date endTime){
        for(Term term:getAllPatientTerms(patientId)){
            if(DateTimeHelpers.checkIfTimesIntersect(startTime,endTime,term.getStartTime(),term.getEndTime())){
                System.out.println("Pacijent zauzet");
                return false;
            }
        }
        return true;
    }

    @Override
    public Term scheduleTerm(DoctorScheduleTermDTO termDTO) {
        if(isDoctorWorking(termDTO.getDoctorId(),termDTO.getPharmacyId(),termDTO.getStartTime(),termDTO.getEndTime()) && isDoctorFree(termDTO.getDoctorId(),termDTO.getStartTime(),termDTO.getEndTime())
            && isPacientFree(termDTO.getPatientId(),termDTO.getStartTime(),termDTO.getEndTime())){
            Term term=new Term();
            term.setDoctor(doctorService.getById(termDTO.getDoctorId()));
            term.setPatient(patientService.getById(termDTO.getPatientId()));
            term.setStartTime(termDTO.getStartTime());
            term.setEndTime(termDTO.getEndTime());
            term.setLoyaltyPoints(5); //const
            term.setPrice(50);//const
            term.setType(termDTO.getType());
            return termRepository.save(term);
        }
        return null;
    }

}
