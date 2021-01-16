package team18.pharmacyapp.model.users;

import com.fasterxml.jackson.annotation.JsonInclude;
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
    @Enumerated(EnumType.STRING)
    private DoctorRole doctorRole;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "doctor")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Vacation> vacations;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Term> terms;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Complaint> complaints;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Mark> marks;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<WorkSchedule> workSchedules;
}
