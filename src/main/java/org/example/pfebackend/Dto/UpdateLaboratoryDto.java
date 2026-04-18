package org.example.pfebackend.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateLaboratoryDto {
    private Integer id;
    private String name;
    private String address;
    private String email;
    private String password;
    private String phone;
}
