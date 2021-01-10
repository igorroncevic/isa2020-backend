package team18.pharmacyapp.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team18.pharmacyapp.model.keys.WorkScheduleId;
import team18.pharmacyapp.model.users.Doctor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@IdClass(WorkScheduleId.class)
public class WorkSchedule {
    @Id
    @ManyToOne
    private Doctor doctor;
    @Id
    @ManyToOne
    private Pharmacy pharmacy;

    @Column(nullable = false)
    private Date fromHour;

    @Column(nullable = false)
    private Date toHour;
}
