package org.example.pfebackend.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PrescriptionDto {
    private Integer idDoctor;
    private Integer idPatient;
    private Integer idPharmacy;
    private String description;
}
