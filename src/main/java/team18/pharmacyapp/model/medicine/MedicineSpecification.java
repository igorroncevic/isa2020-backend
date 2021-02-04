package team18.pharmacyapp.model.medicine;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import team18.pharmacyapp.model.enums.MedicineIssuingRegime;

import javax.persistence.*;
import java.util.UUID;

@Table(uniqueConstraints={@UniqueConstraint(columnNames = {"medicine_id"})})
@Entity(name = "medicine_specification")
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MedicineSpecification {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID", strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @OneToOne()
    private Medicine medicine;

    private String replacementMedicineCode;

    private int recommendedDose;

    private String contraindications;

    private String drugComposition;

    private String additionalNotes;


}
