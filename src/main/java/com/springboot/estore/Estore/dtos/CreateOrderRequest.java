package com.springboot.estore.Estore.dtos;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CreateOrderRequest {


    @NotBlank(message = "User ID is required.")
    String userId;

    @NotBlank(message = "Cart ID is required.")
    String cartId;

    //delivered,dispatched,pending
    private String orderStatus = "PENDING";

    //paid,non-paid
    private String paymentStatus = "NOT PAID";


    @NotBlank(message = "Billing address is required.")
    private String billingAddress;


    @NotBlank(message = "Billing phone is required.")
    private String billingPhone;


    @NotBlank(message = "Billing name is required.")
    private String billingName;






}
