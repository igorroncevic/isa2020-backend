package team18.pharmacyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import team18.pharmacyapp.helpers.DateTimeHelpers;
import team18.pharmacyapp.model.Term;
import team18.pharmacyapp.model.WorkSchedule;
import team18.pharmacyapp.model.dtos.DoctorDTO;
import team18.pharmacyapp.model.dtos.DoctorScheduleTermDTO;
import team18.pharmacyapp.model.dtos.TermDTO;
import team18.pharmacyapp.model.dtos.TermPaginationDTO;
import team18.pharmacyapp.model.enums.TermType;
import team18.pharmacyapp.model.users.Doctor;
import team18.pharmacyapp.repository.users.DoctorRepository;
import team18.pharmacyapp.repository.TermRepository;
import team18.pharmacyapp.repository.WorkScheduleRepository;
import team18.pharmacyapp.service.interfaces.DoctorService;
import team18.pharmacyapp.service.interfaces.PatientService;
import team18.pharmacyapp.service.interfaces.TermService;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class TermServiceImpl implements TermService {
    private final WorkScheduleRepository workScheduleRepository;
    private final TermRepository termRepository;
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final DoctorRepository doctorRepository;

    @Autowired
    public TermServiceImpl(WorkScheduleRepository workScheduleRepository, TermRepository termRepository, DoctorService doctorService, PatientService patientService, DoctorRepository doctorRepository) {
        this.workScheduleRepository = workScheduleRepository;
        this.termRepository = termRepository;
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.doctorRepository = doctorRepository;
    }

    @Override
    public List<Term> getAllDoctorTerms(UUID doctorId){
        List<Term> list=new ArrayList<>();
        list.addAll(termRepository.findAllFreeTermsForDoctor(doctorId));
        list.addAll(termRepository.findAllTermsForDoctor(doctorId));
        return list;
    }

    @Override
    public List<Term> getAllDoctorTermsInPharmacy(UUID doctorId,UUID pharmacyId) {
        List<Term> list=new ArrayList<>();
        list.addAll(termRepository.findAllFreeTermsForDoctorInPharmacy(doctorId,pharmacyId));
        list.addAll(termRepository.findAllTermsForDoctorInPharmacy(doctorId,pharmacyId));
        return list;
    }

    @Override
    public List<Term> getAllPatientTerms(UUID patientId){
        return termRepository.findAllTermsForPatient(patientId);
    }

    public boolean isDoctorWorking(UUID doctorId, UUID pharmacyId, Date startTime,Date endTime) {
        WorkSchedule workSchedule=workScheduleRepository.getDoctorSchedule(doctorId,pharmacyId);
        if(workSchedule == null){
            return false;
        }
        LocalTime startWorking =DateTimeHelpers.getTimeWithoutDate(workSchedule.getFromHour());
        LocalTime endWorking =DateTimeHelpers.getTimeWithoutDate(workSchedule.getToHour());
        LocalTime start =DateTimeHelpers.getTimeWithoutDate(startTime);
        LocalTime end =DateTimeHelpers.getTimeWithoutDate(endTime);
        if(startTime.before(workSchedule.getFromHour()) || endTime.after(workSchedule.getToHour())){
            return false;
        }
        if(start.isBefore(startWorking) || start.isAfter(endWorking) ||end.isBefore(startWorking) || end.isAfter(endWorking) ){
            return false;
        }
        return true;
    }


    @Override
    public boolean isDoctorFree(UUID doctorId,Date startTime,Date endTime){
        for (Term term : getAllDoctorTerms(doctorId)) {
            if(DateTimeHelpers.checkIfTimesIntersect(startTime,endTime,term.getStartTime(),term.getEndTime())){
                return  false;
            }
        }
        return true;
    }

    @Override
    public boolean isPatientFree(UUID patientId, Date startTime, Date endTime){
        for(Term term:getAllPatientTerms(patientId)){
            if(DateTimeHelpers.checkIfTimesIntersect(startTime,endTime,term.getStartTime(),term.getEndTime())){
                return false;
            }
        }
        return true;
    }

    @Override
    public Term scheduleTerm(DoctorScheduleTermDTO termDTO) {
        if(isDoctorWorking(termDTO.getDoctorId(),termDTO.getPharmacyId(),termDTO.getStartTime(),termDTO.getEndTime()) && isDoctorFree(termDTO.getDoctorId(),termDTO.getStartTime(),termDTO.getEndTime())
            && isPatientFree(termDTO.getPatientId(),termDTO.getStartTime(),termDTO.getEndTime()) && checkDates(termDTO.getStartTime(),termDTO.getEndTime())){
            Term term=new Term();
            term.setDoctor(doctorService.getById(termDTO.getDoctorId()));
            term.setPatient(patientService.getById(termDTO.getPatientId()));
            term.setStartTime(termDTO.getStartTime());
            term.setEndTime(termDTO.getEndTime());
            term.setPrice(50);//const
            term.setType(termDTO.getType());
            return termRepository.save(term);
        }
        return null;
    }

    public boolean checkDates(Date startTime,Date endTime){
        if(!startTime.before(endTime)){
            System.out.println("Kraj termina pre pocetka");
            return false;
        }
        return true;
    }

    @Override
    public TermPaginationDTO findAllPatientsPastTermsPaginated(UUID id, String sort, String termType, int page) {
        String[] sortParts = sort.split(" ");
        int startPage = page - 1; // Jer krecu od 1, a ako hocemo prvi da prikazemo, Pageable krece od 0

        Pageable pageable;
        if(sortParts[1].equalsIgnoreCase("asc.")){
            pageable = PageRequest.of(startPage, 3, Sort.by(sortParts[0]).ascending());  // Zakucano 3 po stranici
        }else {
            pageable = PageRequest.of(startPage, 3, Sort.by(sortParts[0]).descending());
        }
        Page<Term> allTerms = termRepository.findAllByPatient_IdAndTypeAndStartTimeBefore(id, TermType.valueOf(termType), new Date(System.currentTimeMillis()), pageable);

        TermPaginationDTO response = new TermPaginationDTO();
        if(!allTerms.hasContent()) return response;

        List<TermDTO> upcomingTerms = new ArrayList<>();
        for(Term t : allTerms) {
            Doctor doctor = doctorRepository.findDoctorByTermId(t.getId());
            DoctorDTO doctorDto = new DoctorDTO(doctor.getId(), doctor.getName(), doctor.getSurname(), doctor.getEmail(), doctor.getPhoneNumber(),
                    doctor.getRole(), null);
            TermDTO termDto = new TermDTO(t.getId(), t.getStartTime(), t.getEndTime(), t.getPrice(), t.getType(), doctorDto);
            upcomingTerms.add(termDto);
        }

        response.setTerms(new ArrayList<>(upcomingTerms));
        response.setTotalPages(allTerms.getTotalPages());

        return response;
    }

    @Override
    public TermPaginationDTO findPatientsUpcomingTermsByTypePaginated(UUID id, String sort, String termType, int page) {
        String[] sortParts = sort.split(" ");
        int startPage = page - 1; // Jer krecu od 1, a ako hocemo prvi da prikazemo, Pageable krece od 0

        Pageable pageable;
        if(sortParts[1].equalsIgnoreCase("asc.")){
            pageable = PageRequest.of(startPage, 3, Sort.by(sortParts[0]).ascending());  // Zakucano 3 po stranici
        }else {
            pageable = PageRequest.of(startPage, 3, Sort.by(sortParts[0]).descending());
        }
        Page<Term> allTerms = termRepository.findAllByPatient_IdAndTypeAndStartTimeAfter(id, TermType.valueOf(termType), new Date(System.currentTimeMillis()), pageable);

        TermPaginationDTO response = new TermPaginationDTO();
        if(!allTerms.hasContent()) return response;

        List<TermDTO> upcomingTerms = new ArrayList<>();
        for(Term t : allTerms) {
            Doctor doctor = doctorRepository.findDoctorByTermId(t.getId());
            DoctorDTO doctorDto = new DoctorDTO(doctor.getId(), doctor.getName(), doctor.getSurname(), doctor.getEmail(), doctor.getPhoneNumber(),
                    doctor.getRole(), null);
            TermDTO termDto = new TermDTO(t.getId(), t.getStartTime(), t.getEndTime(), t.getPrice(), t.getType(), doctorDto);
            upcomingTerms.add(termDto);
        }

        response.setTerms(upcomingTerms);
        response.setTotalPages(allTerms.getTotalPages());

        return response;
    }

    @Override
    public List<TermDTO> findAllPatientsUpcomingTerms(UUID id) {
        Pageable pageable = PageRequest.of(0, 3, Sort.by("startTime").ascending());
        Page<Term> allTerms = termRepository.findAllByPatient_IdAndStartTimeAfter(id, new Date(System.currentTimeMillis()), pageable);

        List<TermDTO> upcomingTerms = new ArrayList<>();
        if(!allTerms.hasContent()) return upcomingTerms;

        for(Term t : allTerms) {
            Doctor doctor = doctorRepository.findDoctorByTermId(t.getId());
            DoctorDTO doctorDto = new DoctorDTO(doctor.getId(), doctor.getName(), doctor.getSurname(), doctor.getEmail(), doctor.getPhoneNumber(),
                    doctor.getRole(), null);
            TermDTO termDto = new TermDTO(t.getId(), t.getStartTime(), t.getEndTime(), t.getPrice(), t.getType(), doctorDto);
            upcomingTerms.add(termDto);
        }

        return upcomingTerms;
    }

}
