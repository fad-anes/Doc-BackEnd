package org.example.pfebackend.Service;

import org.example.pfebackend.Dto.MedicalTestDto;
import org.example.pfebackend.Dto.NotificationDto;
import org.example.pfebackend.Entity.Doctor;
import org.example.pfebackend.Entity.Laboratory;
import org.example.pfebackend.Entity.MedicalTest;
import org.example.pfebackend.Entity.Patient;
import org.example.pfebackend.Enum.Status;
import org.example.pfebackend.Repository.DoctorRepo;
import org.example.pfebackend.Repository.PatientRepo;
import org.example.pfebackend.Repository.LaboratoryRepo;
import org.example.pfebackend.Repository.MedicalTestRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MedicalTestService {
    @Autowired
    DoctorRepo doctorRepo;
    @Autowired
    PatientRepo patientRepo;
    @Autowired
    LaboratoryRepo laboratoryRepo;
    @Autowired
    MedicalTestRepo medicalTestRepo;
    @Autowired
    UploadFileService uploadFileService;
    @Autowired
    NotificationService notificationService;

    public ResponseEntity<MedicalTest> AddMedicalTest(MedicalTestDto m){
        Optional<Patient> patient=patientRepo.findById(m.getIdPatient());
        Optional<Doctor> doctor=doctorRepo.findById(m.getIdDoctor());
        Optional<Laboratory> laboratory=laboratoryRepo.findById(m.getIdLaboratory());
        if(!patient.isPresent() || !doctor.isPresent() || !laboratory.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        MedicalTest medicalTest=new MedicalTest();
        medicalTest.setPatient(patient.get());
        medicalTest.setDoctor(doctor.get());
        medicalTest.setLaboratory(laboratory.get());
        medicalTest.setDescription(m.getDescription());
        medicalTest.setTestStatus(Status.CREATED);
        medicalTestRepo.save(medicalTest);

        NotificationDto notifPatient = new NotificationDto();
        notifPatient.setType("PATIENT");
        notifPatient.setMessage(
                "le médecin" + doctor.get().getFirstName() + " " + doctor.get().getLastName() + "."
                        + " a creer un analyse medicale a realiser par la laboratoire" + laboratory.get().getName() + ".");
        notifPatient.setIdRecever(patient.get().getId());
        notificationService.AddNotification(notifPatient);

        NotificationDto notifLab = new NotificationDto();
        notifLab.setType("LABORATORY");
        notifLab.setMessage(
                "le médecin" + doctor.get().getFirstName() + " " + doctor.get().getLastName() + "."
                        + " a creer un analyse medicale a realiser pour le patient" + patient.get().getFirstName() + " " + patient.get().getLastName() + ".");
        notifLab.setIdRecever(laboratory.get().getId());
        notificationService.AddNotification(notifLab);
        return ResponseEntity.ok(medicalTest);
    }

    public ResponseEntity<MedicalTest> ResultMedicalTest(Integer id, MultipartFile file) throws IOException {
        Optional<MedicalTest> medicalTest=medicalTestRepo.findById(id);
        if(!medicalTest.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        String fileName=uploadFileService.uploadFile(file);
        medicalTest.get().setResult(fileName);
        medicalTest.get().setDate(LocalDate.now());
        medicalTest.get().setTestStatus(Status.READY);
        medicalTestRepo.save(medicalTest.get());

        NotificationDto notifPatient = new NotificationDto();
        notifPatient.setType("PATIENT");
        notifPatient.setMessage("l'analyse medicale avec l'id" + medicalTest.get().getId()
                        + "est prêt" + ".");
        notifPatient.setIdRecever(medicalTest.get().getPatient().getId());
        notificationService.AddNotification(notifPatient);

        NotificationDto notifDOCTOR = new NotificationDto();
        notifDOCTOR.setType("DOCTOR");
        notifDOCTOR.setMessage("l'analyse medicale avec l'id" + medicalTest.get().getId()
                + "est prêt" + ".");
        notifDOCTOR.setIdRecever(medicalTest.get().getDoctor().getId());
        notificationService.AddNotification(notifDOCTOR);
        return ResponseEntity.ok(medicalTest.get());
    }
    public List<MedicalTest> retrieveAllMedicalTest(Integer id,String role){
       if(role.equals("PATIENT")){
           return medicalTestRepo.findByPatient_Id(id);
       }else if(role.equals("LABORATORY")){
           return medicalTestRepo.findByLaboratory_Id(id);
       }
        return medicalTestRepo.findByDoctor_Id(id);
    }
}
