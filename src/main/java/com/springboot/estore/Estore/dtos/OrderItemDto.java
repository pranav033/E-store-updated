package com.springboot.estore.Estore.dtos;

import com.springboot.estore.Estore.entities.Order;
import com.springboot.estore.Estore.entities.Product;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OrderItemDto {

    private int orderItemId;

    private int quantity;

    private int totalPrice;

    private ProductDto product;

}
