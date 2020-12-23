package team18.pharmacyapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class PharmacyappApplication {

	public static void main(String[] args) {
		SpringApplication.run(PharmacyappApplication.class, args);
	}

}
