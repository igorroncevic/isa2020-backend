package team18.pharmacyapp.model.users;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team18.pharmacyapp.model.*;
import team18.pharmacyapp.model.enums.DoctorRole;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Doctor extends User implements Serializable {

    @Column(nullable = false)
    private DoctorRole doctorRole;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "doctor")
    private List<Vacation> vacations;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    private List<Term> terms;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    private List<Complaint> complaints;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    private List<Mark> marks;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    private List<WorkSchedule> workSchedules;

}
