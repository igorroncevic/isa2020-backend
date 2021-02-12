package team18.pharmacyapp.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import team18.pharmacyapp.model.dtos.MedicineIdNameDTO;
import team18.pharmacyapp.model.medicine.Medicine;
import team18.pharmacyapp.model.users.Patient;
import team18.pharmacyapp.repository.users.PatientRepository;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static team18.pharmacyapp.constants.PatientConstants.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PatientServiceUnitTestSTUD1 {

    @Mock
    private PatientRepository patientRepositoryMock;

    @Mock
    private Patient patientMock;

    @InjectMocks
    private PatientServiceImpl patientService;

    @Test
    public void testFindById(){
        when(patientRepositoryMock.findById(ID_MARKO)).thenReturn(Optional.of(patientMock));
        Patient retrievedPatient = patientService.getById(ID_MARKO);
        assertEquals(patientMock, retrievedPatient);
        verify(patientRepositoryMock,times(1)).findById(ID_MARKO);
        verifyNoMoreInteractions(patientRepositoryMock);
    }

    @Transactional
    @Test
    public void testUpdate(){
        when(patientRepositoryMock.findById(ID_MARKO)).thenReturn(Optional.of(new Patient(ID_MARKO, NAME_MARKO, SURNAME_MARKO)));
        Patient patientForUpdate = patientService.getById(ID_MARKO);
        assertThat(patientForUpdate).isNotNull();
        patientForUpdate.setName("Pera");
        patientForUpdate.setSurname("Peric");
        patientForUpdate = patientService.save(patientForUpdate);
        Patient savedPatient = patientService.getById(ID_MARKO);
        assertThat(savedPatient.getName()).isEqualTo("Pera");
        assertThat(savedPatient.getSurname()).isEqualTo("Peric");
    }
    @Test
    public void testFindAll(){
        when(patientRepositoryMock.findAll()).thenReturn(Arrays.asList(new Patient(ID_MARKO,NAME_MARKO,SURNAME_MARKO)));
        List<Patient> patients=patientService.findAll();
        assertThat(patients).hasSize(1);
        verify(patientRepositoryMock, times(1)).findAll();
        verifyNoMoreInteractions(patientRepositoryMock);
    }

    @Test
    public void testFindPatientPatients(){
        when(patientRepositoryMock.getAlergicMedicines(ID_MARKO)).thenReturn(Arrays.asList(new Medicine("Aspirin"), new Medicine("Brufen")));
        List<MedicineIdNameDTO> medicines = patientService.getAlergicTo(ID_MARKO);
        assertThat(medicines).hasSize(2);
        assertThat(medicines.get(0).getName()).isEqualTo("Aspirin");
        verify(patientRepositoryMock, times(1)).getAlergicMedicines(ID_MARKO);
        verifyNoMoreInteractions(patientRepositoryMock);
    }

    @Test
    public void testAddPenalty(){
        when(patientRepositoryMock.addPenalty(ID_MARKO)).thenReturn(1);

        int addedPenalty = patientService.addPenalty(ID_MARKO);
        assertThat(addedPenalty).isEqualTo(1);

        verify(patientRepositoryMock, times(1)).addPenalty(ID_MARKO);
        verifyNoMoreInteractions(patientRepositoryMock);
    }
}
