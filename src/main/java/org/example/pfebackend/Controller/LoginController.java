package org.example.pfebackend.Controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.example.pfebackend.Dto.LoginDto;
import org.example.pfebackend.Dto.UserWrapper;
import org.example.pfebackend.Entity.*;
import org.example.pfebackend.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
public class LoginController {
    @Autowired
    AuthService service;

    private String generateToken(String email, String name, String role, Integer id) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .claim("name", name)
                .claim("id", id)
                .signWith(SignatureAlgorithm.HS256, "SECRET")
                .compact();
    }
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginDto userDto) {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        UserWrapper user = service.getUserByEmail(userDto.getEmail());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Utilisateur introuvable");
        }

        String role = user.getRole();
        Object entity = user.getUser();

        switch (role) {

            case "DOCTOR":
                Doctor d = (Doctor) entity;

                if (!passwordEncoder.matches(userDto.getPassword(), d.getPassword())) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Mot de passe incorrect");
                }
                if(!d.isActive()){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Utilisateur non actif");
                }
                String doctorName =d.getFirstName()+" "+d.getLastName();
                return ResponseEntity.ok(generateToken(d.getEmail(),doctorName, "DOCTOR",d.getId()));

            case "PATIENT":
                Patient p = (Patient) entity;

                if (!passwordEncoder.matches(userDto.getPassword(), p.getPassword())) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Mot de passe incorrect");
                }
                if(!p.isActive()){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Utilisateur non actif");
                }
                String PatientName =p.getFirstName()+" "+p.getLastName();
                return ResponseEntity.ok(generateToken(p.getEmail(),PatientName, "PATIENT",p.getId()));

            case "PHARMACY":
                Pharmacy ph = (Pharmacy) entity;

                if (!passwordEncoder.matches(userDto.getPassword(), ph.getPassword())) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Mot de passe incorrect");
                }
                if(!ph.isActive()){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Utilisateur non actif");
                }
                return ResponseEntity.ok(generateToken(ph.getEmail(), ph.getName(), "PHARMACY",ph.getId()));

            case "LABORATORY":
                Laboratory lab = (Laboratory) entity;

                if (!passwordEncoder.matches(userDto.getPassword(), lab.getPassword())) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Mot de passe incorrect");
                }
                if(!lab.isActive()){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Utilisateur non actif");
                }
                return ResponseEntity.ok(generateToken(lab.getEmail(),lab.getName(), "LABORATORY",lab.getId()));

            case "ADMIN":
                Admin ad = (Admin) entity;

                if (!passwordEncoder.matches(userDto.getPassword(), ad.getPassword())) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Mot de passe incorrect");
                }
                String DoctorName =ad.getFirstName()+" "+ad.getLastName();
                return ResponseEntity.ok(generateToken(ad.getEmail(),DoctorName, "ADMIN",ad.getId()));

            default:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Rôle inconnu");
        }
    }

}
