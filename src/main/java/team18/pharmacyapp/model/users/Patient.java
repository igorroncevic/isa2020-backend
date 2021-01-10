package team18.pharmacyapp.model.users;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team18.pharmacyapp.model.*;
import team18.pharmacyapp.model.medicine.EPrescription;
import team18.pharmacyapp.model.medicine.Medicine;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Patient extends User {

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "alergicto",joinColumns = @JoinColumn(name = "patientId",referencedColumnName = "id"),inverseJoinColumns = @JoinColumn(name = "medicineId",referencedColumnName = "id"))
    private List<Medicine> alergicMedicines;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "reservedMedicines")
    private List<Medicine> reservedMedicines;

    @Column(nullable = false)
    private int penalties;

    @OneToMany(cascade = CascadeType.ALL)
    private List<EPrescription> prescriptions;

    @ManyToMany(mappedBy = "subscribedPatients",cascade = CascadeType.ALL)
    private List<Pharmacy> subscribedPharmacies;

    @OneToMany(mappedBy = "patient",cascade = CascadeType.ALL)
    private  List<Complaint> complaints;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(nullable = false)
    private Loyalty loyalty;

    @Column(nullable = false)
    private  int loyaltyPoints;


}
