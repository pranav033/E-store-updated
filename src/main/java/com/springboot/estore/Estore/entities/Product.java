package com.springboot.estore.Estore.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    private String productId;

    @Column(name = "title" ,length = 30)
    private String title;

    @Column(name = "description",length = 10000)
    private String description;

    private int price;

    private int quantity;

    private int discountedPrice;

    private Date addedDate;

    private String productImageName;

    @JoinColumn(name = "category_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Category category;

    private boolean live;

    private boolean stock;


}
