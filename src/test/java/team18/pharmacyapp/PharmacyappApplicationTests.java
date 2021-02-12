package team18.pharmacyapp;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.boot.test.context.SpringBootTest;
import team18.pharmacyapp.controller.DoctorIntegrationTestSTUD3;
import team18.pharmacyapp.controller.TermIntegrationTestSTUD1;
import team18.pharmacyapp.service.DoctorServiceUnitTestSTUD3;
import team18.pharmacyapp.service.PatientServiceUnitTestSTUD1;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        DoctorServiceUnitTestSTUD3.class,
        DoctorIntegrationTestSTUD3.class,
        PatientServiceUnitTestSTUD1.class,
        TermIntegrationTestSTUD1.class,
})
@SpringBootTest
public class PharmacyappApplicationTests {

    @Test
    void contextLoads() {
    }

}