package org.example.pfebackend.Service;

import org.example.pfebackend.Dto.DoctorDto;
import org.example.pfebackend.Dto.UserWrapper;
import org.example.pfebackend.Entity.Doctor;
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
        return ResponseEntity.ok(doctor);
    }

    public ResponseEntity<Doctor> UpdateDoctorWithImage(DoctorDto d) throws IOException {
        Optional<Doctor> doc = doctorRepo.findById(d.getId());
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
    public ResponseEntity<Doctor> UpdateDoctor(Integer id, String firstName,String lastName,String email,String password,String phone, String address) {
        Optional<Doctor> doc = doctorRepo.findById(id);
        if (!doc.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        UserWrapper user = authService.getUserByEmail(email);
        if(doc.get().getEmail()!=email&&user!=null) {
            return new ResponseEntity<>(HttpStatus.FOUND);
        }
        if(doc.get().getPassword()!=password) {
            doc.get().setPassword(bcryptPasswordEncoder.encode(password));
        }
        doc.get().setEmail(email);
        doc.get().setFirstName(firstName);
        doc.get().setLastName(lastName);
        doc.get().setAddress(address);
        doc.get().setPhone(phone);
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
            return true;
        }
        return false;
    }

}
