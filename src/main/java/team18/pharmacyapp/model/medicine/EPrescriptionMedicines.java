package team18.pharmacyapp.model.medicine;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team18.pharmacyapp.model.keys.EPrescriptionMedicinesId;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@IdClass(EPrescriptionMedicinesId.class)
public class EPrescriptionMedicines {
    @Id
    @ManyToOne
    private EPrescription ePrescription;
    @Id
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "eprescription_medicine_id", referencedColumnName = "medicine_id"),
            @JoinColumn(name = "eprescription_pharmacy_id", referencedColumnName = "pharmacy_id")
    })
    private PharmacyMedicines pharmacyMedicines;

    @Column(nullable = false)
    private int quantity;
}
