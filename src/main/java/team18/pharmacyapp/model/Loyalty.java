package team18.pharmacyapp.model;

import org.hibernate.annotations.GenericGenerator;
import team18.pharmacyapp.model.users.Patient;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.UUID;

@Entity
public class Loyalty {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    private String category;
    private int minPoints;
    private int maxPoints;
    private double discount;

    @OneToMany(mappedBy = "loyalty")
    private List<Patient> patients;


}
