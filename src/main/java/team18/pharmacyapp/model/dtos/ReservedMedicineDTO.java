package team18.pharmacyapp.model.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team18.pharmacyapp.model.Pharmacy;
import team18.pharmacyapp.model.Pricings;
import team18.pharmacyapp.model.medicine.Medicine;
import team18.pharmacyapp.model.users.Patient;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ReservedMedicineDTO {
    @JsonIgnore
    private Patient patient;
    private Medicine medicine;
    @JsonIgnore
    private List<Pricings> pricings;
    private Pharmacy pharmacy;
    private Date pickupDate;
    private double price;
}
