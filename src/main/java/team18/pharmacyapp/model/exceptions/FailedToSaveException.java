package team18.pharmacyapp.model.exceptions;

public class FailedToSaveException extends Exception {
    public FailedToSaveException(String errorMessage) {
        super(errorMessage);
    }
}
