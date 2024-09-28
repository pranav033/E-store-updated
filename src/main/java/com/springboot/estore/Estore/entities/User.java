package com.springboot.estore.Estore.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name="users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @Column(name = "user_id")
    private String user_id;

    @Column(name = "user_name")
    private String name;

    @Column(name = "user_email",unique = true)
    private String email;

    @Column(name = "user_password")
    private String password;


    @Column(name = "user_gender")
    private String gender;

    @Column(name = "user_about",length = 1000)
    private String about;

    @Column(name = "user_image_name")
    private String image_name;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.REMOVE,mappedBy = "user")
    private List<Order> orders = new ArrayList<>();


}
