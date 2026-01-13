package com.medisched.config;

import com.medisched.model.entities.Doctor;
import com.medisched.repositories.UserProfileRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner init(UserProfileRepository repo, JdbcTemplate jdbcTemplate) {
        return args -> {
            Long maxId = jdbcTemplate.queryForObject("SELECT MAX(id) FROM userprofile", Long.class);
            if (maxId != null) {
                long nextId = maxId + 1;
                jdbcTemplate.execute("ALTER TABLE userprofile ALTER COLUMN id RESTART WITH " + nextId);
                System.out.println("Secventa ID-urilor a fost sincronizata la: " + nextId);
            }

            if (repo.count() == 0) {
                Doctor d1 = new Doctor();
                d1.setFirstName("Armin");
                d1.setLastName("Zamfirescu");
                d1.setSpecialization("Dermatologie");
                d1.setEmail("armin.zamfirescu@med.ro");
                repo.save(d1);

                Doctor d2 = new Doctor();
                d2.setFirstName("Ingrid");
                d2.setLastName("Săulescu");
                d2.setSpecialization("Pediatrie");
                d2.setEmail("ingrid.saulescu@med.ro");
                repo.save(d2);

                Doctor d3 = new Doctor();
                d3.setFirstName("Darius");
                d3.setLastName("Cantemir");
                d3.setSpecialization("Neurologie");
                d3.setEmail("darius.cantemir@med.ro");
                repo.save(d3);

                Doctor d4 = new Doctor();
                d4.setFirstName("Noemi");
                d4.setLastName("Berindei");
                d4.setSpecialization("Ortopedie");
                d4.setEmail("noemi.berindei@med.ro");
                repo.save(d4);

                System.out.println("Medici de test salvati pentru toate specializarile!");
            }
        };
    }
}