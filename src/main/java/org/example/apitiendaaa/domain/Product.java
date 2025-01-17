package org.example.apitiendaaa.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String name;
    @Column
    private String description;
    @Column(nullable = false)
    private float price;
    @Column
    private LocalDate creationDate;
    @Column
    private boolean active;


@ManyToOne
@JoinColumn(name = "category_id")
@JsonBackReference(value = "category-products")
private Category category;

@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
@JsonManagedReference(value = "product-orderDetails")
private List<OrderDetail> orderDetails = new ArrayList<>();


}
