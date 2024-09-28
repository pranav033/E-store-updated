package com.springboot.estore.Estore.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddItemtoCartRequest {

    @NotBlank(message = "Enter the product ID.")
    private String productId;

    @NotNull(message = "Enter a valid quantity.")
    private int quantity;
}
