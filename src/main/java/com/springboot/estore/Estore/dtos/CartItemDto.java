package com.springboot.estore.Estore.dtos;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDto {

    private int cartItemId;

    private int quantity;


    private ProductDto product;

    private int totalPrice;



}
