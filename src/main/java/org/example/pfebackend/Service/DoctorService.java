package org.example.pfebackend.Service;

import org.example.pfebackend.Dto.DoctorDto;
import org.example.pfebackend.Dto.NotificationDto;
import org.example.pfebackend.Dto.UpdateDoctorDto;
import org.example.pfebackend.Dto.UserWrapper;
import org.example.pfebackend.Entity.Admin;
import org.example.pfebackend.Entity.Doctor;
import org.example.pfebackend.Repository.AdminRepo;
import org.example.pfebackend.Repository.DoctorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {
    @Autowired
    DoctorRepo doctorRepo;
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

    public ResponseEntity<Doctor> AddDoctor(DoctorDto d) throws IOException {
        UserWrapper user = authService.getUserByEmail(d.getEmail());
        if(user!=null){
            return new ResponseEntity<>(HttpStatus.FOUND);
        }
        Doctor doctor = new Doctor();
        doctor.setEmail(d.getEmail());
        doctor.setPassword(bcryptPasswordEncoder.encode(d.getPassword()));
        doctor.setFirstName(d.getFirstName());
        doctor.setLastName(d.getLastName());
        doctor.setAddress(d.getAddress());
        doctor.setPhone(d.getPhone());
        String fileName=uploadFileService.uploadFile(d.getFile());
        doctor.setImg(fileName);
        doctorRepo.save(doctor);
        List<Admin> admins = adminRepo.findAll();
        for(Admin admin:admins){
            NotificationDto notifAdmin = new NotificationDto();
            notifAdmin.setType("ADMIN");
            notifAdmin.setMessage(
                    "Un medecin avec le nom" + doctor.getFirstName() + " " + doctor.getLastName() + "."
                            + " a creer un compte" + ".");
            notifAdmin.setIdRecever(admin.getId());
            notificationService.AddNotification(notifAdmin);
        }
        return ResponseEntity.ok(doctor);
    }

    public ResponseEntity<Doctor> UpdateDoctorWithImage(DoctorDto d,Integer id) throws IOException {
        Optional<Doctor> doc = doctorRepo.findById(id);
        if (!doc.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        UserWrapper user = authService.getUserByEmail(d.getEmail());
        if(doc.get().getEmail()!=d.getEmail()&&user!=null) {
            return new ResponseEntity<>(HttpStatus.FOUND);
        }
        if(doc.get().getPassword()!=d.getPassword()) {
            doc.get().setPassword(bcryptPasswordEncoder.encode(d.getPassword()));
        }
        doc.get().setEmail(d.getEmail());
        doc.get().setFirstName(d.getFirstName());
        doc.get().setLastName(d.getLastName());
        doc.get().setAddress(d.getAddress());
        doc.get().setPhone(d.getPhone());
        String fileName=uploadFileService.uploadFile(d.getFile());
        doc.get().setImg(fileName);
        doctorRepo.save(doc.get());
        return ResponseEntity.ok(doc.get());
    }
    public ResponseEntity<Doctor> UpdateDoctor(UpdateDoctorDto dto) {
        Optional<Doctor> doc = doctorRepo.findById(dto.getId());
        if (!doc.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        UserWrapper user = authService.getUserByEmail(dto.getEmail());
        if(doc.get().getEmail()!=dto.getEmail()&&user!=null) {
            return new ResponseEntity<>(HttpStatus.FOUND);
        }
        if(doc.get().getPassword()!=dto.getPassword()) {
            doc.get().setPassword(bcryptPasswordEncoder.encode(dto.getPassword()));
        }
        doc.get().setEmail(dto.getEmail());
        doc.get().setFirstName(dto.getFirstName());
        doc.get().setLastName(dto.getLastName());
        doc.get().setAddress(dto.getAddress());
        doc.get().setPhone(dto.getPhone());
        doctorRepo.save(doc.get());
        return ResponseEntity.ok(doc.get());
    }

    public Optional<Doctor> retrieveOneDoc(String email){
        return doctorRepo.findByEmail(email);
    }

    public List<Doctor> retrieveAllDoc(){
        return doctorRepo.findAll();
    }
    public Boolean deleteDoc(Integer id) {
        Optional<Doctor> doc = doctorRepo.findById(id);
        if (doc.isPresent()) {
            doctorRepo.delete(doc.get());
            return true;
        }
        return false;
    }

    public Boolean changeStatus(Integer id) {
        Optional<Doctor> doc = doctorRepo.findById(id);
        if (doc.isPresent()) {
            doc.get().setActive(!doc.get().isActive());
            if(doc.get().isActive()) {
                emailService.sendEmail(doc.get().getEmail(), "Activation du compte", "Nous sommes heureux de vous annoncer que votre compte est désormais actif et que vous pouvez accéder à l'application.");
            }
            else {
                emailService.sendEmail(doc.get().getEmail(), "Désactivation du compte", "Nous sommes désolés de vous annoncer que votre compte est temporairement bloqué. Si vous souhaitez le réactiver, veuillez contacter notre support.");
            }
            return true;
        }
        return false;
    }

}
