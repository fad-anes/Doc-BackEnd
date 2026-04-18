package org.example.pfebackend.Controller;

import org.example.pfebackend.Dto.NotificationDto;
import org.example.pfebackend.Dto.PatientDto;
import org.example.pfebackend.Entity.Notification;
import org.example.pfebackend.Entity.Patient;
import org.example.pfebackend.Service.NotificationService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/notification")
@CrossOrigin("*")
public class NotificationController {
    @Autowired
    NotificationService service;
    @Autowired
    ModelMapper modelMapper;

    @GetMapping("/{id}/{type}")
    public List<Notification> getAllNotificationsByUser(@PathVariable("id") Integer id, @PathVariable("type") String type) {
        return service.getAllNotificationsByUser(id,type);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> MarkSeen (@PathVariable("id") Integer id) {
        boolean deleted = service.MarkSeen(id);
        if (deleted) {
            return new ResponseEntity<>("Seen", HttpStatus.OK);
        }
        return new ResponseEntity<>("Non trouvé", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> Delete (@PathVariable("id") Integer id) {
        boolean deleted = service.delete(id);
        if (deleted) {
            return new ResponseEntity<>("deleted", HttpStatus.OK);
        }
        return new ResponseEntity<>("Non trouvé", HttpStatus.NOT_FOUND);
    }
}
