package team18.pharmacyapp.model.medicine;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import team18.pharmacyapp.model.Mark;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity(name = "medicine")
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Medicine {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID", strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    private String name;

    @OneToMany(mappedBy = "medicine")
    @JsonIgnore
    private List<ReservedMedicines> reservedMedicines;

    @OneToMany(mappedBy = "medicine")
    @JsonIgnore
    private List<ReportMedicines> reportMedicines;

    @OneToMany(mappedBy = "medicine")
    @JsonIgnore
    private List<EPrescriptionMedicines> ePrescriptionMedicines;

    @OneToMany(mappedBy = "medicine")
    @JsonIgnore
    private List<SupplierMedicine> supplierMedicines;

    @OneToMany(mappedBy = "medicine")
    @JsonIgnore
    private List<PurchaseOrderMedicine> purchaseOrderMedicines;

    @OneToMany(mappedBy = "medicine")
    private List<Mark> marks;

    @OneToMany(mappedBy = "medicine")
    private List<PharmacyMedicines> pharmacyMedicines;

    @Column(nullable = false)
    private int loyaltyPoints;

}
