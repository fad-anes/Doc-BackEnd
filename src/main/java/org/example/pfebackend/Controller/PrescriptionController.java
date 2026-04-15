package org.example.pfebackend.Controller;

import org.example.pfebackend.Dto.PrescriptionDto;
import org.example.pfebackend.Entity.Prescription;
import org.example.pfebackend.Service.PrescriptionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/prescription")
@CrossOrigin("*")
public class PrescriptionController {
    @Autowired
    PrescriptionService service;
    @Autowired
    ModelMapper modelMapper;

    @PostMapping("/Add")
    public ResponseEntity<Object> Add(@RequestBody PrescriptionDto p){
        ResponseEntity<Prescription> user=service.AddPrescription(p);
        if (user.getStatusCode() == HttpStatus.OK) {
            Prescription userDto = modelMapper.map(user.getBody(), Prescription.class);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        } else if (user.getStatusCode() == HttpStatus.NOT_FOUND) {
            return new ResponseEntity<>("Objet introuvable", HttpStatus.NOT_FOUND);
        }else {
            return new ResponseEntity<>("Erreur Inconnu", HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/ChangeStatus/{id}")
    public ResponseEntity<Object> ChangeStatus(@PathVariable("id") Integer id) {
        ResponseEntity<Prescription> user=service.ChangeStatus(id);
        if (user.getStatusCode() == HttpStatus.OK) {
            Prescription userDto = modelMapper.map(user.getBody(), Prescription.class);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        } else if (user.getStatusCode() == HttpStatus.NOT_FOUND) {
            return new ResponseEntity<>("Objet introuvable", HttpStatus.NOT_FOUND);
        }else {
            return new ResponseEntity<>("Erreur Inconnu", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/All/{id}/{role}")
    public List<Prescription> retrieveAllPrescriptions(@PathVariable("id") Integer id, @PathVariable("role") String role) {
        return service.retrieveAllPrescription(id,role);
    }
}
