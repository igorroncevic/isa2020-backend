package team18.pharmacyapp.model.medicine;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team18.pharmacyapp.model.Pharmacy;
import team18.pharmacyapp.model.Pricings;
import team18.pharmacyapp.model.keys.PharmacyMedicinesId;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@IdClass(PharmacyMedicinesId.class)
public class PharmacyMedicines {
    @Id
    @ManyToOne
    private Pharmacy pharmacy;
    @Id
    @ManyToOne
    private Medicine medicine;

    @Column(nullable = false)
    private int quantity;

    @OneToMany(mappedBy = "pharmacyMedicine")
    private List<MedicineRequests> medicineRequests;

    @OneToMany(mappedBy = "pharmacyMedicine")
    private List<Pricings> pricings;
}
