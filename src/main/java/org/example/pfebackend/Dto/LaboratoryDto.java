package org.example.pfebackend.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LaboratoryDto {
    private Integer id;
    private String name;
    private String address;
    private String email;
    private String password;
    private String phone;
    private MultipartFile file;
}
