package team18.pharmacyapp.model.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import team18.pharmacyapp.model.Address;
import team18.pharmacyapp.model.enums.UserRole;

import javax.persistence.*;
import java.util.UUID;

@MappedSuperclass
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
           name = "UUID",strategy = "org.hibernate.id.UUIDGenerator"
    )
    protected UUID id;
    protected String name;
    protected String surname;
    protected String email;
    protected String password;
    protected UserRole role;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name="fk_address",referencedColumnName ="id")
    protected Address address;

    public User() { }

    public User(String name, String surname, String email, String password, Address address) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = this.address;
    }
}
