package org.example.apitiendaaa.domain;

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
@Entity(name="categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String description;
    @Column
    private LocalDate creationDate;
    @Column
    private boolean active;
    @Column
    private String image;

    @OneToMany(mappedBy = "category")
    @JsonManagedReference(value = "category-products")
    private List<Product> products = new ArrayList<>();
}
