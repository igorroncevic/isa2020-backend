package team18.pharmacyapp.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import team18.pharmacyapp.model.users.Patient;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Loyalty {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID", strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @Column(nullable = false, unique = true)
    private String category;
    @Column(nullable = false, unique = true)
    private int minPoints;
    @Column(nullable = false, unique = true)
    private int maxPoints;
    @Column(nullable = false, unique = true)
    private double discount;

    @OneToMany(mappedBy = "loyalty", cascade = CascadeType.ALL)
    private List<Patient> patients;


}
