package org.example.apitiendaaa.domain.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserOutDTO {
    private long id;
    private String name;
    private String email;
    private String birthDate;
    private boolean active;
    private String address;
    private String phone;
    private String creationDate;

}
