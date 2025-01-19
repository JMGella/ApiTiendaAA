package org.example.apitiendaaa.domain.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.apitiendaaa.domain.Category;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductOutDTO {
    private String name;
    private float price;
    private Category category;

}
