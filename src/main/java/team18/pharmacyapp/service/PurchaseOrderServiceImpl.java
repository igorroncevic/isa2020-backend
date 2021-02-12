package team18.pharmacyapp.service;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import team18.pharmacyapp.model.PurchaseOrder;
import team18.pharmacyapp.model.SupplierPurchaseOrder;
import team18.pharmacyapp.model.dtos.*;
import team18.pharmacyapp.model.exceptions.ActionNotAllowedException;
import team18.pharmacyapp.model.exceptions.FailedToSaveException;
import team18.pharmacyapp.model.medicine.PurchaseOrderMedicine;
import team18.pharmacyapp.model.users.Supplier;
import team18.pharmacyapp.repository.PharmacyMedicinesRepository;
import team18.pharmacyapp.repository.PurchaseOrderRepository;
import team18.pharmacyapp.service.interfaces.EmailService;
import team18.pharmacyapp.service.interfaces.PurchaseOrderService;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PharmacyMedicinesRepository pharmacyMedicinesRepository;
    private final EmailService emailService;

    @Autowired
    public PurchaseOrderServiceImpl(PurchaseOrderRepository purchaseOrderRepository, PharmacyMedicinesRepository pharmacyMedicinesRepository, EmailService emailService) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.pharmacyMedicinesRepository = pharmacyMedicinesRepository;
        this.emailService = emailService;
    }

    @Override
    public List<PurchaseOrderDTO> getPharmacyPurchaseOrders(UUID pharmacyId) {
        List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.getPharmacyPurchaseOrders(pharmacyId);
        List<PurchaseOrderDTO> purchaseOrderDTOs = new ArrayList<>();
        for(PurchaseOrder purchaseOrder : purchaseOrders) {
            UserInfoDTO pharmacyAdminInfoDTO = new UserInfoDTO(purchaseOrder.getPharmacyAdmin().getId(),
                    purchaseOrder.getPharmacyAdmin().getName(), purchaseOrder.getPharmacyAdmin().getSurname(),
                    purchaseOrder.getPharmacyAdmin().getEmail(), purchaseOrder.getPharmacyAdmin().getPhoneNumber());
            List<PurchaseOrderMedicine> purchaseOrderMedicines = purchaseOrderRepository.getPurchaseOrderMedicines(purchaseOrder.getId());
            List<PurchaseOrderMedicineDTO> purchaseOrderMedicineDTOs = new ArrayList<>();
            for(PurchaseOrderMedicine purchaseOrderMedicine : purchaseOrderMedicines) {
                PurchaseOrderMedicineDTO purchaseOrderMedicineDTO = new PurchaseOrderMedicineDTO(purchaseOrderMedicine.getMedicine().getId(),
                        purchaseOrderMedicine.getMedicine().getName(), purchaseOrderMedicine.getQuantity());
                purchaseOrderMedicineDTOs.add(purchaseOrderMedicineDTO);
            }

            int numberOfOffers = purchaseOrderRepository.getNumberOfOffersForOrder(purchaseOrder.getId());
            PurchaseOrderOfferDTO acceptedOffer = null;
            if(numberOfOffers > 0) {
                SupplierPurchaseOrder supplierPurchaseOrder = purchaseOrderRepository.getAcceptedOffer(purchaseOrder.getId()).orElse(null);
                if(supplierPurchaseOrder != null)
                    acceptedOffer = new PurchaseOrderOfferDTO(new UserInfoDTO(supplierPurchaseOrder.getSupplier().getId(), supplierPurchaseOrder.getSupplier().getName(),
                            supplierPurchaseOrder.getSupplier().getSurname(), supplierPurchaseOrder.getSupplier().getEmail(), supplierPurchaseOrder.getSupplier().getPhoneNumber()),
                            supplierPurchaseOrder.getDeliveryDate(), supplierPurchaseOrder.getPrice());
            }

            PurchaseOrderDTO purchaseOrderDTO = new PurchaseOrderDTO(purchaseOrder.getId(), pharmacyAdminInfoDTO,
                    purchaseOrder.getEndDate(), purchaseOrderMedicineDTOs, acceptedOffer, numberOfOffers);
            purchaseOrderDTOs.add(purchaseOrderDTO);
        }
        return purchaseOrderDTOs;
    }

    @Override
    public PurchaseOrderDTO addPurchaseOrder(NewPurchaseOrderDTO newPurchaseOrderDTO) throws FailedToSaveException {
        UUID orderId = UUID.randomUUID();
        int rowsChanged = purchaseOrderRepository.insertPurchaseOrder(orderId, newPurchaseOrderDTO.getEndDate(), newPurchaseOrderDTO.getPharmacyAdminId());
        if(rowsChanged != 1) {
            throw new FailedToSaveException("Could not save new purchase order.");
        }
        return getPurchaseOrderDTO(orderId, newPurchaseOrderDTO);
    }

    @Override
    public List<PurchaseOrderOfferDTO> getAllOffersForOrder(UUID id) {
        List<SupplierPurchaseOrder> supplierPurchaseOrders = purchaseOrderRepository.getAllOffersForOrder(id);
        List<PurchaseOrderOfferDTO> purchaseOrderOfferDTOs = new ArrayList<>();
        for(SupplierPurchaseOrder supplierPurchaseOrder : supplierPurchaseOrders) {
            UserInfoDTO userInfoDTO = new UserInfoDTO(supplierPurchaseOrder.getSupplier().getId(), supplierPurchaseOrder.getSupplier().getName(),
                    supplierPurchaseOrder.getSupplier().getSurname(), supplierPurchaseOrder.getSupplier().getEmail(), supplierPurchaseOrder.getSupplier().getPhoneNumber());
            PurchaseOrderOfferDTO purchaseOrderOfferDTO = new PurchaseOrderOfferDTO(userInfoDTO, supplierPurchaseOrder.getDeliveryDate(), supplierPurchaseOrder.getPrice());
            purchaseOrderOfferDTOs.add(purchaseOrderOfferDTO);
        }
        return purchaseOrderOfferDTOs;
    }

    @Override
    public void acceptOffer(UUID orderId, UUID supplierId, UUID phadminId) throws ActionNotAllowedException, FailedToSaveException {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.getPurchaseOrderById(orderId);
        if(LocalDate.now().isBefore(purchaseOrder.getEndDate())) {
            throw new ActionNotAllowedException("You can't accept offers for order that is still open.");
        } else if(!purchaseOrder.getPharmacyAdmin().getId().equals(phadminId)) {
            throw new ActionNotAllowedException("You can't accept offers created by other pharmacy admins.");
        }

        int rowsChanged = purchaseOrderRepository.acceptOffer(orderId, supplierId);
        if(rowsChanged != 1)
            throw new FailedToSaveException("Failed to accept offer");

        List<PurchaseOrderMedicine> purchaseOrderMedicines = purchaseOrderRepository.getPurchaseOrderMedicines(orderId);
        UUID pharmacyId = purchaseOrder.getPharmacyAdmin().getPharmacy().getId();

        for(PurchaseOrderMedicine purchaseOrderMedicine : purchaseOrderMedicines) {
            rowsChanged = purchaseOrderRepository.addQuantityToPharmacyMedicine(pharmacyId, purchaseOrderMedicine.getMedicine().getId(), purchaseOrderMedicine.getQuantity());
            if(rowsChanged != 1)
                throw new FailedToSaveException("Failed to increase pharmacy medicine quantity");
            rowsChanged = purchaseOrderRepository.subtractQuantityFromSupplierMedicine(supplierId, purchaseOrderMedicine.getMedicine().getId(), purchaseOrderMedicine.getQuantity());
            if(rowsChanged != 1)
                throw new FailedToSaveException("Failed to decrease quantity of supplier medicine");
        }

        List<String> declinedSuppliersEmails = purchaseOrderRepository.getAllDeclinedSuppliers(orderId);
        String acceptedSupplierEmail = purchaseOrderRepository.getAcceptedSupplier(orderId);
        sendEmailsToSuppliers(acceptedSupplierEmail, declinedSuppliersEmails, purchaseOrder.getPharmacyAdmin().getPharmacy().getName());

    }

    @Override
    public PurchaseOrderDTO updatePurchaseOrder(UUID orderId, NewPurchaseOrderDTO newPurchaseOrderDTO) throws FailedToSaveException, ActionNotAllowedException, ChangeSetPersister.NotFoundException {
        int numberOfOffers = purchaseOrderRepository.getNumberOfOffersForOrder(orderId);
        if(numberOfOffers != 0) {
            throw new ActionNotAllowedException("You can't update purchase orders that have offers.");
        }
        PurchaseOrder purchaseOrder = purchaseOrderRepository.getPurchaseOrderById(orderId);
        if(purchaseOrder == null) {
            throw new ChangeSetPersister.NotFoundException();
        }
        if (!purchaseOrder.getPharmacyAdmin().getId().equals(newPurchaseOrderDTO.getPharmacyAdminId())) {
            throw new ActionNotAllowedException("You can't update purchase orders that are not yours");
        }
        int rowsChanged = purchaseOrderRepository.updatePurchaseOrder(orderId, newPurchaseOrderDTO.getEndDate());
        if(rowsChanged != 1) {
            throw new FailedToSaveException("Could not update purchase order.");
        }

        purchaseOrderRepository.deletePurchaseOrderMedicines(orderId);

        return getPurchaseOrderDTO(orderId, newPurchaseOrderDTO);
    }

    @Override
    public void deletePurchaseOrder(UUID orderId, UUID phadminId) throws NotFoundException, ActionNotAllowedException {
        int numberOfOffers = purchaseOrderRepository.getNumberOfOffersForOrder(orderId);
        if(numberOfOffers != 0) {
            throw new ActionNotAllowedException("You can't delete purchase orders that have offers.");
        }
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(orderId).orElse(null);
        if(purchaseOrder == null) {
            throw new NotFoundException("This purchase order does not exists!");
        }
        if(!purchaseOrder.getPharmacyAdmin().getId().equals(phadminId)) {
            throw new ActionNotAllowedException("You can't delete purchase orders that are not yours.");
        }
        purchaseOrderRepository.deletePurchaseOrderMedicines(orderId);
        purchaseOrderRepository.deletePurchaseOrder(orderId);
    }

    private PurchaseOrderDTO getPurchaseOrderDTO(UUID orderId, NewPurchaseOrderDTO newPurchaseOrderDTO) throws FailedToSaveException {
        int rowsChanged;
        for(PurchaseOrderMedicineDTO purchaseOrderMedicineDTO : newPurchaseOrderDTO.getMedicines()) {
            rowsChanged = purchaseOrderRepository.insertPurchaseOrderMedicine(orderId,
                    purchaseOrderMedicineDTO.getMedicineId(), purchaseOrderMedicineDTO.getOrderQuantity());
            if(rowsChanged != 1) {
                throw new FailedToSaveException("Could not save new purchase order medicine.");
            }
        }

        PurchaseOrder purchaseOrder = purchaseOrderRepository.getPurchaseOrderById(orderId);

        UUID pharmacyId = purchaseOrder.getPharmacyAdmin().getPharmacy().getId();
        List<String> pharmacyMedicineIds = purchaseOrderRepository.getPharmacyMedicineUUIDs(pharmacyId);

        UserInfoDTO pharmacyAdminInfoDTO = new UserInfoDTO(purchaseOrder.getPharmacyAdmin().getId(),
                purchaseOrder.getPharmacyAdmin().getName(), purchaseOrder.getPharmacyAdmin().getSurname(),
                purchaseOrder.getPharmacyAdmin().getEmail(), purchaseOrder.getPharmacyAdmin().getPhoneNumber());
        List<PurchaseOrderMedicine> purchaseOrderMedicines = purchaseOrder.getPurchaseOrderMedicines();
        List<PurchaseOrderMedicineDTO> purchaseOrderMedicineDTOs = new ArrayList<>();
        for(PurchaseOrderMedicine purchaseOrderMedicine : purchaseOrderMedicines) {

            PurchaseOrderMedicineDTO purchaseOrderMedicineDTO = new PurchaseOrderMedicineDTO(purchaseOrderMedicine.getMedicine().getId(),
                    purchaseOrderMedicine.getMedicine().getName(), purchaseOrderMedicine.getQuantity());
            purchaseOrderMedicineDTOs.add(purchaseOrderMedicineDTO);
            if(!pharmacyMedicineIds.contains(purchaseOrderMedicine.getMedicine().getId().toString())) {
                pharmacyMedicinesRepository.insert(pharmacyId, purchaseOrderMedicine.getMedicine().getId());
            }
        }

        PurchaseOrderDTO purchaseOrderDTO = new PurchaseOrderDTO(purchaseOrder.getId(), pharmacyAdminInfoDTO,
                purchaseOrder.getEndDate(), purchaseOrderMedicineDTOs, null, 0);

        return purchaseOrderDTO;
    }

    private void sendEmailsToSuppliers(String acceptedSupplierEmail, List<String> declinedSuppliersEmails, String pharmacyName) {
        String subjectAccepted = "[ISA Pharmacy] Your offer has been accepted!";
        String bodyAccepted = "Your offer for " + pharmacyName + " has been accepted!";
        new Thread(() -> emailService.sendMail(acceptedSupplierEmail, subjectAccepted, bodyAccepted)).start();

        String subjectDeclined = "[ISA Pharmacy] Your offer has been declined.";
        String bodyDeclined = "Your offer for " + pharmacyName + " has been declined!";
        new Thread(() -> sendEmails(declinedSuppliersEmails, subjectDeclined, bodyDeclined)).start();
    }

    private void sendEmails(List<String> suppliersEmails, String subject, String body) {
        for(String supplierEmail : suppliersEmails)
            emailService.sendMail(supplierEmail, subject, body);
    }

}
