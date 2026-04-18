package org.example.pfebackend.Controller;

import org.example.pfebackend.Dto.DoctorDto;
import org.example.pfebackend.Dto.UpdateDoctorDto;
import org.example.pfebackend.Entity.Doctor;
import org.example.pfebackend.Service.DoctorService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/doctor")
@CrossOrigin("*")
public class DoctorController {
    @Autowired
    DoctorService service;
    @Autowired
    ModelMapper modelMapper;

    @PostMapping("/Add")
    public ResponseEntity<Object> Add(@ModelAttribute DoctorDto doc) throws IOException {
        ResponseEntity<Doctor> user=service.AddDoctor(doc);
        if (user.getStatusCode() == HttpStatus.OK) {
            Doctor userDto = modelMapper.map(user.getBody(), Doctor.class);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        } else if (user.getStatusCode() == HttpStatus.FOUND) {
            return new ResponseEntity<>("Email existe", HttpStatus.FOUND);
        }else {
            return new ResponseEntity<>("Erreur Inconnu", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/UpdateWithImage/{id}")
    public ResponseEntity<Object> UpdateWithImage(@ModelAttribute DoctorDto doc,@PathVariable("id") Integer id) throws IOException {
        ResponseEntity<Doctor> user=service.UpdateDoctorWithImage(doc,id);
        if (user.getStatusCode() == HttpStatus.OK) {
            Doctor userDto = modelMapper.map(user.getBody(), Doctor.class);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        }  else if (user.getStatusCode() == HttpStatus.NOT_FOUND) {
            return new ResponseEntity<>("Utilisateur introuvable", HttpStatus.NOT_FOUND);
        } else if (user.getStatusCode() == HttpStatus.FOUND) {
            return new ResponseEntity<>("email existe", HttpStatus.FOUND);
        } else {
            return new ResponseEntity<>("Erreur Inconnu", HttpStatus.BAD_REQUEST);

        }
    }

    @PostMapping("/Update")
    public ResponseEntity<Object> Update(@RequestBody UpdateDoctorDto dto)  {
        ResponseEntity<Doctor> user=service.UpdateDoctor(dto);
        if (user.getStatusCode() == HttpStatus.OK) {
            Doctor userDto = modelMapper.map(user.getBody(), Doctor.class);
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
        Optional<Doctor> doctor = service.retrieveOneDoc(email);
        if (doctor.isPresent()) {
            return new ResponseEntity<>(modelMapper.map(doctor.get(), Doctor.class), HttpStatus.OK);
        }
        return new ResponseEntity<>("email introuvable", HttpStatus.NOT_FOUND);
    }
    @GetMapping("/All")
    public List<Doctor> retrieveAllDoctors() {
        return service.retrieveAllDoc();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Object>delete (@PathVariable("id") Integer id) {
        boolean deleted = service.deleteDoc(id);
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
