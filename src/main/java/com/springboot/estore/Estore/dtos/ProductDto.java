package com.springboot.estore.Estore.dtos;

import com.springboot.estore.Estore.entities.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductDto {

    private String productId;

    @NotBlank(message = "Title cant be blank.")
    @Size(min = 4, message = "Title must be of atleast 4 chars.")
    private String title;

    @Size(min = 10, max = 10000, message = "Enter something more.")
    private String description;

    @NotNull(message = "A product price is needed.")
    private int price;

    @NotNull(message = "A product quantity is needed.")
    private int quantity;

    private int discountedPrice;

    private String productImageName;

    private CategoryDto category;

    private Date addedDate;

    @NotNull(message = "Live field is mandatory")
    private boolean live;

    @NotNull(message = "Stock field is mandatory")
    private boolean stock;
}
