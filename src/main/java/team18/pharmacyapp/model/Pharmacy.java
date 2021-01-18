package team18.pharmacyapp.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import team18.pharmacyapp.model.medicine.PharmacyMedicines;
import team18.pharmacyapp.model.users.Patient;
import team18.pharmacyapp.model.users.PharmacyAdmin;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
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
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Promotion> promotions;

    @ManyToMany(cascade = CascadeType.ALL)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Patient> subscribedPatients;

    @OneToMany(mappedBy = "pharmacy", cascade = CascadeType.ALL)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Complaint> complaints;

    @OneToMany(mappedBy = "pharmacy", cascade = CascadeType.ALL)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<PharmacyAdmin> admins;

    @OneToMany(mappedBy = "pharmacy", cascade = CascadeType.ALL)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Mark> marks;

    @OneToMany(mappedBy = "pharmacy", cascade = CascadeType.ALL)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<WorkSchedule> workSchedules;

    @OneToMany(mappedBy = "pharmacy", cascade = CascadeType.ALL)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<PharmacyMedicines> pharmacyMedicines;
}
