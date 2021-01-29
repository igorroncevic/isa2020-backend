package team18.pharmacyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import team18.pharmacyapp.model.Pharmacy;
import team18.pharmacyapp.model.dtos.PharmacyFilteringDTO;

import java.util.List;
import java.util.UUID;

public interface PharmacyRepository extends JpaRepository<Pharmacy, UUID> {

    @Transactional(readOnly = true)
    @Query("SELECT COALESCE(AVG(m.mark), 0.0) FROM mark m WHERE m.pharmacy.id = :id")
    Float getAverageMark(@Param("id") UUID id);

    @Transactional(readOnly = true)
    @Query("SELECT new team18.pharmacyapp.model.dtos.PharmacyFilteringDTO(p.id, p.name, p.address, AVG(m.mark)) FROM pharmacy p " +
                "JOIN p.address a " +
                "JOIN p.marks m WHERE " +
                "(:name IS NULL OR UPPER(p.name) LIKE CONCAT('%', UPPER(COALESCE(:name, 'x')), '%')) AND " +
                "(:city IS NULL OR UPPER(a.city) LIKE CONCAT('%', UPPER(COALESCE(:city, 'x')), '%')) AND " +
                "(:mark = 0.0 OR :mark >= (SELECT AVG(m.mark) FROM mark m INNER JOIN pharmacy p2 ON m.pharmacy.id = p2.id WHERE p2.id = p.id)) " +
                "GROUP BY p.id, p.name, p.address")
    List<PharmacyFilteringDTO> filterAll(@Param("name") String name, @Param("mark") Double mark, @Param("city") String city);
}
