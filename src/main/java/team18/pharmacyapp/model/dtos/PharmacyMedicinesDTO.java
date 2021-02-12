package team18.pharmacyapp.model.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PharmacyMedicinesDTO {
    PharmacyDTO pharmacy;
    MedicineDTO medicine;
    int quantity;
    double price;
}
