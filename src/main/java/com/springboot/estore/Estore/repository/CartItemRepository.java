package com.springboot.estore.Estore.repository;

import com.springboot.estore.Estore.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem,Integer> {
}
