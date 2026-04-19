package org.example.pfebackend.Service;

import org.example.pfebackend.Dto.NotificationDto;
import org.example.pfebackend.Dto.PharmacyDto;
import org.example.pfebackend.Dto.UpdatePharmacyDto;
import org.example.pfebackend.Dto.UserWrapper;
import org.example.pfebackend.Entity.Admin;
import org.example.pfebackend.Entity.Pharmacy;
import org.example.pfebackend.Repository.AdminRepo;
import org.example.pfebackend.Repository.PharmacyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PharmacyService {
    @Autowired
    PharmacyRepo pharmacyRepo;
    @Autowired
    UploadFileService uploadFileService;
    @Autowired
    AuthService authService;
    @Autowired
    EmailService emailService;
    @Autowired
    NotificationService notificationService;
    @Autowired
    AdminRepo adminRepo;
    BCryptPasswordEncoder bcryptPasswordEncoder =new BCryptPasswordEncoder();

    public ResponseEntity<Pharmacy> AddPharmacy(PharmacyDto d) throws IOException {
        UserWrapper user = authService.getUserByEmail(d.getEmail());
        if(user!=null){
            return new ResponseEntity<>(HttpStatus.FOUND);
        }
        Pharmacy pharmacy = new Pharmacy();
        pharmacy.setEmail(d.getEmail());
        pharmacy.setPassword(bcryptPasswordEncoder.encode(d.getPassword()));
        pharmacy.setName(d.getName());
        pharmacy.setAddress(d.getAddress());
        pharmacy.setPhone(d.getPhone());
        String fileName=uploadFileService.uploadFile(d.getFile());
        pharmacy.setImg(fileName);
        pharmacyRepo.save(pharmacy);

        List<Admin> admins = adminRepo.findAll();
        for(Admin admin:admins){
            NotificationDto notifAdmin = new NotificationDto();
            notifAdmin.setType("ADMIN");
            notifAdmin.setMessage(
                    "Une pharmacie avec le nom" + pharmacy.getName() + "."
                            + " a creer un compte" + ".");
            notifAdmin.setIdRecever(admin.getId());
            notificationService.AddNotification(notifAdmin);
        }
        return ResponseEntity.ok(pharmacy);
    }

    public ResponseEntity<Pharmacy> UpdatePharmacyWithImage(PharmacyDto d,Integer id) throws IOException {
        Optional<Pharmacy> pha = pharmacyRepo.findById(id);
        if (!pha.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        UserWrapper user = authService.getUserByEmail(d.getEmail());
        if(!Objects.equals(pha.get().getEmail(), d.getEmail()) &&user!=null) {
            return new ResponseEntity<>(HttpStatus.FOUND);
        }
        if(!Objects.equals(pha.get().getPassword(), d.getPassword())) {
            pha.get().setPassword(bcryptPasswordEncoder.encode(d.getPassword()));
        }
        pha.get().setEmail(d.getEmail());
        pha.get().setName(d.getName());
        pha.get().setAddress(d.getAddress());
        pha.get().setPhone(d.getPhone());
        String fileName=uploadFileService.uploadFile(d.getFile());
        pha.get().setImg(fileName);
        pharmacyRepo.save(pha.get());
        return ResponseEntity.ok(pha.get());
    }

    public ResponseEntity<Pharmacy> UpdatePharmacy(UpdatePharmacyDto dto) {
        Optional<Pharmacy> pha = pharmacyRepo.findById(dto.getId());
        if (!pha.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        UserWrapper user = authService.getUserByEmail(dto.getEmail());
        if(!Objects.equals(pha.get().getEmail(), dto.getEmail()) &&user!=null) {
            return new ResponseEntity<>(HttpStatus.FOUND);
        }
        if(!Objects.equals(pha.get().getPassword(), dto.getPassword())) {
            pha.get().setPassword(bcryptPasswordEncoder.encode(dto.getPassword()));
        }
        pha.get().setEmail(dto.getEmail());
        pha.get().setName(dto.getName());
        pha.get().setAddress(dto.getAddress());
        pha.get().setPhone(dto.getPhone());
        pharmacyRepo.save(pha.get());
        return ResponseEntity.ok(pha.get());
    }

    public Optional<Pharmacy> retrieveOnePharmacy(String email){
        return pharmacyRepo.findByEmail(email);
    }

    public List<Pharmacy> retrieveAllPharmacy(){
        return pharmacyRepo.findAll();
    }
    public Boolean deletePharmacy(Integer id) {
        Optional<Pharmacy> pha = pharmacyRepo.findById(id);
        if (pha.isPresent()) {
            pharmacyRepo.delete(pha.get());
            return true;
        }
        return false;
    }

    public Boolean changeStatus(Integer id) {
        Optional<Pharmacy> pha = pharmacyRepo.findById(id);
        if (pha.isPresent()) {
            pha.get().setActive(!pha.get().isActive());
            if(pha.get().isActive()) {
                emailService.sendEmail(pha.get().getEmail(), "Activation du compte", "Nous sommes heureux de vous annoncer que votre compte est désormais actif et que vous pouvez accéder à l'application.");
            }
            else {
                emailService.sendEmail(pha.get().getEmail(), "Désactivation du compte", "Nous sommes désolés de vous annoncer que votre compte est temporairement bloqué. Si vous souhaitez le réactiver, veuillez contacter notre support.");
            }
            return true;
        }
        return false;
    }
}
