package org.example.apitiendaaa.domain.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryOutDTO {
    private long id;
    private String name;
    private String description;
    private Boolean active;
    private LocalDate creationDate;
}
