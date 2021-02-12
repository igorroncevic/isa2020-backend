package team18.pharmacyapp.model.medicine;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @ManyToOne(fetch = FetchType.LAZY)
    private Report report;
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    private Medicine medicine;

    @Column(nullable = false)
    private int medicineQuantity;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date startDate;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date endDate;


}
