package team18.pharmacyapp.model.users;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team18.pharmacyapp.model.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity(name = "doctor")
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Doctor extends RegisteredUser implements Serializable {
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    private List<Vacation> vacations;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    private List<Term> terms;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    private List<Complaint> complaints;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    private List<Mark> marks;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    private List<WorkSchedule> workSchedules;

    public Doctor(UUID id, String name, String surname) {
        this.id=id;
        this.name=name;
        this.surname=surname;
    }
}
