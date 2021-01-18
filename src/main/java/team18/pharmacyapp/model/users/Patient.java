package team18.pharmacyapp.model.users;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team18.pharmacyapp.model.Complaint;
import team18.pharmacyapp.model.Loyalty;
import team18.pharmacyapp.model.Pharmacy;
import team18.pharmacyapp.model.medicine.EPrescription;
import team18.pharmacyapp.model.medicine.Medicine;
import team18.pharmacyapp.model.medicine.ReservedMedicines;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Patient extends User {

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "alergicto", joinColumns = @JoinColumn(name = "patientId", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "medicineId", referencedColumnName = "id"))
    private List<Medicine> alergicMedicines;

    @OneToMany(mappedBy = "patient")
    private List<ReservedMedicines> reservedMedicines;

    @Column(nullable = false)
    private int penalties;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<EPrescription> prescriptions;

    @ManyToMany(mappedBy = "subscribedPatients", cascade = CascadeType.ALL)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Pharmacy> subscribedPharmacies;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Complaint> complaints;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JoinColumn(nullable = false)
    private Loyalty loyalty;

    @Column(nullable = false)
    private int loyaltyPoints;
}
