package team18.pharmacyapp.model.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team18.pharmacyapp.model.Pharmacy;
import team18.pharmacyapp.model.medicine.Medicine;

@Getter
@Setter
@NoArgsConstructor
public class PharmacyMedicinesDTO {
    Pharmacy pharmacy;
    Medicine medicine;
    int quantity;
    double price;
}