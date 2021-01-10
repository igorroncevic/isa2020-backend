package team18.pharmacyapp.model.medicine;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team18.pharmacyapp.model.Report;
import team18.pharmacyapp.model.keys.ReportMedicineId;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@IdClass(ReportMedicineId.class)
public class ReportMedicines {
    @Id
    @ManyToOne
    private Report report;
    @Id
    @ManyToOne
    private Medicine medicine;

    @Column(nullable = false)
    private int medicineQuantity;

    @Column(nullable = false)
    private Date startDate;

    @Column(nullable = false)
    private Date endDate;


}
