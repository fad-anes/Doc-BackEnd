package org.example.pfebackend.Service;

import org.example.pfebackend.Dto.OtpDto;
import org.example.pfebackend.Dto.UserWrapper;
import org.example.pfebackend.Entity.*;
import org.example.pfebackend.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthService {
    @Autowired
    PatientRepo patientRepo;
    @Autowired
    PharmacyRepo pharmacyRepo;
    @Autowired
    LaboratoryRepo laboratoryRepo;
    @Autowired
    AdminRepo adminRepo;
    @Autowired
    DoctorRepo doctorRepo;
    @Autowired
    OtpRepo otpRepo;
    @Autowired
    EmailService emailService;

    BCryptPasswordEncoder bcryptPasswordEncoder =new BCryptPasswordEncoder();

    public UserWrapper getUserByEmail(String email) {
        return doctorRepo.findByEmail(email)
                .map(d -> new UserWrapper("DOCTOR", d))
                .orElseGet(() ->
                        patientRepo.findByEmail(email)
                                .map(p -> new UserWrapper("PATIENT", p))
                                .orElseGet(() ->
                                        pharmacyRepo.findByEmail(email)
                                                .map(ph -> new UserWrapper("PHARMACY", ph))
                                                .orElseGet(() ->
                                                        laboratoryRepo.findByEmail(email)
                                                                .map(l -> new UserWrapper("LABORATORY", l))
                                                                .orElseGet(() ->
                                                                        adminRepo.findByEmail(email)
                                                                                .map(l -> new UserWrapper("ADMIN", l))
                                                                                .orElse(null)

                                                                )
                                                )
                                )
                );
    }

    public ResponseEntity<Otp> AddOtp(String email) {
        UserWrapper user = getUserByEmail(email);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Otp newOtp = new Otp();
        Random random = new Random();
        int otpCode = 100000 + random.nextInt(900000);
        newOtp.setNumber(String.valueOf(otpCode));
        String role = user.getRole();
        Object entity = user.getUser();

        switch (role) {

            case "DOCTOR":
                Doctor d = (Doctor) entity;
                newOtp.setIdUser(d.getId());
                newOtp.setType("DOCTOR");
                otpRepo.save(newOtp);
                emailService.sendEmail(email, "Code verification", "Bonjour voici votre code de verification :" +" "+ otpCode);
                return ResponseEntity.ok(newOtp);

            case "PATIENT":
                Patient p = (Patient) entity;

                newOtp.setIdUser(p.getId());
                newOtp.setType("PATIENT");
                otpRepo.save(newOtp);
                emailService.sendEmail(email, "Code verification", "Bonjour voici votre code de verification :" +" "+ otpCode);
                return ResponseEntity.ok(newOtp);

            case "PHARMACY":
                Pharmacy ph = (Pharmacy) entity;

                newOtp.setIdUser(ph.getId());
                newOtp.setType("PHARMACY");
                otpRepo.save(newOtp);
                emailService.sendEmail(email, "Code verification", "Bonjour voici votre code de verification :" +" "+ otpCode);
                return ResponseEntity.ok(newOtp);

            case "LABORATORY":
                Laboratory lab = (Laboratory) entity;

                newOtp.setIdUser(lab.getId());
                newOtp.setType("LABORATORY");
                otpRepo.save(newOtp);
                emailService.sendEmail(email, "Code verification", "Bonjour voici votre code de verification :" +" "+ otpCode);
                return ResponseEntity.ok(newOtp);

            case "ADMIN":
                Admin ad = (Admin) entity;

                newOtp.setIdUser(ad.getId());
                newOtp.setType("ADMIN");
                otpRepo.save(newOtp);
                emailService.sendEmail(email, "Code verification", "Bonjour voici votre code de verification :" +" "+ otpCode);
                return ResponseEntity.ok(newOtp);

            default:
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Otp> ChangePassword(OtpDto dto) {
        UserWrapper user = getUserByEmail(dto.getEmail());
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        String role = user.getRole();
        Object entity = user.getUser();

        switch (role) {

            case "DOCTOR":
                Doctor d = (Doctor) entity;
                Optional<Otp> docOtp = otpRepo.findByIdUserAndType(d.getId(),"DOCTOR");
                if(!docOtp.isPresent()){
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
                if(!Objects.equals(docOtp.get().getNumber(), dto.getNumber())){
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                d.setPassword(bcryptPasswordEncoder.encode(dto.getPassword()));
                doctorRepo.save(d);
                emailService.sendEmail(dto.getEmail(), "Mot de passe changer", "Bonjour votre mot de passe a ete changer");
                otpRepo.delete(docOtp.get());
                return ResponseEntity.ok(docOtp.get());

            case "PATIENT":
                Patient p = (Patient) entity;

                Optional<Otp> patiOtp = otpRepo.findByIdUserAndType(p.getId(),"PATIENT");
                if(!patiOtp.isPresent()){
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
                if(!Objects.equals(patiOtp.get().getNumber(), dto.getNumber())){
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                p.setPassword(bcryptPasswordEncoder.encode(dto.getPassword()));
                patientRepo.save(p);
                emailService.sendEmail(dto.getEmail(), "Mot de passe changer", "Bonjour votre mot de passe a ete changer");
                otpRepo.delete(patiOtp.get());
                return ResponseEntity.ok(patiOtp.get());

            case "PHARMACY":
                Pharmacy ph = (Pharmacy) entity;

                Optional<Otp> pharOtp = otpRepo.findByIdUserAndType(ph.getId(),"PHARMACY");
                if(!pharOtp.isPresent()){
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
                if(!Objects.equals(pharOtp.get().getNumber(), dto.getNumber())){
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                ph.setPassword(bcryptPasswordEncoder.encode(dto.getPassword()));
                pharmacyRepo.save(ph);
                emailService.sendEmail(dto.getEmail(), "Mot de passe changer", "Bonjour votre mot de passe a ete changer");
                otpRepo.delete(pharOtp.get());
                return ResponseEntity.ok(pharOtp.get());

            case "LABORATORY":
                Laboratory lab = (Laboratory) entity;

                Optional<Otp> labOtp = otpRepo.findByIdUserAndType(lab.getId(),"LABORATORY");
                if(!labOtp.isPresent()){
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
                if(!Objects.equals(labOtp.get().getNumber(), dto.getNumber())){
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                lab.setPassword(bcryptPasswordEncoder.encode(dto.getPassword()));
                laboratoryRepo.save(lab);
                emailService.sendEmail(dto.getEmail(), "Mot de passe changer", "Bonjour votre mot de passe a ete changer");
                otpRepo.delete(labOtp.get());
                return ResponseEntity.ok(labOtp.get());

            case "ADMIN":
                Admin ad = (Admin) entity;

                Optional<Otp> adOtp = otpRepo.findByIdUserAndType(ad.getId(),"ADMIN");
                if(!adOtp.isPresent()){
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
                if(!Objects.equals(adOtp.get().getNumber(), dto.getNumber())){
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                ad.setPassword(bcryptPasswordEncoder.encode(dto.getPassword()));
                adminRepo.save(ad);
                emailService.sendEmail(dto.getEmail(), "Mot de passe changer", "Bonjour votre mot de passe a ete changer");
                otpRepo.delete(adOtp.get());
                return ResponseEntity.ok(adOtp.get());

            default:
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
