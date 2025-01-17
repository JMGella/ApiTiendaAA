package org.example.apitiendaaa.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String status;
    @Column
    private int total;
    @Column
    private String address;
    @Column
    private LocalDate creationDate;
    @Column
    private String paymentMethod;



}
