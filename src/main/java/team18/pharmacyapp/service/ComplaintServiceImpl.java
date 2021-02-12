package team18.pharmacyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team18.pharmacyapp.model.Complaint;
import team18.pharmacyapp.model.dtos.ComplaintDTO;
import team18.pharmacyapp.repository.ComplaintRepository;
import team18.pharmacyapp.service.interfaces.ComplaintService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ComplaintServiceImpl implements ComplaintService {
    private final ComplaintRepository complaintRepository;
    private final EmailServiceImpl emailService;

    @Autowired
    public ComplaintServiceImpl(ComplaintRepository complaintRepository, EmailServiceImpl emailService) {
        this.complaintRepository = complaintRepository;
        this.emailService = emailService;
    }

    @Override
    public List<ComplaintDTO> getAllComplaints() {
        List<Complaint> complaints = complaintRepository.findAll();
        List<ComplaintDTO> allComplaintsDTO = new ArrayList<>();
        for(Complaint c : complaints){
            UUID pharmacyId=null;
            UUID doctorId=null;
            if(c.getPharmacy()!=null){
                pharmacyId=c.getPharmacy().getId();
            }
            if(c.getDoctor()!=null) {
                doctorId = c.getDoctor().getId();
            }

                ComplaintDTO   dto = new ComplaintDTO(c.getId(), c.getPatient().getId(), c.getComplaintText(), c.getAdminResponse(), doctorId, pharmacyId);
            allComplaintsDTO.add(dto);
        }
        return allComplaintsDTO;
    }

    @Override
    public boolean response(ComplaintDTO complaint) {
        Complaint c = complaintRepository.findById(complaint.getComplaintId()).orElse(null);
        if(c!=null){
            c.setAdminResponse(complaint.getComplaintResponse());
            complaintRepository.save(c);
            String subject = "[ISA Pharmacy] Complaint response ";
            String body = "Admin responsed to you complaint ("+ c.getId()+")\n"+
                    "Response text:" + complaint.getComplaintResponse();
            new Thread(() -> emailService.sendMail(c.getPatient().getEmail(), subject, body)).start();
            return true;
        }
        return false;
    }

    @Override
    public int saveComplaint(ComplaintDTO dto,String type){
        UUID id=UUID.randomUUID();
        int res=0;
        if(type.equals("doctor")){
            res=complaintRepository.saveComplaintDoctor(id,dto.getPatientId(),dto.getComplaintText(),dto.getDoctorId());
        }
        if(type.equals("pharmacy")){
              res =complaintRepository.saveComplaintPharmacy(id,dto.getPatientId(),dto.getComplaintText(),dto.getPharmacyId());
        }
        return res;
    }
}
