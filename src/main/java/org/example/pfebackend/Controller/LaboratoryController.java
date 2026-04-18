package org.example.pfebackend.Controller;

import org.example.pfebackend.Dto.LaboratoryDto;
import org.example.pfebackend.Dto.UpdateLaboratoryDto;
import org.example.pfebackend.Entity.Laboratory;
import org.example.pfebackend.Service.LaboratoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/laboratory")
@CrossOrigin("*")
public class LaboratoryController {
    @Autowired
    LaboratoryService service;
    @Autowired
    ModelMapper modelMapper;

    @PostMapping("/Add")
    public ResponseEntity<Object> Add(@ModelAttribute LaboratoryDto lab) throws IOException {
        ResponseEntity<Laboratory> user=service.AddLaboratory(lab);
        if (user.getStatusCode() == HttpStatus.OK) {
            Laboratory userDto = modelMapper.map(user.getBody(), Laboratory.class);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        } else if (user.getStatusCode() == HttpStatus.FOUND) {
            return new ResponseEntity<>("Email existe", HttpStatus.FOUND);
        }else {
            return new ResponseEntity<>("Erreur Inconnu", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/UpdateWithImage/{id}")
    public ResponseEntity<Object> UpdateWithImage(@ModelAttribute LaboratoryDto lab,@PathVariable("id") Integer id) throws IOException {
        ResponseEntity<Laboratory> user=service.UpdateLaboratoryWithImage(lab,id);
        if (user.getStatusCode() == HttpStatus.OK) {
            Laboratory userDto = modelMapper.map(user.getBody(), Laboratory.class);
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
    public ResponseEntity<Object> Update(@RequestBody UpdateLaboratoryDto dto)  {
        ResponseEntity<Laboratory> user=service.UpdateLaboratory(dto);
        if (user.getStatusCode() == HttpStatus.OK) {
            Laboratory userDto = modelMapper.map(user.getBody(), Laboratory.class);
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
        Optional<Laboratory> doctor = service.retrieveOneLaboratory(email);
        if (doctor.isPresent()) {
            return new ResponseEntity<>(modelMapper.map(doctor.get(), Laboratory.class), HttpStatus.OK);
        }
        return new ResponseEntity<>("email introuvable", HttpStatus.NOT_FOUND);
    }
    @GetMapping("/All")
    public List<Laboratory> retrieveAllDoctors() {
        return service.retrieveAllLaboratory();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Object>delete (@PathVariable("id") Integer id) {
        boolean deleted = service.deleteLaboratory(id);
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
