package org.example.pfebackend.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AppointmentDto {
    private Integer id;
    private String  date;
    private String consultationMode;;
    private Integer idPatient;
    private Integer idDoctor;
}
