package team18.pharmacyapp.model.medicine;

import team18.pharmacyapp.model.keys.EPrescriptionMedicinesId;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

@Entity
@IdClass(EPrescriptionMedicinesId.class)
public class EPrescriptionMedicines {
    @Id
    @ManyToOne
    private EPrescription ePrescription;
    @Id
    @ManyToOne
    private Medicine medicine;
    private int quantity;

    public EPrescriptionMedicines() {
    }

    public EPrescription getEPrescription() {
        return ePrescription;
    }

    public void setEPrescription(EPrescription ePrescription) {
        this.ePrescription = ePrescription;
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
