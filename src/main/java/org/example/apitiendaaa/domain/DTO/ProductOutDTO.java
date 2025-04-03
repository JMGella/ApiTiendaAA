package org.example.apitiendaaa.domain.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductOutDTO {
    private long id;
    private String name;
    private float price;
    private long categoryId;
    private LocalDate creationDate;
    private String description;
}
