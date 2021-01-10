package team18.pharmacyapp.model;

import org.hibernate.annotations.GenericGenerator;
import team18.pharmacyapp.model.medicine.Medicine;
import team18.pharmacyapp.model.users.Doctor;
import team18.pharmacyapp.model.users.Patient;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.UUID;

@Entity
public class Mark {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @ManyToOne
    private Doctor doctor;
    @ManyToOne
    private Pharmacy pharmacy;
    @ManyToOne
    private Medicine medicine;
    @ManyToOne
    private Patient patient;

    private int mark;


}
