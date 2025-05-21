package org.example.apitiendaaa.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    @NotBlank
    private String status;
    @Column
    private Float total;
    @Column(nullable = false)
    private String address;
    @Column
    private LocalDate creationDate;
    @Column
    private String paymentMethod;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference(value = "user-orders")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "order-orderDetails")
    private List<OrderDetail> orderDetails = new ArrayList<>();



}
