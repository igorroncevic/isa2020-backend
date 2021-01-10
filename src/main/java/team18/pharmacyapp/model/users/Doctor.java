package team18.pharmacyapp.model.users;

import team18.pharmacyapp.model.*;
import team18.pharmacyapp.model.enums.DoctorRole;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class Doctor extends User implements Serializable {

    private DoctorRole doctorRole;
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "doctor")
    private List<Vacation> vacations;

    @OneToMany(mappedBy = "doctor")
    private List<Term> terms;

    @OneToMany(mappedBy = "doctor")
    private List<Complaint> complaints;

    @OneToMany(mappedBy = "doctor")
    private List<Mark> marks;

    @OneToMany(mappedBy = "doctor")
    private List<WorkSchedule> workSchedules;

    public List<Vacation> getVacations() {
        return vacations;
    }

    public void setVacations(List<Vacation> vacations) {
        this.vacations = vacations;
    }

    public DoctorRole getDoctorRole() {
        return doctorRole;
    }

    public void setDoctorRole(DoctorRole role) {
        this.doctorRole = role;
    }
}
