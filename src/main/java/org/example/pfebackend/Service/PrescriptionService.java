package org.example.pfebackend.Service;

import org.example.pfebackend.Dto.NotificationDto;
import org.example.pfebackend.Dto.PrescriptionDto;
import org.example.pfebackend.Entity.*;
import org.example.pfebackend.Enum.Status;
import org.example.pfebackend.Repository.DoctorRepo;
import org.example.pfebackend.Repository.PharmacyRepo;
import org.example.pfebackend.Repository.PrescriptionRepo;
import org.example.pfebackend.Repository.PatientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PrescriptionService {
    @Autowired
    DoctorRepo doctorRepo;
    @Autowired
    PatientRepo patientRepo;
    @Autowired
    PharmacyRepo pharmacyRepo;
    @Autowired
    PrescriptionRepo prescriptionRepo;
    @Autowired
    NotificationService notificationService;

    public ResponseEntity<Prescription> AddPrescription(PrescriptionDto p){
        Optional<Patient> patient=patientRepo.findById(p.getIdPatient());
        Optional<Doctor> doctor=doctorRepo.findById(p.getIdDoctor());
        Optional<Pharmacy> pharmacy=pharmacyRepo.findById(p.getIdPharmacy());
        if(!patient.isPresent() || !doctor.isPresent() || !pharmacy.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Prescription prescription=new Prescription();
        prescription.setPatient(patient.get());
        prescription.setDoctor(doctor.get());
        prescription.setPharmacy(pharmacy.get());
        prescription.setDescription(p.getDescription());
        prescription.setPrescriptionStatus(Status.CREATED);
        prescriptionRepo.save(prescription);

        NotificationDto notifPatient = new NotificationDto();
        notifPatient.setType("PATIENT");
        notifPatient.setMessage(
                "le médecin" + doctor.get().getFirstName() + " " + doctor.get().getLastName() + "."
                        + " a creer une ordonnance a realiser par la pharmacie" + pharmacy.get().getName() + ".");
        notifPatient.setIdRecever(patient.get().getId());
        notificationService.AddNotification(notifPatient);

        NotificationDto notifPharmacy = new NotificationDto();
        notifPharmacy.setType("LABORATORY");
        notifPharmacy.setMessage(
                "le médecin" + doctor.get().getFirstName() + " " + doctor.get().getLastName() + "."
                        + " a creer une ordonnance a realiser pour le patient" + patient.get().getFirstName() + " " + patient.get().getLastName() + ".");
        notifPharmacy.setIdRecever(pharmacy.get().getId());
        notificationService.AddNotification(notifPharmacy);
        return ResponseEntity.ok(prescription);
    }

    public ResponseEntity<Prescription> ChangeStatus(Integer id) {
        Optional<Prescription> prescription=prescriptionRepo.findById(id);
        if(!prescription.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        prescription.get().setDate(LocalDate.now());
        prescription.get().setPrescriptionStatus(Status.READY);
        prescriptionRepo.save(prescription.get());

        NotificationDto notifPatient = new NotificationDto();
        notifPatient.setType("PATIENT");
        notifPatient.setMessage("l'ordonnance avec l'id" + prescription.get().getId()
                + "est prêt" + ".");
        notifPatient.setIdRecever(prescription.get().getPatient().getId());
        notificationService.AddNotification(notifPatient);
        return ResponseEntity.ok(prescription.get());
    }

    public List<Prescription> retrieveAllPrescription(Integer id, String role){
        if(role.equals("PATIENT")){
            return prescriptionRepo.findByPatient_Id(id);
        }else if(role.equals("PHARMACY")){
            return prescriptionRepo.findByPharmacy_Id(id);
        }
        return prescriptionRepo.findByDoctor_Id(id);
    }
}
