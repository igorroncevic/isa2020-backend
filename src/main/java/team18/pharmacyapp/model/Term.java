package team18.pharmacyapp.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import team18.pharmacyapp.model.enums.TermType;
import team18.pharmacyapp.model.users.Doctor;
import team18.pharmacyapp.model.users.Patient;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity(name = "term")
@Getter
@Setter
@NoArgsConstructor
public class Term {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID", strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(nullable = true)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Doctor doctor;

    @Column(nullable = false)
    private Date startTime;

    @Column(nullable = false)
    private Date endTime;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private TermType type;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(nullable = true)
    private Report report;

    @Column(nullable = false)
    private int loyaltyPoints;

}
