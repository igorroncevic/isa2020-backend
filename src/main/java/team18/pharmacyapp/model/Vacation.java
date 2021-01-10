package team18.pharmacyapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import team18.pharmacyapp.model.users.Doctor;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Vacation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Date startDate;
    private Date endDate;
    @ManyToOne
    @JsonIgnore
    private Doctor doctor;

    private boolean approved;
    private String rejectionReason;

    public Vacation() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
}
