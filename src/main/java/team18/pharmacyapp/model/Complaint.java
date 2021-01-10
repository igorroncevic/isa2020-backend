package team18.pharmacyapp.model;

import com.sun.istack.Nullable;
import org.hibernate.annotations.GenericGenerator;
import team18.pharmacyapp.model.users.Doctor;
import team18.pharmacyapp.model.users.Patient;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.UUID;

@Entity
public class Complaint {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @ManyToOne
    private Patient patient;

    @ManyToOne()
    private Pharmacy pharmacy;

    @ManyToOne
    private Doctor doctor;

    private String complaintText;
    private String adminResponse;



}
