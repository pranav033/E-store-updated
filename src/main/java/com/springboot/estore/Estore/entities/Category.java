package com.springboot.estore.Estore.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Table(name = "categories")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @Column(name = "Id")
    private String categoryId;

    @Column(name = "title" ,length = 30)
    private String title;

    @Column(name = "description", length = 300)
    private String categoryDesc;

    private String coverImage;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "category")
    private List<Product> products = new ArrayList<>();


}
