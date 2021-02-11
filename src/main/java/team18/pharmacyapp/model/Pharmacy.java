package team18.pharmacyapp.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
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

@Entity(name = "pharmacy")
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

    public Pharmacy(UUID id,String name,Address address){
        this.id=id;
        this.name=name;
        this.address=address;
    }

    @ManyToOne()
    @JoinColumn(nullable = false)
    private Address address;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Promotion> promotions;

    @OneToMany(mappedBy = "pharmacy")
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

    @Override
    public boolean equals(Object obj) {
        Pharmacy pharmacy = (Pharmacy) obj;
        return this.id.equals(pharmacy.id);
    }
}
