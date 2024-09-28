package com.springboot.estore.Estore.repository;

import com.springboot.estore.Estore.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem,Integer> {

}
