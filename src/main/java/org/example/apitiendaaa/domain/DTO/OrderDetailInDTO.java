package org.example.apitiendaaa.domain.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailInDTO {

    @NotNull
    private int quantity;
    private Float discount;
    @NotNull
    private Long productId;
}
