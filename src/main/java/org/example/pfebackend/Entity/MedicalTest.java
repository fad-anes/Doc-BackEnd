package org.example.pfebackend.Entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.pfebackend.Enum.Status;

import java.io.Serializable;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class MedicalTest implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String description;
    private LocalDate date;
    private String result;
    @Enumerated(EnumType.STRING)
    private Status testStatus;
    @ManyToOne
    @JsonBackReference
    private Doctor doctor;
    @ManyToOne
    @JsonBackReference
    private Patient patient;
    @ManyToOne
    @JsonBackReference
    private Laboratory laboratory;
}
