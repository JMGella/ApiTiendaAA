package org.example.apitiendaaa.domain.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailOutDTO {

    private long id;
    private long orderId;
    private long productId;
    private String productName;
    private int quantity;
    private float discount;
    private float subtotal;

}
