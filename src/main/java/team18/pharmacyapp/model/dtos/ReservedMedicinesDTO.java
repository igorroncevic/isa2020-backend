package team18.pharmacyapp.model.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservedMedicinesDTO {
    private UUID id;
    private MedicineDTO medicine;
    private PharmacyDTO pharmacy;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date pickupDate;
    private boolean handled;
}
