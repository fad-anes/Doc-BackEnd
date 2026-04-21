package org.example.pfebackend.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DashboardDto {
    private long nbrDoctors;
    private long nbrPatients;
    private long nbrLabs;
    private long nbrPharms;
    private long nbrAppointments;
    private long nbrPrescriptions;
    private long nbrMedicalTests;
}
