package team18.pharmacyapp.model;

import org.hibernate.annotations.GenericGenerator;
import team18.pharmacyapp.model.enums.TermType;
import team18.pharmacyapp.model.users.Doctor;
import team18.pharmacyapp.model.users.Patient;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity(name = "term")
public class Term {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @ManyToOne
    private Patient patient;
    @ManyToOne
    private Doctor doctor;
    private Date startTime;
    private Date endTime;
    private double price;
    private TermType type;
    @OneToOne(cascade = CascadeType.ALL)
    private Report report;
    private int loyaltyPoints;


    public Term() {
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
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

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public TermType getType() {
        return type;
    }

    public void setType(TermType type) {
        this.type = type;
    }
}
