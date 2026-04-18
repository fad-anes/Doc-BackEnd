package org.example.pfebackend.Service;

import org.example.pfebackend.Dto.AppointmentDto;
import org.example.pfebackend.Dto.NotificationDto;
import org.example.pfebackend.Entity.Appointment;
import org.example.pfebackend.Entity.Doctor;
import org.example.pfebackend.Entity.Patient;
import org.example.pfebackend.Enum.AppointmentStatus;
import org.example.pfebackend.Enum.ConsultationMode;
import org.example.pfebackend.Repository.PatientRepo;
import org.example.pfebackend.Repository.DoctorRepo;
import org.example.pfebackend.Repository.AppointmentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {
    @Autowired
    PatientRepo patientRepo;
    @Autowired
    DoctorRepo doctorRepo;
    @Autowired
    AppointmentRepo appointmentRepo;
    @Autowired
    NotificationService notificationService;

    public ResponseEntity<Appointment> AddAppointment(AppointmentDto a) {

        Optional<Patient> patientOpt = patientRepo.findById(a.getIdPatient());
        Optional<Doctor> doctorOpt = doctorRepo.findById(a.getIdDoctor());

        if (patientOpt.isEmpty() || doctorOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Patient patient = patientOpt.get();
        Doctor doctor = doctorOpt.get();

        Optional<Appointment> existDoctor = appointmentRepo
                .findByDateAndDoctor_Id(a.getDate(), a.getIdDoctor());

        Optional<Appointment> existPatient = appointmentRepo
                .findByDateAndPatient_Id(a.getDate(), a.getIdPatient());

        if (existDoctor.isPresent() || existPatient.isPresent()) {
            return new ResponseEntity<>(HttpStatus.FOUND);
        }

        Appointment appointment = new Appointment();

        appointment.setDate(a.getDate());
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentStatus(AppointmentStatus.ATT);

        ConsultationMode mode = ConsultationMode.valueOf(a.getConsultationMode().toUpperCase());
        appointment.setConsultationMode(mode);

        String message;

        if (mode == ConsultationMode.EN_LIGNE) {
            appointment.setPaid(false);
            appointment.setLienVisio("https://meet.jit.si/consultation-"
                    + doctor.getId() + "-" + patient.getId());

            message = "Le patient " + patient.getFirstName() + " " + patient.getLastName()
                    + " a pris un rendez-vous en ligne le " + appointment.getDate() + ".";

        } else {
            appointment.setPaid(true);

            message = "Le patient " + patient.getFirstName() + " " + patient.getLastName()
                    + " a pris un rendez-vous en présentiel le " + appointment.getDate() + ".";
        }

        appointmentRepo.save(appointment);

        NotificationDto notifDoctor = new NotificationDto();
        notifDoctor.setType("DOCTOR");
        notifDoctor.setMessage(message);
        notifDoctor.setIdRecever(doctor.getId());
        notificationService.AddNotification(notifDoctor);

        NotificationDto notifPatient = new NotificationDto();
        notifPatient.setType("PATIENT");
        notifPatient.setMessage(
                "Vous avez pris un rendez-vous le " + appointment.getDate()
                        + " avec le médecin " + doctor.getFirstName() + " " + doctor.getLastName() + "."
        );
        notifPatient.setIdRecever(patient.getId());
        notificationService.AddNotification(notifPatient);

        return ResponseEntity.ok(appointment);
    }

    public ResponseEntity<Appointment> PayAppointment(Integer id){
        Optional<Appointment> a=appointmentRepo.findById(id);
        if(!a.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        a.get().setPaid(true);
        appointmentRepo.save(a.get());
        return ResponseEntity.ok(a.get());
    }

    public ResponseEntity<Appointment> UpdateAppointment(AppointmentDto a){
        Optional<Appointment> appointment=appointmentRepo.findById(a.getId());
        if(!appointment.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Optional<Appointment> exist1=appointmentRepo.findByDateAndDoctor_Id(a.getDate(),a.getIdDoctor());
        Optional<Appointment> exist2=appointmentRepo.findByDateAndPatient_Id(a.getDate(),a.getIdPatient());
        if((exist1.isPresent()&&appointment.get().getId()!=exist1.get().getId()) || (exist2.isPresent()&&appointment.get().getId()!=exist2.get().getId())){
            return new ResponseEntity<>(HttpStatus.FOUND);
        }
        appointment.get().setDate(a.getDate());
        appointment.get().setConsultationMode(ConsultationMode.valueOf(a.getConsultationMode().toUpperCase()));
        if(appointment.get().getConsultationMode().toString()=="EN_LIGNE"){
            appointment.get().setPaid(false);
            appointment.get().setLienVisio("https://meet.jit.si/consultation-" + a.getIdDoctor() + "-" + a.getIdPatient());
        }else{
            appointment.get().setPaid(true);
        }
        appointmentRepo.save(appointment.get());

        NotificationDto notifDoctor = new NotificationDto();
        notifDoctor.setType("DOCTOR");
        notifDoctor.setMessage("le rendez-vous avec l'id"+" "+appointment.get().getId()+" a ete changer");
        notifDoctor.setIdRecever(appointment.get().getDoctor().getId());
        notificationService.AddNotification(notifDoctor);

        NotificationDto notifPatient = new NotificationDto();
        notifPatient.setType("PATIENT");
        notifPatient.setMessage("le rendez-vous avec l'id"+" "+appointment.get().getId()+" a ete changer");
        notifPatient.setIdRecever(appointment.get().getPatient().getId());
        notificationService.AddNotification(notifPatient);
        return ResponseEntity.ok(appointment.get());
    }

    public ResponseEntity<Appointment> ChangeStatusAppointment(Integer id,String status){
        Optional<Appointment> appointment=appointmentRepo.findById(id);
        if(!appointment.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        appointment.get().setAppointmentStatus(AppointmentStatus.valueOf(status.toUpperCase()));
        appointmentRepo.save(appointment.get());
        return ResponseEntity.ok(appointment.get());
    }

    public List<Appointment> retrieveAllAppointment(Integer id,String role){
        if(role.equals("DOCTOR")){
            return appointmentRepo.findByDoctor_Id(id);
        }
        return appointmentRepo.findByPatient_Id(id);
    }
}
