package team18.pharmacyapp.configuration;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
 * Koristi se za razrijesavanje cirkularnih JSON-a. Automatski postavlja sve reference na NULL, ali ih mi mozemo
 * get-ovati ako su nam potrebne.
 * */
@Configuration
public class HibernateConfiguration {
    @Bean
    public Hibernate5Module hibernate5Module() {
        return new Hibernate5Module();
    }
}
