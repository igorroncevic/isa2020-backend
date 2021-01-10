package team18.pharmacyapp.model;

import org.hibernate.annotations.GenericGenerator;
import team18.pharmacyapp.model.medicine.ReportMedicines;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
public class Report {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @OneToMany(mappedBy = "report")
    private List<ReportMedicines> reportMedicines;

    private String text;

    @OneToOne()
    private Term term;

    public Report() {
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }
}
