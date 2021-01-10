package team18.pharmacyapp.model;

import com.sun.istack.Nullable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import team18.pharmacyapp.model.users.Doctor;
import team18.pharmacyapp.model.users.Patient;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Complaint {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(nullable = false)
    private Patient patient;

    @ManyToOne(cascade = CascadeType.ALL)
    private Pharmacy pharmacy;

    @ManyToOne(cascade = CascadeType.ALL)
    private Doctor doctor;

    @Column(nullable = false)
    private String complaintText;
    private String adminResponse;



}
