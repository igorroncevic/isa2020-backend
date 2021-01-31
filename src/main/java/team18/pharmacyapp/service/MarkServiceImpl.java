package team18.pharmacyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team18.pharmacyapp.model.Mark;
import team18.pharmacyapp.model.Pharmacy;
import team18.pharmacyapp.model.dtos.MarkDTO;
import team18.pharmacyapp.model.exceptions.ActionNotAllowedException;
import team18.pharmacyapp.model.exceptions.AlreadyGivenMarkException;
import team18.pharmacyapp.model.medicine.Medicine;
import team18.pharmacyapp.model.users.Doctor;
import team18.pharmacyapp.repository.DoctorRepository;
import team18.pharmacyapp.repository.MarkRepository;
import team18.pharmacyapp.repository.MedicineRepository;
import team18.pharmacyapp.repository.PharmacyRepository;
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

    @Autowired
    public MarkServiceImpl(MarkRepository markRepository, DoctorRepository doctorRepository, PharmacyRepository pharmacyRepository, MedicineRepository medicineRepository) {
        this.markRepository = markRepository;
        this.doctorRepository = doctorRepository;
        this.pharmacyRepository = pharmacyRepository;
        this.medicineRepository = medicineRepository;
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
        List<Medicine> pharmacyMedicines = medicineRepository.getAllMedicinesFromPharmacy(markDTO.getPharmacyId());
        boolean takenMedicineFlag = false;
        for(Medicine m : pharmacyMedicines){
            Pharmacy pharmacy = pharmacyRepository.checkIfPatientTakenMedicineFromPharmacy(markDTO.getPharmacyId(), markDTO.getPatientId(), m.getId());
            if(pharmacy != null){
                takenMedicineFlag = true;
                break;
            }
        }
        if(!takenMedicineFlag) throw new ActionNotAllowedException("You have not taken any medicines from this pharmacy");

        List<Doctor> pharmacyDoctors = doctorRepository.findAllDoctorsInPharmacy(markDTO.getPharmacyId());
        boolean hadAppointmentFlag = false;
        for(Doctor d : pharmacyDoctors){
            Pharmacy pharmacy = pharmacyRepository.checkIfPatientHadAppointmentInPharmacy(markDTO.getPharmacyId(), markDTO.getPatientId(), d.getId());
            if(pharmacy != null){
                hadAppointmentFlag = true;
                break;
            }
        }
        if(!hadAppointmentFlag) throw new ActionNotAllowedException("You have not had any appointments in this pharmacy");

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
