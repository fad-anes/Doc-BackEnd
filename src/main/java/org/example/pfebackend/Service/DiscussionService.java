package org.example.pfebackend.Service;

import org.example.pfebackend.Entity.Discussion;
import org.example.pfebackend.Entity.Doctor;
import org.example.pfebackend.Entity.Patient;
import org.example.pfebackend.Repository.PatientRepo;
import org.example.pfebackend.Repository.DoctorRepo;
import org.example.pfebackend.Repository.DiscussionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DiscussionService {
    @Autowired
    PatientRepo patientRepo;
    @Autowired
    DoctorRepo doctorRepo;
    @Autowired
    DiscussionRepo discussionRepo;

    public Discussion CreateDiscussion(Integer idPatient, Integer idDoctor){
        Optional<Doctor>doctor=doctorRepo.findById(idDoctor);
        Optional<Patient>patient=patientRepo.findById(idPatient);
        if(doctor.isPresent() && patient.isPresent()){
            Discussion discussion=new Discussion();
            discussion.setDoctor(doctor.get());
            discussion.setPatient(patient.get());
            return discussionRepo.save(discussion);
        }
        return null;
    }

    public List<Discussion> GetAllDiscussions(Integer id, String role){
        if(role.equals("DOCTOR")){
            return discussionRepo.findByDoctor_Id(id);
        }
        return discussionRepo.findByPatient_Id(id);
    }
}
