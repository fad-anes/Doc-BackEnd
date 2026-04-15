package org.example.pfebackend.Service;

import org.example.pfebackend.Dto.DayOffDto;
import org.example.pfebackend.Entity.DayOff;
import org.example.pfebackend.Repository.DayOffRepo;
import org.example.pfebackend.Repository.DoctorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DayOffService {
    @Autowired
    DayOffRepo dayOffRepo;
    @Autowired
    DoctorRepo doctorRepo;

    public ResponseEntity<DayOff> AddDayOff(DayOffDto d) {
        boolean exist = dayOffRepo.findByDateOffAndDoctor_Id(d.getDateOff(),d.getId());
        if(exist) {
            return new ResponseEntity<>(HttpStatus.FOUND);
        }
        DayOff dayOff = new DayOff();
        dayOff.setDateOff(d.getDateOff());
        dayOff.setDoctor(doctorRepo.findById(d.getId()).get());
        dayOffRepo.save(dayOff);
        return ResponseEntity.ok(dayOff);
    }
    public ResponseEntity<DayOff> UpdateDayOff(DayOffDto d) {
        DayOff dayOff = dayOffRepo.findById(d.getId()).get();
        if(dayOff==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        dayOff.setDateOff(d.getDateOff());
        dayOffRepo.save(dayOff);
        return ResponseEntity.ok(dayOff);
    }

    public List<DayOff> retrieveAllDaysByPatient(Integer id){
        return dayOffRepo.findByDoctor_Id(id);
    }
    public Boolean deleteDayOff(Integer id) {
        Optional<DayOff> dayOff = dayOffRepo.findById(id);
        if (dayOff.isPresent()) {
            dayOffRepo.delete(dayOff.get());
            return true;
        }
        return false;
    }

}
