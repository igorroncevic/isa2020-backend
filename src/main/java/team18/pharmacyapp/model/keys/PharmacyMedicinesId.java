package team18.pharmacyapp.model.keys;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class PharmacyMedicinesId implements Serializable {
    UUID pharmacy;
    UUID medicine;
}
