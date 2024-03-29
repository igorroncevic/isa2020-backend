package team18.pharmacyapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Term {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID", strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private Patient patient;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private Doctor doctor;

    @Column(nullable = false)
    private Date startTime;

    @Column(nullable = false)
    private Date endTime;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TermType type;

    @OneToOne(cascade = CascadeType.ALL)
    private Report report;

    @Column(nullable = false)
    private boolean completed = false;

    @Version
    private Long version = 0L;
}
