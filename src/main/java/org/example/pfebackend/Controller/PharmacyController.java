package org.example.pfebackend.Controller;

import org.example.pfebackend.Dto.PharmacyDto;
import org.example.pfebackend.Dto.UpdatePharmacyDto;
import org.example.pfebackend.Entity.Pharmacy;
import org.example.pfebackend.Service.PharmacyService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/pharmacy")
@CrossOrigin("*")
public class PharmacyController {
    @Autowired
    PharmacyService service;
    @Autowired
    ModelMapper modelMapper;

    @PostMapping("/Add")
    public ResponseEntity<Object> Add(@ModelAttribute PharmacyDto pha) throws IOException {
        ResponseEntity<Pharmacy> user=service.AddPharmacy(pha);
        if (user.getStatusCode() == HttpStatus.OK) {
            Pharmacy userDto = modelMapper.map(user.getBody(), Pharmacy.class);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        } else if (user.getStatusCode() == HttpStatus.FOUND) {
            return new ResponseEntity<>("Email existe", HttpStatus.FOUND);
        }else {
            return new ResponseEntity<>("Erreur Inconnu", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/UpdateWithImage/{id}")
    public ResponseEntity<Object> UpdateWithImage(@ModelAttribute PharmacyDto pha,@PathVariable("id") Integer id) throws IOException {
        ResponseEntity<Pharmacy> user=service.UpdatePharmacyWithImage(pha,id);
        if (user.getStatusCode() == HttpStatus.OK) {
            Pharmacy userDto = modelMapper.map(user.getBody(), Pharmacy.class);
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
    public ResponseEntity<Object> Update(@RequestBody UpdatePharmacyDto dto)  {
        ResponseEntity<Pharmacy> user=service.UpdatePharmacy(dto);
        if (user.getStatusCode() == HttpStatus.OK) {
            Pharmacy userDto = modelMapper.map(user.getBody(), Pharmacy.class);
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
        Optional<Pharmacy> doctor = service.retrieveOnePharmacy(email);
        if (doctor.isPresent()) {
            return new ResponseEntity<>(modelMapper.map(doctor.get(), Pharmacy.class), HttpStatus.OK);
        }
        return new ResponseEntity<>("email introuvable", HttpStatus.NOT_FOUND);
    }
    @GetMapping("/All")
    public List<Pharmacy> retrieveAllDoctors() {
        return service.retrieveAllPharmacy();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Object>delete (@PathVariable("id") Integer id) {
        boolean deleted = service.deletePharmacy(id);
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
