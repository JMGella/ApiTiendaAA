package org.example.apitiendaaa.domain.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.apitiendaaa.domain.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderOutDTO {
    private long id;
    private String status;
    private int total;
    private User user;

}
