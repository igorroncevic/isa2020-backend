package team18.pharmacyapp.model;

import org.hibernate.annotations.GenericGenerator;
import team18.pharmacyapp.model.medicine.PharmacyMedicines;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
public class Pricings {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @ManyToOne
    private PharmacyMedicines pharmacyMedicine;
    private Date startDate;
    private Date endDate;
    private double price;
}
