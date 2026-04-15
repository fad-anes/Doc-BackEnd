package org.example.pfebackend.Service;

import org.example.pfebackend.Dto.UserWrapper;
import org.example.pfebackend.Entity.Admin;
import org.example.pfebackend.Repository.AdminRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {
    @Autowired
    AdminRepo adminRepo;
    @Autowired
    AuthService authService;
    public ResponseEntity<Admin> updateAdmin(Admin u) {
        Optional<Admin> admin = adminRepo.findById(u.getId());
        if (!admin.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        UserWrapper user = authService.getUserByEmail(u.getEmail());
        if(admin.get().getEmail()!=u.getEmail()&&user!=null) {
            return new ResponseEntity<>(HttpStatus.FOUND);
        }
        if(admin.get().getPassword()!=u.getPassword()) {
            BCryptPasswordEncoder bCryptPasswordEncoder =new BCryptPasswordEncoder();
            u.setPassword(bCryptPasswordEncoder.encode(u.getPassword()));
        }
        adminRepo.save(u);
        return ResponseEntity.ok(u);
    }
    public Optional<Admin> retrieveAdmin(String email){
        return adminRepo.findByEmail(email);
    }
}
