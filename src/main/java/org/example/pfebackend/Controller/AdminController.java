package org.example.pfebackend.Controller;


import org.example.pfebackend.Entity.Admin;
import org.example.pfebackend.Service.AdminService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/admin")
@CrossOrigin("*")
public class AdminController {
    @Autowired
    AdminService service;
    @Autowired
    ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<Object> Update(@RequestBody Admin admin) {
        ResponseEntity<Admin> user=service.updateAdmin(admin);
        if (user.getStatusCode() == HttpStatus.OK) {
            Admin userDto = modelMapper.map(user.getBody(), Admin.class);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        } else if (user.getStatusCode() == HttpStatus.NOT_FOUND) {
            return new ResponseEntity<>("Utilisateur introuvable", HttpStatus.NOT_FOUND);
        } else if (user.getStatusCode() == HttpStatus.FOUND) {
            return new ResponseEntity<>("email existe", HttpStatus.FOUND);
        } else {
            return new ResponseEntity<>("Erreur Inconnu", HttpStatus.BAD_REQUEST);

        }
    }

    @GetMapping("/{email}")
    public ResponseEntity<Object> retrieve(@PathVariable("email") String email) {
        Optional<Admin> admin = service.retrieveAdmin(email);
        if (admin.isPresent()) {
            return new ResponseEntity<>(modelMapper.map(admin.get(), Admin.class), HttpStatus.OK);
        }
        return new ResponseEntity<>("Utilisateur introuvable", HttpStatus.NOT_FOUND);
    }
}
