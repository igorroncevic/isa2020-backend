package team18.pharmacyapp.model.keys;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class EPrescriptionMedicinesId implements Serializable {
    UUID ePrescription;
    PharmacyMedicinesId pharmacyMedicines;
}
