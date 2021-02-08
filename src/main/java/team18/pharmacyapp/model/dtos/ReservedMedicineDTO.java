package team18.pharmacyapp.model.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team18.pharmacyapp.model.Pharmacy;
import team18.pharmacyapp.model.medicine.Medicine;

import java.time.LocalDate;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class ReservedMedicineDTO implements Serializable {
    private Medicine medicine;
    private Pharmacy pharmacy;
    private LocalDate pickupDate;
    private double price;

    public ReservedMedicineDTO(Medicine medicine, Pharmacy pharmacy, Date pickupDate, double price) {
        this.medicine = medicine;
        this.pharmacy = pharmacy;
        this.pickupDate = pickupDate;
        this.price = price;
    }
}
