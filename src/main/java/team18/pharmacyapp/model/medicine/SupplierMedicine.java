package team18.pharmacyapp.model.medicine;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team18.pharmacyapp.model.keys.SupplierMedicineId;
import team18.pharmacyapp.model.medicine.Medicine;
import team18.pharmacyapp.model.users.Supplier;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@IdClass(SupplierMedicineId.class)
public class SupplierMedicine {
    @Id
    @ManyToOne
    private Supplier supplier;

    @Id
    @ManyToOne
    private Medicine medicine;

    @Column(nullable = false)
    private int quantity;

}
