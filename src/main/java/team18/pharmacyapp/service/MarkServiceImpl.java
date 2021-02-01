package team18.pharmacyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team18.pharmacyapp.model.Mark;
import team18.pharmacyapp.model.Pharmacy;
import team18.pharmacyapp.model.Term;
import team18.pharmacyapp.model.dtos.MarkDTO;
import team18.pharmacyapp.model.exceptions.ActionNotAllowedException;
import team18.pharmacyapp.model.exceptions.AlreadyGivenMarkException;
import team18.pharmacyapp.model.medicine.Medicine;
import team18.pharmacyapp.model.users.Doctor;
import team18.pharmacyapp.repository.*;
import team18.pharmacyapp.service.interfaces.MarkService;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class MarkServiceImpl implements MarkService {
    private final MarkRepository markRepository;
    private final DoctorRepository doctorRepository;
    private final PharmacyRepository pharmacyRepository;
    private final MedicineRepository medicineRepository;
    private final TermRepository termRepository;

    @Autowired
    public MarkServiceImpl(MarkRepository markRepository, DoctorRepository doctorRepository, PharmacyRepository pharmacyRepository, MedicineRepository medicineRepository, TermRepository termRepository) {
        this.markRepository = markRepository;
        this.doctorRepository = doctorRepository;
        this.pharmacyRepository = pharmacyRepository;
        this.medicineRepository = medicineRepository;
        this.termRepository = termRepository;
    }


    @Override
    public boolean giveMark(MarkDTO markDTO) throws ActionNotAllowedException, AlreadyGivenMarkException {
        boolean success = false;
        if(markDTO.getDoctorId() != null && markDTO.getMedicineId() == null && markDTO.getPharmacyId() == null) {
            success = this.giveMarkToDoctor(markDTO);
        }else if(markDTO.getDoctorId() == null && markDTO.getMedicineId() == null && markDTO.getPharmacyId() != null){
            success = this.giveMarkToPharmacy(markDTO);
        }else if(markDTO.getDoctorId() == null && markDTO.getMedicineId() != null && markDTO.getPharmacyId() == null){
            success = this.giveMarkToMedicine(markDTO);
        }

        return success;
    }

    @Override
    public boolean giveMarkToDoctor(MarkDTO markDTO) throws ActionNotAllowedException, AlreadyGivenMarkException {
        Doctor doctor = doctorRepository.checkIfPatientHadAppointmentWithDoctor(markDTO.getDoctorId(), markDTO.getPatientId(), new Date());
        if(doctor == null) throw new ActionNotAllowedException("You have not had any appointments with this doctor");

        Mark mark = markRepository.checkIfPatientHasGivenMarkToDoctor(markDTO.getDoctorId(), markDTO.getPatientId());
        if(mark != null) throw new AlreadyGivenMarkException("You already gave mark to this doctor");

        UUID markId = UUID.randomUUID();
        int markGiven = markRepository.giveMarkToDoctor(markId, markDTO.getMarkValue(), markDTO.getDoctorId(), markDTO.getPatientId());

        if(markGiven != 1) throw new RuntimeException("Unable to give mark.");

        return true;
    }

    @Override
    public boolean giveMarkToPharmacy(MarkDTO markDTO) throws ActionNotAllowedException, AlreadyGivenMarkException {
        List<Medicine> reservedMedicines = medicineRepository.getPatientsReservedMedicinesFromPharmacy(markDTO.getPharmacyId(), markDTO.getPatientId());
        List<Medicine> ePrescriptionMedicines = medicineRepository.getPatientsEPrescriptionMedicinesFromPharmacy(markDTO.getPharmacyId(), markDTO.getPatientId());
        boolean takenMedicines = true;
        if(reservedMedicines.size() == 0 && ePrescriptionMedicines.size() == 0)
            takenMedicines = false;

        List<Term> terms = termRepository.getPatientsTermsFromPharmacy(markDTO.getPharmacyId(), markDTO.getPatientId());
        boolean hadTerms = true;
        if(terms.size() == 0 && !takenMedicines)
            hadTerms = false;

        if(!hadTerms && !takenMedicines)
            throw new ActionNotAllowedException("You cannot give mark to this pharmacy");

        Mark mark = markRepository.checkIfPatientHasGivenMarkToPharmacy(markDTO.getPharmacyId(), markDTO.getPatientId());
        if(mark != null) throw new AlreadyGivenMarkException("You already gave mark to this pharmacy");

        UUID markId = UUID.randomUUID();
        int markGiven = markRepository.giveMarkToPharmacy(markId, markDTO.getMarkValue(), markDTO.getPharmacyId(), markDTO.getPatientId());

        if(markGiven != 1) throw new RuntimeException("Unable to give mark.");

        return true;
    }

    @Override
    public boolean giveMarkToMedicine(MarkDTO markDTO) throws ActionNotAllowedException, AlreadyGivenMarkException {
        Medicine medicine = medicineRepository.checkIfPatientReservedMedicine(markDTO.getMedicineId(), markDTO.getPatientId());
        if(medicine == null) throw new ActionNotAllowedException("You have not taken any medicines from this pharmacy");

        Mark mark = markRepository.checkIfPatientHasGivenMarkToMedicine(markDTO.getMedicineId(), markDTO.getPatientId());
        if(mark != null) throw new AlreadyGivenMarkException("You already gave mark to this pharmacy");

        UUID markId = UUID.randomUUID();
        int markGiven = markRepository.giveMarkToMedicine(markId, markDTO.getMarkValue(), markDTO.getPharmacyId(), markDTO.getPatientId());

        if(markGiven != 1) throw new RuntimeException("Unable to give mark.");

        return true;
    }

}
