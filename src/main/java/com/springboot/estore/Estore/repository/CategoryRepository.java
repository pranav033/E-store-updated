package com.springboot.estore.Estore.repository;

import com.springboot.estore.Estore.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public interface CategoryRepository extends JpaRepository<Category,String> {


}
