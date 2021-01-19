package team18.pharmacyapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import team18.pharmacyapp.model.medicine.PharmacyMedicines;
import team18.pharmacyapp.model.medicine.ReservedMedicines;
import team18.pharmacyapp.model.users.Patient;
import team18.pharmacyapp.model.users.PharmacyAdmin;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Pharmacy {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID", strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(nullable = false)
    private Address address;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Promotion> promotions;

    @OneToMany(mappedBy = "pharmacy")
    @JsonIgnore
    private List<ReservedMedicines> reservedMedicines;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Patient> subscribedPatients;

    @OneToMany(mappedBy = "pharmacy", cascade = CascadeType.ALL)
    private List<Complaint> complaints;

    @OneToMany(mappedBy = "pharmacy", cascade = CascadeType.ALL)
    private List<PharmacyAdmin> admins;

    @OneToMany(mappedBy = "pharmacy", cascade = CascadeType.ALL)
    private List<Mark> marks;

    @OneToMany(mappedBy = "pharmacy", cascade = CascadeType.ALL)
    private List<WorkSchedule> workSchedules;

    @OneToMany(mappedBy = "pharmacy", cascade = CascadeType.ALL)
    private List<PharmacyMedicines> pharmacyMedicines;
}
