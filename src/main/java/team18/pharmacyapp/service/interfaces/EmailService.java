package team18.pharmacyapp.service.interfaces;

public interface EmailService {
    void sendMail(String to, String subject, String body);
}
