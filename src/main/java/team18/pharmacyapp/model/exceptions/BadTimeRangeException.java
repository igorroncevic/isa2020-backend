package team18.pharmacyapp.model.exceptions;

public class BadTimeRangeException extends Exception {
    public BadTimeRangeException(String errorMessage) {
        super(errorMessage);
    }
}
