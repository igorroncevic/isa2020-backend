package team18.pharmacyapp.service.interfaces;

import team18.pharmacyapp.model.users.Authority;

import java.util.List;
import java.util.UUID;

public interface AuthorityService {
    List<Authority> findById(UUID id);

    List<Authority> findByName(String name);
}
