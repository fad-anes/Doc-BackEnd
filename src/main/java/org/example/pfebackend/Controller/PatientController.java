package org.example.pfebackend.Controller;

import org.example.pfebackend.Dto.PatientDto;
import org.example.pfebackend.Entity.Patient;
import org.example.pfebackend.Service.PatientService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/patient")
@CrossOrigin("*")
public class PatientController {
    @Autowired
    PatientService service;
    @Autowired
    ModelMapper modelMapper;

    @PostMapping("/Add")
    public ResponseEntity<Object> Add(@RequestBody PatientDto pat)  {
        ResponseEntity<Patient> user=service.AddPatient(pat);
        if (user.getStatusCode() == HttpStatus.OK) {
            Patient userDto = modelMapper.map(user.getBody(), Patient.class);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        } else if (user.getStatusCode() == HttpStatus.FOUND) {
            return new ResponseEntity<>("Email existe", HttpStatus.FOUND);
        }else {
            return new ResponseEntity<>("Erreur Inconnu", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/Update")
    public ResponseEntity<Object> Update(@RequestBody PatientDto pat)  {
        ResponseEntity<Patient> user=service.UpdatePatient(pat);
        if (user.getStatusCode() == HttpStatus.OK) {
            Patient userDto = modelMapper.map(user.getBody(), Patient.class);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        }  else if (user.getStatusCode() == HttpStatus.NOT_FOUND) {
            return new ResponseEntity<>("Utilisateur introuvable", HttpStatus.NOT_FOUND);
        } else if (user.getStatusCode() == HttpStatus.FOUND) {
            return new ResponseEntity<>("email existe", HttpStatus.FOUND);
        } else {
            return new ResponseEntity<>("Erreur Inconnu", HttpStatus.BAD_REQUEST);

        }
    }
    @GetMapping("/One/{email}")
    public ResponseEntity<Object> retrieve(@PathVariable("email") String email) {
        Optional<Patient> patient = service.retrieveOnePatient(email);
        if (patient.isPresent()) {
            return new ResponseEntity<>(modelMapper.map(patient.get(), Patient.class), HttpStatus.OK);
        }
        return new ResponseEntity<>("email introuvable", HttpStatus.NOT_FOUND);
    }
    @GetMapping("/All")
    public List<Patient> retrieveAllDoctors() {
        return service.retrieveAllPatient();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Object>delete (@PathVariable("id") Integer id) {
        boolean deleted = service.deletePatient(id);
        if (deleted) {
            return new ResponseEntity<>("Utilisateur supprimé", HttpStatus.OK);
        }
        return new ResponseEntity<>("Utilisateur introuvable", HttpStatus.NOT_FOUND);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Object>changeStatus (@PathVariable("id") Integer id) {
        boolean deleted = service.changeStatus(id);
        if (deleted) {
            return new ResponseEntity<>("Status Changé", HttpStatus.OK);
        }
        return new ResponseEntity<>("Utilisateur introuvable", HttpStatus.NOT_FOUND);
    }
}
