package org.example.pfebackend.Service;

import org.example.pfebackend.Entity.Admin;
import org.example.pfebackend.Repository.AdminRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    @Autowired
    AdminRepo adminRepo;
    private BCryptPasswordEncoder bCryptPasswordEncoder =new BCryptPasswordEncoder();
    public void run(String... args) throws Exception {
        if (adminRepo.count() == 0) {
            Admin user = new Admin();
            String encodedPassword = bCryptPasswordEncoder.encode("admin");
            user.setPassword(encodedPassword);
            user.setEmail("admin@admin.com");
            user.setFirstName("Admin");
            user.setLastName("Admin");
            adminRepo.save(user);
        }
    }
}
