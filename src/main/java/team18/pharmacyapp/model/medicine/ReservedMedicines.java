package team18.pharmacyapp.model.medicine;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import team18.pharmacyapp.model.Pharmacy;
import team18.pharmacyapp.model.users.Patient;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity(name = "reserved_medicines")
@Getter
@Setter
@NoArgsConstructor
public class ReservedMedicines {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID", strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Medicine medicine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Pharmacy pharmacy;

    private Date pickupDate; // Datum do kog ce pacijent preuzeti lijek

    private boolean handled; // ako je pacijent pokupio lijek ili je dobio penal, bice true

    @Version
    private Long version = 0L;
}
