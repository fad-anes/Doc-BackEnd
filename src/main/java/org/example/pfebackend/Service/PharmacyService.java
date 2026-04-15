package org.example.pfebackend.Service;

import org.example.pfebackend.Dto.PharmacyDto;
import org.example.pfebackend.Dto.UserWrapper;
import org.example.pfebackend.Entity.Pharmacy;
import org.example.pfebackend.Repository.PharmacyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class PharmacyService {
    @Autowired
    PharmacyRepo pharmacyRepo;
    @Autowired
    UploadFileService uploadFileService;
    @Autowired
    AuthService authService;
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
        return ResponseEntity.ok(pharmacy);
    }

    public ResponseEntity<Pharmacy> UpdatePharmacyWithImage(PharmacyDto d) throws IOException {
        Optional<Pharmacy> pha = pharmacyRepo.findById(d.getId());
        if (!pha.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        UserWrapper user = authService.getUserByEmail(d.getEmail());
        if(pha.get().getEmail()!=d.getEmail()&&user!=null) {
            return new ResponseEntity<>(HttpStatus.FOUND);
        }
        if(pha.get().getPassword()!=d.getPassword()) {
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

    public ResponseEntity<Pharmacy> UpdatePharmacy(Integer id, String name,String email,String password,String phone, String address) {
        Optional<Pharmacy> pha = pharmacyRepo.findById(id);
        if (!pha.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        UserWrapper user = authService.getUserByEmail(email);
        if(pha.get().getEmail()!=email&&user!=null) {
            return new ResponseEntity<>(HttpStatus.FOUND);
        }
        if(pha.get().getPassword()!=password) {
            pha.get().setPassword(bcryptPasswordEncoder.encode(password));
        }
        pha.get().setEmail(email);
        pha.get().setName(name);
        pha.get().setAddress(address);
        pha.get().setPhone(phone);
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
            return true;
        }
        return false;
    }
}
