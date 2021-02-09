package team18.pharmacyapp.model.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import team18.pharmacyapp.model.Address;
import team18.pharmacyapp.model.enums.UserRole;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public  class RegisteredUser implements UserDetails {
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_address", referencedColumnName = "id")
    protected Address address;

    @Column(name = "first_login")
    protected boolean firstLogin=true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_authority",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id"))
    private List<Authority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
