package team18.pharmacyapp.model;

import org.hibernate.annotations.GenericGenerator;
import team18.pharmacyapp.model.medicine.PharmacyMedicines;
import team18.pharmacyapp.model.users.Patient;
import team18.pharmacyapp.model.users.PharmacyAdmin;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;
@Entity
public class Pharmacy {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @ManyToOne
    private  Address address;

    @OneToMany
    private List<Promotion> promotions;

    @ManyToMany
    private List<Patient> subscribedPatients;

    @OneToMany(mappedBy = "pharmacy")
    private List<Complaint> complaints;

    @OneToMany(mappedBy = "pharmacy")
    private List<PharmacyAdmin> admins;

    @OneToMany(mappedBy = "pharmacy")
    private List<Mark> marks;

    @OneToMany(mappedBy = "pharmacy")
    private List<WorkSchedule> workSchedules;

    @OneToMany(mappedBy = "pharmacy")
    private List<PharmacyMedicines> pharmacyMedicines;


}
