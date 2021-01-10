package team18.pharmacyapp.model.medicine;

import team18.pharmacyapp.model.Report;
import team18.pharmacyapp.model.keys.ReportMedicineId;

import javax.persistence.*;
import java.util.Date;

@Entity
@IdClass(ReportMedicineId.class)
public class ReportMedicines {
    @Id
    @ManyToOne
    private Report report;
    @Id
    @ManyToOne
    private Medicine medicine;
    private int medicineQuantity;
    private Date startDate;
    private Date endDate;

    public ReportMedicines() {
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }

    public int getMedicineQuantity() {
        return medicineQuantity;
    }

    public void setMedicineQuantity(int medicineQuantity) {
        this.medicineQuantity = medicineQuantity;
    }
}
