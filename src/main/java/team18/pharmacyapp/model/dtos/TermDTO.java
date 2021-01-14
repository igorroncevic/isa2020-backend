package team18.pharmacyapp.model.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team18.pharmacyapp.model.Term;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class TermDTO {
    private UUID id;
    private UUID patient_id;

    public TermDTO(Term term){
        this(term.getId(), term.getPatient().getId());
    }

    public TermDTO(UUID id, UUID patient_id){
        this.id = id;
        this.patient_id = patient_id;
    }
}
