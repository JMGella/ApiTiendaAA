package org.example.apitiendaaa.domain.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.apitiendaaa.domain.Order;
import org.example.apitiendaaa.domain.Product;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailOutDTO {

    private Product product;
    private Order order;

    private int quantity;

    private float subtotal;

    private float discount;
}
