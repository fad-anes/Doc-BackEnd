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

    @PostMapping
    public ResponseEntity<Object> Add(@RequestBody NotificationDto n)  {
        ResponseEntity<Notification> user=service.AddNotification(n);
        if (user.getStatusCode() == HttpStatus.OK) {
            Notification userDto = modelMapper.map(user.getBody(), Notification.class);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        }
        return new ResponseEntity<>("Erreur Inconnu", HttpStatus.BAD_REQUEST);

    }
}
