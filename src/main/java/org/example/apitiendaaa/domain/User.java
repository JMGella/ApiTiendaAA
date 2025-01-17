package org.example.apitiendaaa.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String name;
    @Column
    private String email;
    @Column
    private LocalDate birthDate;
    @Column
    private boolean active;
    @Column
    private String address;
    @Column
    private String phone;
    @Column
    private LocalDate creationDate;

}
