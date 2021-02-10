package team18.pharmacyapp.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team18.pharmacyapp.model.keys.WorkScheduleId;
import team18.pharmacyapp.model.users.Doctor;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "work_schedule")
@Getter
@Setter
@NoArgsConstructor
@IdClass(WorkScheduleId.class)
public class WorkSchedule {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    private Doctor doctor;
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    private Pharmacy pharmacy;

    @Column(nullable = false)
    private Date fromHour;

    @Column(nullable = false)
    private Date toHour;
}
