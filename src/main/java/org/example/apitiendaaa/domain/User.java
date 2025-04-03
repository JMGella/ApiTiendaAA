package org.example.apitiendaaa.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    @Email(message = "El formato de correo no es válido")
    private String email;
    @Column
    private LocalDate birthDate;
    @Column
    private Boolean active;
    @Column
    private String address;
    @Column
    @Pattern(regexp = "\\+?[0-9]+", message = "El número de telefono deben ser solo números")
    private String phone;
    @Column
    private LocalDate creationDate;
    @Column
    private String latitude;
    @Column
    private String longitude;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "user-orders")
    private List<Order> orders = new ArrayList<>();



}
