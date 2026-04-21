package org.example.pfebackend.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.pfebackend.Entity.Appointment;
import org.example.pfebackend.Entity.MedicalTest;
import org.example.pfebackend.Entity.Prescription;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GetPatientsDto {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private List<Appointment> appointments;
    private List<Prescription> prescriptions;
    private List<MedicalTest> medicalTests;
}
