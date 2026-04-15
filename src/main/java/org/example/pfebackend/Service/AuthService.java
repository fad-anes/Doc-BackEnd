package org.example.pfebackend.Service;

import org.example.pfebackend.Dto.UserWrapper;
import org.example.pfebackend.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    PatientRepo patientRepo;
    @Autowired
    PharmacyRepo pharmacyRepo;
    @Autowired
    LaboratoryRepo laboratoryRepo;
    @Autowired
    AdminRepo adminRepo;
    @Autowired
    DoctorRepo doctorRepo;

    public UserWrapper getUserByEmail(String email) {
        return doctorRepo.findByEmail(email)
                .map(d -> new UserWrapper("DOCTOR", d))
                .orElseGet(() ->
                        patientRepo.findByEmail(email)
                                .map(p -> new UserWrapper("PATIENT", p))
                                .orElseGet(() ->
                                        pharmacyRepo.findByEmail(email)
                                                .map(ph -> new UserWrapper("PHARMACY", ph))
                                                .orElseGet(() ->
                                                        laboratoryRepo.findByEmail(email)
                                                                .map(l -> new UserWrapper("LABORATORY", l))
                                                                .orElseGet(() ->
                                                                        adminRepo.findByEmail(email)
                                                                                .map(l -> new UserWrapper("ADMIN", l))
                                                                                .orElse(null)

                                                                )
                                                )
                                )
                );
    }
}
