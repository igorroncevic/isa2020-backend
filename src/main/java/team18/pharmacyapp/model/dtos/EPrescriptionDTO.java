package team18.pharmacyapp.model.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team18.pharmacyapp.model.enums.EPrescriptionStatus;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class EPrescriptionDTO {
    private UUID id;
    private Date issueDate;
    private List<EPrescriptionMedicinesDTO> ePrescriptionMedicines;
    private EPrescriptionStatus status;
}
