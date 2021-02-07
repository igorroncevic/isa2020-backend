package team18.pharmacyapp.model.medicine;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import team18.pharmacyapp.model.Pharmacy;
import team18.pharmacyapp.model.Pricings;
import team18.pharmacyapp.model.keys.PharmacyMedicinesId;

import javax.persistence.*;
import java.util.List;

@Entity(name = "pharmacy_medicines")
@Getter
@Setter
@NoArgsConstructor
@IdClass(PharmacyMedicinesId.class)
public class PharmacyMedicines {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    private Pharmacy pharmacy;
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicine_id")
    private Medicine medicine;

    @Column(nullable = false)
    private int quantity;

    @OneToMany(mappedBy = "pharmacyMedicine")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    private List<MedicineRequests> medicineRequests;

    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    @OneToMany(mappedBy = "pharmacyMedicine")
    private List<Pricings> pricings;

    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    @OneToMany(mappedBy = "pharmacyMedicines")
    private List<EPrescriptionMedicines> ePrescriptionMedicines;

    // Verzija torke, koristi se u svrhu optimistickog zakljucavanja
    @Version
    private Long version;
}
