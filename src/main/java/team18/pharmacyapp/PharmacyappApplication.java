package team18.pharmacyapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@ConfigurationPropertiesScan
@EnableScheduling
@SpringBootApplication
@EnableWebSecurity
public class PharmacyappApplication {

    public static void main(String[] args) {
        SpringApplication.run(PharmacyappApplication.class, args);
    }

}
