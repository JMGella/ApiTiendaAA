package org.example.apitiendaaa.domain.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductInDTO {
    private String name;
    private String description;
    private Float price;
    private LocalDate creationDate;
    private Boolean active;
}
