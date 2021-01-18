package team18.pharmacyapp.model.keys;

import java.io.Serializable;
import java.util.UUID;

public class ReservedMedicinesId implements Serializable {
    UUID medicine;
    UUID patient;
    UUID pharmacy;
}
