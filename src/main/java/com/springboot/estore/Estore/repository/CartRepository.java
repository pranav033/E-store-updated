package com.springboot.estore.Estore.repository;

import com.springboot.estore.Estore.entities.Cart;
import com.springboot.estore.Estore.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,String> {

    Optional<Cart> findByUser(User user);
}
