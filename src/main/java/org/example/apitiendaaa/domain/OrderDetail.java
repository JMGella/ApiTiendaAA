package org.example.apitiendaaa.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="order_details")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    @Positive(message = "La cantidad debe ser mayor a 0")
    private int quantity;
    @Column(nullable = false)
    @Positive(message = "El subtotal debe ser mayor a 0")
    private float subtotal;
    @Column
    private float discount;
    @Column
    private LocalDate creationDate;

@ManyToOne
@JoinColumn(name = "product_id")
@JsonBackReference(value = "product-orderDetails")
private Product product;

@ManyToOne
@JoinColumn(name = "order_id")
@JsonBackReference(value = "order-orderDetails")
private Order order;

}
