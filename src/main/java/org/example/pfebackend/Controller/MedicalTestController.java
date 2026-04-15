package org.example.pfebackend.Controller;

import org.example.pfebackend.Dto.MedicalTestDto;
import org.example.pfebackend.Entity.MedicalTest;
import org.example.pfebackend.Service.MedicalTestService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/medicalTest")
@CrossOrigin("*")
public class MedicalTestController {
    @Autowired
    MedicalTestService service;
    @Autowired
    ModelMapper modelMapper;

    @PostMapping("/Add")
    public ResponseEntity<Object> Add(@RequestBody MedicalTestDto m){
        ResponseEntity<MedicalTest> user=service.AddMedicalTest(m);
        if (user.getStatusCode() == HttpStatus.OK) {
            MedicalTest userDto = modelMapper.map(user.getBody(), MedicalTest.class);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        } else if (user.getStatusCode() == HttpStatus.NOT_FOUND) {
            return new ResponseEntity<>("Objet introuvable", HttpStatus.NOT_FOUND);
        }else {
            return new ResponseEntity<>("Erreur Inconnu", HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/Result")
    public ResponseEntity<Object> Result(@RequestParam MultipartFile file,
                                         @RequestParam Integer id) throws IOException {
        ResponseEntity<MedicalTest> user=service.ResultMedicalTest(id,file);
        if (user.getStatusCode() == HttpStatus.OK) {
            MedicalTest userDto = modelMapper.map(user.getBody(), MedicalTest.class);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        } else if (user.getStatusCode() == HttpStatus.NOT_FOUND) {
            return new ResponseEntity<>("Objet introuvable", HttpStatus.NOT_FOUND);
        }else {
            return new ResponseEntity<>("Erreur Inconnu", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/All/{id}/{role}")
    public List<MedicalTest> retrieveAllMedicalTests(@PathVariable("id") Integer id,@PathVariable("role") String role) {
        return service.retrieveAllMedicalTest(id,role);
    }
}
