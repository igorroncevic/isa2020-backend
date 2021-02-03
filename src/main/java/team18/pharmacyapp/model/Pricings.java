package team18.pharmacyapp.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import team18.pharmacyapp.model.medicine.PharmacyMedicines;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity(name = "pricings")
@Getter
@Setter
@NoArgsConstructor
public class Pricings {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID", strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumns({
            @JoinColumn(nullable = false, referencedColumnName = "medicine_id"),
            @JoinColumn(nullable = false, referencedColumnName = "pharmacy_id"),
    })
    private PharmacyMedicines pharmacyMedicine;

    @Column(nullable = false)
    private Date startDate;

    @Column(nullable = false)
    private Date endDate;

    @Column(nullable = false)
    private double price;
}
