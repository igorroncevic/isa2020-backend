package team18.pharmacyapp.model.medicine;

import team18.pharmacyapp.model.Pharmacy;
import team18.pharmacyapp.model.Pricings;
import team18.pharmacyapp.model.keys.PharmacyMedicinesId;

import javax.persistence.*;
import java.util.List;

@Entity
@IdClass(PharmacyMedicinesId.class)
public class PharmacyMedicines {
    @Id
    @ManyToOne
    private Pharmacy pharmacy;
    @Id
    @ManyToOne
    private Medicine medicine;
    private int quantity;

    @OneToMany(mappedBy = "pharmacyMedicine")
    private List<MedicineRequests> medicineRequests;

    @OneToMany(mappedBy = "pharmacyMedicine")
    private List<Pricings> pricings;
}
