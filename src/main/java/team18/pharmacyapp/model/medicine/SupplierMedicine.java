package team18.pharmacyapp.model.medicine;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team18.pharmacyapp.model.keys.SupplierMedicineId;
import team18.pharmacyapp.model.users.Supplier;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@IdClass(SupplierMedicineId.class)
public class SupplierMedicine {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    private Supplier supplier;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    private Medicine medicine;

    @Column(nullable = false)
    private int quantity;

}
