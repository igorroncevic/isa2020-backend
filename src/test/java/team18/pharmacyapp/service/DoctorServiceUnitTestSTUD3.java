package team18.pharmacyapp.service;

import com.sun.mail.imap.protocol.ID;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import team18.pharmacyapp.model.Address;
import team18.pharmacyapp.model.Pharmacy;
import team18.pharmacyapp.model.dtos.DoctorsPatientDTO;
import team18.pharmacyapp.model.dtos.PharmacyDTO;
import team18.pharmacyapp.model.users.Doctor;
import team18.pharmacyapp.repository.users.DoctorRepository;

import javax.print.Doc;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static team18.pharmacyapp.constants.DoctorConstants.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DoctorServiceUnitTestSTUD3 {

    @Mock
    private DoctorRepository doctorRepositoryMock;

    @Mock
    private Doctor doctorMock;

    @InjectMocks
    private DoctorServiceImpl doctorService;

    @Test
    public void testFindById(){
        when(doctorRepositoryMock.findById(ID_PERA)).thenReturn(Optional.of(doctorMock));
        Doctor dbDoctor= doctorService.getById(ID_PERA);
        assertEquals(doctorMock,dbDoctor);
        verify(doctorRepositoryMock,times(1)).findById(ID_PERA);
        verifyNoMoreInteractions(doctorRepositoryMock);
    }

    @Transactional
    @Test
    public void testUpdate(){
        when(doctorRepositoryMock.findById(ID_PERA)).thenReturn(Optional.of(new Doctor(ID_PERA,NAME_PERA,SURNAME_PERA)));
        Doctor doctorForUpdate= doctorService.getById(ID_PERA);
        assertThat(doctorForUpdate).isNotNull();
        doctorForUpdate.setName("Mirko");
        doctorForUpdate.setSurname("Mirkovic");
        doctorForUpdate=doctorService.update(doctorForUpdate);
        Doctor savedDoctor=doctorService.getById(ID_PERA);
        assertThat(savedDoctor.getName()).isEqualTo("Mirko");
        assertThat(savedDoctor.getSurname()).isEqualTo("Mirkovic");
    }
    @Test
    public void testFindAll(){
        when(doctorRepositoryMock.findAll()).thenReturn(Arrays.asList(new Doctor(ID_PERA,NAME_PERA,SURNAME_PERA)));
        List<Doctor> doctors=doctorService.findAll();
        assertThat(doctors).hasSize(1);
        verify(doctorRepositoryMock, times(1)).findAll();
        verifyNoMoreInteractions(doctorRepositoryMock);
    }

    @Test
    public void testFindDoctorPatients(){
        when(doctorRepositoryMock.findDoctorPatients(ID_PERA)).thenReturn(Arrays.asList(new DoctorsPatientDTO(null,"Mirko","Mirkovic",null,null), new DoctorsPatientDTO(null,"Slavko","Slavkovic",null,null)));
        List<DoctorsPatientDTO> doctors=doctorService.findDoctorsPatients(ID_PERA);
        assertThat(doctors).hasSize(2);
        assertThat(doctors.get(0).getName()).isEqualTo("Mirko");
        verify(doctorRepositoryMock, times(1)).findDoctorPatients(ID_PERA);
        verifyNoMoreInteractions(doctorRepositoryMock);
    }

    @Test
    public void findPharmacistPharmacy(){
        when(doctorRepositoryMock.getPharmPharmacy(ID_PERA)).thenReturn(new Pharmacy(ID_PHARMACY,"Jankovic",new Address(ID_PHARMACY,"SRB","NS","Bulevar")));
        PharmacyDTO pharmacy = doctorService.getPharmacistPharmacy(ID_PERA);

        assertThat(pharmacy).isNotNull();
        assertThat(pharmacy.getId()).isEqualTo(ID_PHARMACY);
        assertThat(pharmacy.getName()).isEqualTo("Jankovic");
        assertThat(pharmacy.getCity()).isEqualTo("NS");
        assertThat(pharmacy.getCountry()).isEqualTo("SRB");
        assertThat(pharmacy.getStreet()).isEqualTo("Bulevar");
        verify(doctorRepositoryMock, times(1)).getPharmPharmacy(ID_PERA);
        verifyNoMoreInteractions(doctorRepositoryMock);

    }
}
