package team18.pharmacyapp.model.users;

import team18.pharmacyapp.model.*;
import team18.pharmacyapp.model.medicine.EPrescription;
import team18.pharmacyapp.model.medicine.Medicine;

import javax.persistence.*;
import java.util.List;

@Entity
public class Patient extends User {

    @ManyToMany
    @JoinTable(name = "alergicto",joinColumns = @JoinColumn(name = "patientId",referencedColumnName = "id"),inverseJoinColumns = @JoinColumn(name = "medicineId",referencedColumnName = "id"))
    private List<Medicine> alergicMedicines;
    @ManyToMany
    @JoinTable(name = "reservedMedicines")
    private List<Medicine> reservedMedicines;
    private int penalties;
    @OneToMany
    private List<EPrescription> prescriptions;
    @ManyToMany(mappedBy = "subscribedPatients")
    private List<Pharmacy> subscribedPharmacies;
    @OneToMany(mappedBy = "patient")
    private  List<Complaint> complaints;
    @ManyToOne
    private Loyalty loyalty;

    private  int loyaltyPoints;


}
