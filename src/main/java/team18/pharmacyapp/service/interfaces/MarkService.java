package team18.pharmacyapp.service.interfaces;

import team18.pharmacyapp.model.dtos.MarkDTO;
import team18.pharmacyapp.model.exceptions.ActionNotAllowedException;
import team18.pharmacyapp.model.exceptions.AlreadyGivenMarkException;

public interface MarkService {
    boolean giveMark(MarkDTO markDTO) throws ActionNotAllowedException, AlreadyGivenMarkException;

    boolean giveMarkToDoctor(MarkDTO markDTO) throws ActionNotAllowedException, AlreadyGivenMarkException;
    boolean giveMarkToPharmacy(MarkDTO markDTO) throws ActionNotAllowedException, AlreadyGivenMarkException;
    boolean giveMarkToMedicine(MarkDTO markDTO) throws ActionNotAllowedException, AlreadyGivenMarkException;
}
