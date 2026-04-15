package org.example.pfebackend.Service;

import org.example.pfebackend.Dto.PatientDto;
import org.example.pfebackend.Dto.UserWrapper;
import org.example.pfebackend.Entity.Patient;
import org.example.pfebackend.Repository.PatientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {
    @Autowired
    PatientRepo patientRepo;
    @Autowired
    AuthService authService;
    BCryptPasswordEncoder bcryptPasswordEncoder =new BCryptPasswordEncoder();

    public ResponseEntity<Patient> AddPatient(PatientDto d){
        UserWrapper user = authService.getUserByEmail(d.getEmail());
        if(user!=null){
            return new ResponseEntity<>(HttpStatus.FOUND);
        }
        Patient patient = new Patient();
        patient.setEmail(d.getEmail());
        patient.setPassword(bcryptPasswordEncoder.encode(d.getPassword()));
        patient.setFirstName(d.getFirstName());
        patient.setLastName(d.getLastName());
        patient.setPhone(d.getPhone());
        patientRepo.save(patient);
        return ResponseEntity.ok(patient);
    }

    public ResponseEntity<Patient> UpdatePatient(PatientDto p) {
        Optional<Patient> pa = patientRepo.findById(p.getId());
        if (!pa.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        UserWrapper user = authService.getUserByEmail(p.getEmail());
        if(pa.get().getEmail()!=p.getEmail()&&user!=null) {
            return new ResponseEntity<>(HttpStatus.FOUND);
        }
        if(pa.get().getPassword()!=p.getPassword()) {
            pa.get().setPassword(bcryptPasswordEncoder.encode(p.getPassword()));
        }
        pa.get().setEmail(p.getEmail());
        pa.get().setFirstName(p.getFirstName());
        pa.get().setLastName(p.getLastName());
        pa.get().setPhone(p.getPhone());
        patientRepo.save(pa.get());
        return ResponseEntity.ok(pa.get());
    }

    public Optional<Patient> retrieveOnePatient(String email){
        return patientRepo.findByEmail(email);
    }

    public List<Patient> retrieveAllPatient(){
        return patientRepo.findAll();
    }
    public Boolean deletePatient(Integer id) {
        Optional<Patient> pa = patientRepo.findById(id);
        if (pa.isPresent()) {
            patientRepo.delete(pa.get());
            return true;
        }
        return false;
    }

    public Boolean changeStatus(Integer id) {
        Optional<Patient> pa = patientRepo.findById(id);
        if (pa.isPresent()) {
            pa.get().setActive(!pa.get().isActive());
            return true;
        }
        return false;
    }
}
