package team18.pharmacyapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team18.pharmacyapp.model.medicine.MedicineRequests;
import team18.pharmacyapp.repository.MedicineRequestRepository;
import team18.pharmacyapp.service.interfaces.MedicineRequestsService;

import java.util.UUID;

@Service
public class MedicineRequestsServiceImpl implements MedicineRequestsService {
    private final MedicineRequestRepository repository;

    @Autowired
    public MedicineRequestsServiceImpl(MedicineRequestRepository repository) {
        this.repository = repository;
    }

    @Override
    public MedicineRequests checkRequest(UUID medicineId, UUID pharmacyId) {
        String requests = repository.getByMedicineAndPharmacy(medicineId);
        if (requests == null) {
            UUID id = UUID.randomUUID();
            repository.createNew(id, medicineId, pharmacyId);
            return null;
        }
        repository.addRequest(UUID.fromString(requests));
        return null;
    }


}
