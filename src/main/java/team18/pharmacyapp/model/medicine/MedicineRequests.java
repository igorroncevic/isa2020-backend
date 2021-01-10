package team18.pharmacyapp.model.medicine;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class MedicineRequests {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(unique = true,nullable = false,referencedColumnName = "medicine_id"),
            @JoinColumn(unique = true,nullable = false,referencedColumnName = "pharmacy_id"),
    })
    private PharmacyMedicines pharmacyMedicine;

    private int timesRequested=0;

}
