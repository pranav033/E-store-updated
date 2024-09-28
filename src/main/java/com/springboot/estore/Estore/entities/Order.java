package com.springboot.estore.Estore.entities;


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
@Entity
@Table(name = "orders")
public class Order {

    @Id
    private String orderId;

    //delivered,dispatched,pending
    private String orderStatus;


    //paid,non-paid
    private String paymentStatus;

    private long orderAmount;

    @Column(length = 1000)
    private String billingAddress;


    private String billingPhone;

    private String billingName;

    private Date orderedDate;

    private Date deliveredDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL,mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();

}
