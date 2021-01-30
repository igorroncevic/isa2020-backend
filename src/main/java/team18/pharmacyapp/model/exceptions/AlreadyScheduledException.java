package team18.pharmacyapp.model.exceptions;

public class AlreadyScheduledException extends Exception {
    public AlreadyScheduledException(String errorMessage) {
        super(errorMessage);
    }
}
