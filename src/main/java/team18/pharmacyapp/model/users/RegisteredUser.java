package team18.pharmacyapp.model.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import team18.pharmacyapp.model.Address;
import team18.pharmacyapp.model.enums.UserRole;

import javax.persistence.*;
import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class RegisteredUser {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID", strategy = "org.hibernate.id.UUIDGenerator"
    )
    protected UUID id;

    @Column(nullable = false)
    protected String name;

    @Column(nullable = false)
    protected String surname;

    @Column(nullable = false, unique = true)
    protected String email;

    @Column(nullable = false)
    @JsonIgnore
    protected String password;

    @Column
    protected String phoneNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    protected UserRole role;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    @JoinColumn(name = "fk_address", referencedColumnName = "id")
    protected Address address;

}