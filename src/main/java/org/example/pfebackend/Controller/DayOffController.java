package org.example.pfebackend.Controller;

import org.example.pfebackend.Dto.DayOffDto;
import org.example.pfebackend.Entity.DayOff;
import org.example.pfebackend.Service.DayOffService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/dayOff")
@CrossOrigin("*")
public class DayOffController {
    @Autowired
    DayOffService service;
    @Autowired
    ModelMapper modelMapper;

    @PostMapping("/Add")
    public ResponseEntity<Object> Add(@RequestBody DayOffDto d){
        ResponseEntity<DayOff> user=service.AddDayOff(d);
        if (user.getStatusCode() == HttpStatus.OK) {
            DayOff userDto = modelMapper.map(user.getBody(), DayOff.class);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        } else if (user.getStatusCode() == HttpStatus.FOUND) {
            return new ResponseEntity<>("Date existe deja", HttpStatus.FOUND);
        }else {
            return new ResponseEntity<>("Erreur Inconnu", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/Update")
    public ResponseEntity<Object> Update(@RequestBody DayOffDto d)  {
        ResponseEntity<DayOff> user=service.UpdateDayOff(d);
        if (user.getStatusCode() == HttpStatus.OK) {
            DayOff userDto = modelMapper.map(user.getBody(), DayOff.class);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        }  else if (user.getStatusCode() == HttpStatus.NOT_FOUND) {
            return new ResponseEntity<>("Jour de congé introuvable", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>("Erreur Inconnu", HttpStatus.BAD_REQUEST);

        }
    }

    @GetMapping("/{id}")
    public List<DayOff> retrieveAllDayOff(@PathVariable("id") Integer id) {
        return service.retrieveAllDaysByPatient(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object>delete (@PathVariable("id") Integer id) {
        boolean deleted = service.deleteDayOff(id);
        if (deleted) {
            return new ResponseEntity<>("Date supprimé", HttpStatus.OK);
        }
        return new ResponseEntity<>("Jour de congé introuvable", HttpStatus.NOT_FOUND);
    }
}
