package team18.pharmacyapp.service.interfaces;

import team18.pharmacyapp.model.dtos.ComplaintDTO;

import java.util.List;


public interface ComplaintService {

    List<ComplaintDTO> getAllComplaints();

    boolean response(ComplaintDTO complaint);

    int saveComplaint(ComplaintDTO dto,String type);
}
