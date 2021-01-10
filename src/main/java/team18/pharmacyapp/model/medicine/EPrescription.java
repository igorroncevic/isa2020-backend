package team18.pharmacyapp.model.medicine;

import org.hibernate.annotations.GenericGenerator;
import team18.pharmacyapp.model.users.Patient;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
public class EPrescription {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;
    @ManyToOne
    private Patient patient;
    private Date issueDate;

    @OneToMany(mappedBy = "ePrescription")
    private List<EPrescriptionMedicines> ePrescriptionMedicines;

    public EPrescription() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public List<EPrescriptionMedicines> getePresciptionMedicines() {
        return ePrescriptionMedicines;
    }

    public void setePresciptionMedicines(List<EPrescriptionMedicines> ePrescriptionMedicines) {
        this.ePrescriptionMedicines = ePrescriptionMedicines;
    }
}
