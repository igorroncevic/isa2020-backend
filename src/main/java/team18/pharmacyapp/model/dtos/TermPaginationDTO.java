package team18.pharmacyapp.model.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team18.pharmacyapp.model.Term;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TermPaginationDTO {
    List<Term> terms;
    int totalPages;
}
