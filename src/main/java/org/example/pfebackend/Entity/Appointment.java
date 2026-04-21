package org.example.pfebackend.Entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.pfebackend.Enum.AppointmentStatus;
import org.example.pfebackend.Enum.ConsultationMode;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Appointment implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDateTime date;
    private String lienVisio;
    @Enumerated(EnumType.STRING)
    private ConsultationMode consultationMode;
    @Enumerated(EnumType.STRING)
    private AppointmentStatus appointmentStatus;
    private boolean paid = false;
    @ManyToOne
    @JsonManagedReference
    private Doctor doctor;
    @ManyToOne
    @JsonManagedReference
    private Patient patient;
}
