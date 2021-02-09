package team18.pharmacyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team18.pharmacyapp.model.Promotion;
import team18.pharmacyapp.model.dtos.NewPromotionDTO;
import team18.pharmacyapp.model.exceptions.BadTimeRangeException;
import team18.pharmacyapp.repository.PromotionRepository;
import team18.pharmacyapp.service.interfaces.EmailService;
import team18.pharmacyapp.service.interfaces.PromotionService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepository promotionRepository;
    private final EmailService emailService;

    @Autowired
    public PromotionServiceImpl(PromotionRepository promotionRepository, EmailService emailService) {
        this.promotionRepository = promotionRepository;
        this.emailService = emailService;
    }

    @Override
    public List<Promotion> getAllPromotionsForPharmacy(UUID id) {
        return promotionRepository.getAllForPharmacy(id);
    }

    @Override
    public Promotion addNewPromotion(UUID pharmacyId, NewPromotionDTO newPromotionDTO) throws BadTimeRangeException {
        Date today = new Date();
        if(newPromotionDTO.getStartDate().before(today) || newPromotionDTO.getEndDate().before(today))
            throw new BadTimeRangeException("You can't define promotion for past time");

        UUID promotionId = UUID.randomUUID();

        int rowsAdded = promotionRepository.insert(promotionId, newPromotionDTO.getStartDate(), newPromotionDTO.getEndDate(),
                newPromotionDTO.getText(), pharmacyId);
        if(rowsAdded != 1)
            throw new InternalError("Failed to create promotion.");

        List<String> subscribedPatientsMails = promotionRepository.getEmailsOfSubscribedPatients(pharmacyId);
        String subject = "[ISA Pharmacy] New promotion!";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String body = "There is a new promotion in pharmacy you have been subsribed to.\nIt lasts from " + sdf.format(newPromotionDTO.getStartDate()) +
        " to " + sdf.format(newPromotionDTO.getEndDate()) + ".\nText: " + newPromotionDTO.getText();
        new Thread(() -> sendEmails(subscribedPatientsMails, subject, body)).start();

        Promotion promotion = promotionRepository.findById(promotionId).orElse(null);
        return promotion;
    }

    private void sendEmails(List<String> emails, String subject, String body) {
        for(String email : emails)
            emailService.sendMail(email, subject, body);
    }

}
