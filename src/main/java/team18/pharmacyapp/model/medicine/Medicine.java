package team18.pharmacyapp.model.medicine;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import team18.pharmacyapp.model.Mark;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
public class Medicine {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;



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

    private int loyaltyPoints;

    public Medicine() {
    }

    public List<SupplierMedicine> getSupplierMedicines() {
        return supplierMedicines;
    }

    public void setSupplierMedicines(List<SupplierMedicine> supplierMedicines) {
        this.supplierMedicines = supplierMedicines;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<ReportMedicines> getReportMedicines() {
        return reportMedicines;
    }

    public void setReportMedicines(List<ReportMedicines> reportMedicines) {
        this.reportMedicines = reportMedicines;
    }

    public List<EPrescriptionMedicines> getePrescriptionMedicines() {
        return ePrescriptionMedicines;
    }

    public void setePrescriptionMedicines(List<EPrescriptionMedicines> ePrescriptionMedicines) {
        this.ePrescriptionMedicines = ePrescriptionMedicines;
    }




    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }
}
