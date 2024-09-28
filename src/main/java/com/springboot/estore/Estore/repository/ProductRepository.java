package com.springboot.estore.Estore.repository;

import com.springboot.estore.Estore.dtos.PageableResponse;
import com.springboot.estore.Estore.dtos.ProductDto;
import com.springboot.estore.Estore.entities.Category;
import com.springboot.estore.Estore.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,String> {
    Page<Product>  findByTitleContaining(String subTitle,Pageable pageable);

    Page<Product> findByLiveTrue(Pageable pageable);

    Page<Product> findByCategory(Category category,Pageable pageable);
}
