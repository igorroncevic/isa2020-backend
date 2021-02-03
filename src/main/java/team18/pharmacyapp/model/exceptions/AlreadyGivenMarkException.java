package team18.pharmacyapp.model.exceptions;

public class AlreadyGivenMarkException extends Exception{
    public AlreadyGivenMarkException(String errorMessage) {
        super(errorMessage);
    }
}
