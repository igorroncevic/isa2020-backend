package team18.pharmacyapp.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team18.pharmacyapp.model.Pharmacy;
import team18.pharmacyapp.model.medicine.Medicine;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EPrescriptionMedicinesDTO {
    private Pharmacy pharmacy;
    private Medicine medicine;
    private int quantity;
}
