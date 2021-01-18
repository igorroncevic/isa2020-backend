package team18.pharmacyapp.model.medicine;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team18.pharmacyapp.model.keys.ReservedMedicinesId;
import team18.pharmacyapp.model.users.Patient;

import javax.persistence.*;
import java.util.Date;

@Entity(name="reserved_medicines")
@Getter
@Setter
@NoArgsConstructor
@IdClass(ReservedMedicinesId.class)
public class ReservedMedicines {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    private Patient patient;
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    private Medicine medicine;

    private Date pickupDate; // Datum do kog ce pacijent preuzeti lijek
}
