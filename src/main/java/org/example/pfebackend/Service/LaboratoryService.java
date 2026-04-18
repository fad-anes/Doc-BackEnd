package org.example.pfebackend.Service;

import org.example.pfebackend.Dto.LaboratoryDto;
import org.example.pfebackend.Dto.NotificationDto;
import org.example.pfebackend.Dto.UpdateLaboratoryDto;
import org.example.pfebackend.Dto.UserWrapper;
import org.example.pfebackend.Entity.Admin;
import org.example.pfebackend.Entity.Laboratory;
import org.example.pfebackend.Repository.AdminRepo;
import org.example.pfebackend.Repository.LaboratoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class LaboratoryService {
    @Autowired
    LaboratoryRepo laboratoryRepo;
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


    public ResponseEntity<Laboratory> AddLaboratory(LaboratoryDto d) throws IOException {
        UserWrapper user = authService.getUserByEmail(d.getEmail());
        if(user!=null){
            return new ResponseEntity<>(HttpStatus.FOUND);
        }
        Laboratory laboratory = new Laboratory();
        laboratory.setEmail(d.getEmail());
        laboratory.setPassword(bcryptPasswordEncoder.encode(d.getPassword()));
        laboratory.setName(d.getName());
        laboratory.setAddress(d.getAddress());
        laboratory.setPhone(d.getPhone());
        String fileName=uploadFileService.uploadFile(d.getFile());
        laboratory.setImg(fileName);
        laboratoryRepo.save(laboratory);

        List<Admin> admins = adminRepo.findAll();
        for(Admin admin:admins){
            NotificationDto notifAdmin = new NotificationDto();
            notifAdmin.setType("ADMIN");
            notifAdmin.setMessage(
                    "Une laboratoire avec le nom" + laboratory.getName() + "."
                            + " a creer un compte" + ".");
            notifAdmin.setIdRecever(admin.getId());
            notificationService.AddNotification(notifAdmin);
        }
        return ResponseEntity.ok(laboratory);
    }

    public ResponseEntity<Laboratory> UpdateLaboratoryWithImage(LaboratoryDto d,Integer id) throws IOException {
        Optional<Laboratory> lab = laboratoryRepo.findById(id);
        if (!lab.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        UserWrapper user = authService.getUserByEmail(d.getEmail());
        if(lab.get().getEmail()!=d.getEmail()&&user!=null) {
            return new ResponseEntity<>(HttpStatus.FOUND);
        }
        if(lab.get().getPassword()!=d.getPassword()) {
            lab.get().setPassword(bcryptPasswordEncoder.encode(d.getPassword()));
        }
        lab.get().setEmail(d.getEmail());
        lab.get().setName(d.getName());
        lab.get().setAddress(d.getAddress());
        lab.get().setPhone(d.getPhone());
        String fileName=uploadFileService.uploadFile(d.getFile());
        lab.get().setImg(fileName);
        laboratoryRepo.save(lab.get());
        return ResponseEntity.ok(lab.get());
    }

    public ResponseEntity<Laboratory> UpdateLaboratory(UpdateLaboratoryDto dto) {
        Optional<Laboratory> lab = laboratoryRepo.findById(dto.getId());
        if (!lab.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        UserWrapper user = authService.getUserByEmail(dto.getEmail());
        if(lab.get().getEmail()!= dto.getEmail()&&user!=null) {
            return new ResponseEntity<>(HttpStatus.FOUND);
        }
        if(lab.get().getPassword()!= dto.getPassword()) {
            lab.get().setPassword(bcryptPasswordEncoder.encode(dto.getPassword()));
        }
        lab.get().setEmail(dto.getEmail());
        lab.get().setName(dto.getName());
        lab.get().setAddress(dto.getAddress());
        lab.get().setPhone(dto.getPhone());
        laboratoryRepo.save(lab.get());
        return ResponseEntity.ok(lab.get());
    }

    public Optional<Laboratory> retrieveOneLaboratory(String email){
        return laboratoryRepo.findByEmail(email);
    }

    public List<Laboratory> retrieveAllLaboratory(){
        return laboratoryRepo.findAll();
    }
    public Boolean deleteLaboratory(Integer id) {
        Optional<Laboratory> lab = laboratoryRepo.findById(id);
        if (lab.isPresent()) {
            laboratoryRepo.delete(lab.get());
            return true;
        }
        return false;
    }

    public Boolean changeStatus(Integer id) {
        Optional<Laboratory> lab = laboratoryRepo.findById(id);
        if (lab.isPresent()) {
            lab.get().setActive(!lab.get().isActive());
            if(lab.get().isActive()) {
                emailService.sendEmail(lab.get().getEmail(), "Activation du compte", "Nous sommes heureux de vous annoncer que votre compte est désormais actif et que vous pouvez accéder à l'application.");
            }
            else {
                emailService.sendEmail(lab.get().getEmail(), "Désactivation du compte", "Nous sommes désolés de vous annoncer que votre compte est temporairement bloqué. Si vous souhaitez le réactiver, veuillez contacter notre support.");
            }
            return true;
        }
        return false;
    }
}
