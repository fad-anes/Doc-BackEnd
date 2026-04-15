package org.example.pfebackend.Controller;

import org.example.pfebackend.Dto.AppointmentDto;
import org.example.pfebackend.Entity.Appointment;
import org.example.pfebackend.Service.AppointmentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/appointment")
@CrossOrigin("*")
public class AppointmentController {
    @Autowired
    AppointmentService service;
    @Autowired
    ModelMapper modelMapper;

    @PostMapping("/Add")
    public ResponseEntity<Object> Add(@RequestBody AppointmentDto a)  {
        ResponseEntity<Appointment> user=service.AddAppointment(a);
        if (user.getStatusCode() == HttpStatus.OK) {
            Appointment userDto = modelMapper.map(user.getBody(), Appointment.class);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        } else if (user.getStatusCode() == HttpStatus.NOT_FOUND) {
            return new ResponseEntity<>("Médecin ou patient introuvable", HttpStatus.NOT_FOUND);
        }else if (user.getStatusCode() == HttpStatus.FOUND) {
            return new ResponseEntity<>("Date pas disponible", HttpStatus.FOUND);
        }else {
            return new ResponseEntity<>("Erreur Inconnu", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/Update")
    public ResponseEntity<Object> Update(@RequestBody AppointmentDto a)  {
        ResponseEntity<Appointment> user=service.UpdateAppointment(a);
        if (user.getStatusCode() == HttpStatus.OK) {
            Appointment userDto = modelMapper.map(user.getBody(), Appointment.class);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        } else if (user.getStatusCode() == HttpStatus.NOT_FOUND) {
            return new ResponseEntity<>("Rendez-vous introuvable", HttpStatus.NOT_FOUND);
        }else if (user.getStatusCode() == HttpStatus.FOUND) {
            return new ResponseEntity<>("Date pas disponible", HttpStatus.FOUND);
        }else {
            return new ResponseEntity<>("Erreur Inconnu", HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/Pay/{id}")
    public ResponseEntity<Object> Pay(@PathVariable("id") Integer id)  {
        ResponseEntity<Appointment> user=service.PayAppointment(id);
        if (user.getStatusCode() == HttpStatus.OK) {
            Appointment userDto = modelMapper.map(user.getBody(), Appointment.class);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        }else if (user.getStatusCode() == HttpStatus.NOT_FOUND) {
            return new ResponseEntity<>("Rendez-vous introuvable", HttpStatus.NOT_FOUND);
        }else {
            return new ResponseEntity<>("Erreur Inconnu", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/ChangeStatus/{id}/{status}")
    public ResponseEntity<Object> ChangeStatus(@PathVariable("id") Integer id,@PathVariable("status") String status)  {
        ResponseEntity<Appointment> user=service.ChangeStatusAppointment(id,status);
        if (user.getStatusCode() == HttpStatus.OK) {
            Appointment userDto = modelMapper.map(user.getBody(), Appointment.class);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        }else if (user.getStatusCode() == HttpStatus.NOT_FOUND) {
            return new ResponseEntity<>("Rendez-vous introuvable", HttpStatus.NOT_FOUND);
        }else {
            return new ResponseEntity<>("Erreur Inconnu", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/All/{id}/{role}")
    public List<Appointment> retrieveAllAppointments(@PathVariable("id") Integer id,@PathVariable("role") String role) {
        return service.retrieveAllAppointment(id,role);
    }
}
