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
    private Medicine medicine;
    @Column(nullable = false)
    private int quantity;

}
