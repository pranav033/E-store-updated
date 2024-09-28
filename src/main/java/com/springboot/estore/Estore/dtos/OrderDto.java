package com.springboot.estore.Estore.dtos;

import com.springboot.estore.Estore.entities.OrderItem;
import com.springboot.estore.Estore.entities.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OrderDto {


    private String orderId;

    //delivered,dispatched,pending
    private String orderStatus = "PENDING";


    //paid,non-paid
    private String paymentStatus = "NOT PAID";

    private long orderAmount;


    private String billingAddress;


    private String billingPhone;

    private String billingName;

    private Date orderedDate = new Date();

    private Date deliveredDate;


    private List<OrderItemDto> orderItems = new ArrayList<>();
}
