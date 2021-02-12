package team18.pharmacyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team18.pharmacyapp.model.Mark;
import team18.pharmacyapp.model.Term;
import team18.pharmacyapp.model.dtos.MarkDTO;
import team18.pharmacyapp.model.exceptions.ActionNotAllowedException;
import team18.pharmacyapp.model.exceptions.AlreadyGivenMarkException;
import team18.pharmacyapp.model.medicine.Medicine;
import team18.pharmacyapp.model.users.Doctor;
import team18.pharmacyapp.repository.MarkRepository;
import team18.pharmacyapp.repository.MedicineRepository;
import team18.pharmacyapp.repository.PharmacyRepository;
import team18.pharmacyapp.repository.TermRepository;
import team18.pharmacyapp.repository.users.DoctorRepository;
import team18.pharmacyapp.service.interfaces.MarkService;

import javax.persistence.LockModeType;
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
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Transactional(rollbackFor = {ActionNotAllowedException.class, AlreadyGivenMarkException.class, RuntimeException.class})
    public boolean giveMark(MarkDTO markDTO) throws ActionNotAllowedException, AlreadyGivenMarkException, RuntimeException {
        boolean success = false;
        if (markDTO.getDoctorId() != null && markDTO.getMedicineId() == null && markDTO.getPharmacyId() == null) {
            success = this.giveMarkToDoctor(markDTO);
        } else if (markDTO.getDoctorId() == null && markDTO.getMedicineId() == null && markDTO.getPharmacyId() != null) {
            success = this.giveMarkToPharmacy(markDTO);
        } else if (markDTO.getDoctorId() == null && markDTO.getMedicineId() != null && markDTO.getPharmacyId() == null) {
            success = this.giveMarkToMedicine(markDTO);
        }

        return success;
    }

    @Override
    @Lock(LockModeType.WRITE)
    @Transactional(rollbackFor = {ActionNotAllowedException.class, RuntimeException.class})
    public boolean updateMark(MarkDTO markDTO) throws ActionNotAllowedException{
        boolean success = false;
        if (markDTO.getDoctorId() != null && markDTO.getMedicineId() == null && markDTO.getPharmacyId() == null) {
            success = this.updateDoctorsMark(markDTO);
        } else if (markDTO.getDoctorId() == null && markDTO.getMedicineId() == null && markDTO.getPharmacyId() != null) {
            success = this.updatePharmacysMark(markDTO);
        } else if (markDTO.getDoctorId() == null && markDTO.getMedicineId() != null && markDTO.getPharmacyId() == null) {
            success = this.updateMedicinesMark(markDTO);
        }

        return success;
    }

    @Override
    @Transactional(rollbackFor = {ActionNotAllowedException.class, AlreadyGivenMarkException.class, RuntimeException.class})
    public boolean giveMarkToDoctor(MarkDTO markDTO) throws ActionNotAllowedException, AlreadyGivenMarkException {
        Doctor doctor = doctorRepository.checkIfPatientHadAppointmentWithDoctor(markDTO.getDoctorId(), markDTO.getPatientId(), new Date());
        if (doctor == null) throw new ActionNotAllowedException("You have not had any appointments with this doctor");

        Mark mark = markRepository.checkIfPatientHasGivenMarkToDoctor(markDTO.getDoctorId(), markDTO.getPatientId());
        if (mark != null) throw new AlreadyGivenMarkException("You already gave mark to this doctor");

        UUID markId = UUID.randomUUID();
        int markGiven = markRepository.giveMarkToDoctor(markId, markDTO.getMarkValue(), markDTO.getDoctorId(), markDTO.getPatientId());

        if (markGiven != 1) throw new RuntimeException("Unable to give mark.");

        return true;
    }

    @Override
    @Transactional(rollbackFor = {ActionNotAllowedException.class, AlreadyGivenMarkException.class, RuntimeException.class})
    public boolean giveMarkToPharmacy(MarkDTO markDTO) throws ActionNotAllowedException, AlreadyGivenMarkException {
        List<Medicine> reservedMedicines = medicineRepository.getPatientsReservedMedicinesFromPharmacy(markDTO.getPharmacyId(), markDTO.getPatientId());
        List<Medicine> ePrescriptionMedicines = medicineRepository.getPatientsEPrescriptionMedicinesFromPharmacy(markDTO.getPharmacyId(), markDTO.getPatientId());
        boolean takenMedicines = true;
        if (reservedMedicines.size() == 0 && ePrescriptionMedicines.size() == 0)
            takenMedicines = false;

        List<Term> terms = termRepository.getPatientsTermsFromPharmacy(markDTO.getPharmacyId(), markDTO.getPatientId());
        boolean hadTerms = true;
        if (terms.size() == 0 && !takenMedicines)
            hadTerms = false;

        if (!hadTerms && !takenMedicines)
            throw new ActionNotAllowedException("You cannot give mark to this pharmacy");

        Mark mark = markRepository.checkIfPatientHasGivenMarkToPharmacy(markDTO.getPharmacyId(), markDTO.getPatientId());
        if (mark != null) throw new AlreadyGivenMarkException("You already gave mark to this pharmacy");

        UUID markId = UUID.randomUUID();
        int markGiven = markRepository.giveMarkToPharmacy(markId, markDTO.getMarkValue(), markDTO.getPharmacyId(), markDTO.getPatientId());

        if (markGiven != 1) throw new RuntimeException("Unable to give mark.");

        return true;
    }

    @Override
    @Transactional(rollbackFor = {ActionNotAllowedException.class, AlreadyGivenMarkException.class, RuntimeException.class})
    public boolean giveMarkToMedicine(MarkDTO markDTO) throws ActionNotAllowedException, AlreadyGivenMarkException {
        Medicine medicineReserved = medicineRepository.checkIfPatientReservedMedicine(markDTO.getMedicineId(), markDTO.getPatientId());
        Medicine medicinePrescribed = medicineRepository.checkIfPatientGotPrescribedMedicine(markDTO.getMedicineId(), markDTO.getPatientId());
        if (medicineReserved == null && medicinePrescribed == null)
            throw new ActionNotAllowedException("You have not taken this medicine before");

        Mark mark = markRepository.checkIfPatientHasGivenMarkToMedicine(markDTO.getMedicineId(), markDTO.getPatientId());
        if (mark != null) throw new AlreadyGivenMarkException("You already gave mark to this medicine");

        UUID markId = UUID.randomUUID();
        int markGiven = markRepository.giveMarkToMedicine(markId, markDTO.getMarkValue(), markDTO.getMedicineId(), markDTO.getPatientId());
        if (markGiven != 1) throw new RuntimeException("Unable to give mark.");

        return true;
    }

    @Override
    @Transactional(rollbackFor = {ActionNotAllowedException.class, RuntimeException.class})
    public boolean updateDoctorsMark(MarkDTO markDTO) throws ActionNotAllowedException {
        Doctor doctor = doctorRepository.checkIfPatientHadAppointmentWithDoctor(markDTO.getDoctorId(), markDTO.getPatientId(), new Date());
        if (doctor == null) throw new ActionNotAllowedException("You have not had any appointments with this doctor");

        Mark mark = markRepository.checkIfPatientHasGivenMarkToDoctor(markDTO.getDoctorId(), markDTO.getPatientId());
        if (mark == null) throw new ActionNotAllowedException("You didn't give mark to this doctor");

        int markUpdated = markRepository.updateDoctorsMark(markDTO.getMarkValue(), markDTO.getDoctorId(), markDTO.getPatientId());
        if (markUpdated != 1) throw new RuntimeException("Unable to give mark.");

        return true;
    }

    @Override
    @Transactional(rollbackFor = {ActionNotAllowedException.class, RuntimeException.class})
    public boolean updatePharmacysMark(MarkDTO markDTO) throws ActionNotAllowedException {
        List<Medicine> reservedMedicines = medicineRepository.getPatientsReservedMedicinesFromPharmacy(markDTO.getPharmacyId(), markDTO.getPatientId());
        List<Medicine> ePrescriptionMedicines = medicineRepository.getPatientsEPrescriptionMedicinesFromPharmacy(markDTO.getPharmacyId(), markDTO.getPatientId());
        boolean takenMedicines = true;
        if (reservedMedicines.size() == 0 && ePrescriptionMedicines.size() == 0)
            takenMedicines = false;

        List<Term> terms = termRepository.getPatientsTermsFromPharmacy(markDTO.getPharmacyId(), markDTO.getPatientId());
        boolean hadTerms = true;
        if (terms.size() == 0 && !takenMedicines)
            hadTerms = false;

        if (!hadTerms && !takenMedicines)
            throw new ActionNotAllowedException("You cannot give mark to this pharmacy");

        Mark mark = markRepository.checkIfPatientHasGivenMarkToPharmacy(markDTO.getPharmacyId(), markDTO.getPatientId());
        if (mark == null) throw new ActionNotAllowedException("You didn't give mark to this pharmacy");

        int markUpdated = markRepository.updatePharmacysMark(markDTO.getMarkValue(), markDTO.getPharmacyId(), markDTO.getPatientId());
        if (markUpdated != 1) throw new RuntimeException("Unable to give mark.");

        return true;
    }

    @Override
    @Transactional(rollbackFor = {ActionNotAllowedException.class, RuntimeException.class})
    public boolean updateMedicinesMark(MarkDTO markDTO) throws ActionNotAllowedException, RuntimeException {
        Medicine medicineReserved = medicineRepository.checkIfPatientReservedMedicine(markDTO.getMedicineId(), markDTO.getPatientId());
        Medicine medicinePrescribed = medicineRepository.checkIfPatientGotPrescribedMedicine(markDTO.getMedicineId(), markDTO.getPatientId());
        if (medicineReserved == null && medicinePrescribed == null)
            throw new ActionNotAllowedException("You have not taken this medicine");

        Mark mark = markRepository.checkIfPatientHasGivenMarkToMedicine(markDTO.getMedicineId(), markDTO.getPatientId());
        if (mark == null) throw new ActionNotAllowedException("You didn't give mark to this medicine");

        int markUpdated = markRepository.updateMedicinesMark(markDTO.getMarkValue(), markDTO.getMedicineId(), markDTO.getPatientId());
        if (markUpdated != 1) throw new RuntimeException("Unable to give mark.");

        return true;
    }

}
