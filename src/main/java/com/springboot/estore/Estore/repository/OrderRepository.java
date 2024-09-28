package com.springboot.estore.Estore.repository;

import com.springboot.estore.Estore.entities.Order;
import com.springboot.estore.Estore.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,String> {

    Page<Order> findByUser(User user, Pageable pageable);
}
