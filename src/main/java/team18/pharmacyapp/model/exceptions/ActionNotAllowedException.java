package team18.pharmacyapp.model.exceptions;

public class ActionNotAllowedException extends Exception {
    public ActionNotAllowedException(String errorMessage) {
        super(errorMessage);
    }
}
