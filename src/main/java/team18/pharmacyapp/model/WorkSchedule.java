package team18.pharmacyapp.model;

import team18.pharmacyapp.model.keys.WorkScheduleId;
import team18.pharmacyapp.model.users.Doctor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
@IdClass(WorkScheduleId.class)
public class WorkSchedule {
    @Id
    @ManyToOne
    private Doctor doctor;
    @Id
    @ManyToOne
    private Pharmacy pharmacy;

    private Date fromHour;
    private Date toHour;
}
