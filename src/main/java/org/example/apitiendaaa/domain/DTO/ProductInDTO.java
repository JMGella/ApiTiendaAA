package org.example.apitiendaaa.domain.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductInDTO {
    @NotBlank
    private String name;
    private String description;
    private Float price;
    private LocalDate creationDate;
    private Boolean active;
}
