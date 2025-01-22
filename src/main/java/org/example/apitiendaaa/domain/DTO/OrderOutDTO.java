package org.example.apitiendaaa.domain.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderOutDTO {
    private long id;
    private String status;
    private Float total;
    private int userId;
    private String address;
    private String creationDate;
    private String paymentMethod;


}
