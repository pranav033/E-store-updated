package com.springboot.estore.Estore.services;

import com.springboot.estore.Estore.dtos.PageableResponse;
import com.springboot.estore.Estore.dtos.ProductDto;

public interface ProductService {

    //create
    ProductDto createProduct(ProductDto productDto);

    //delete
    void deleteProduct(String productId);

    //get all
    PageableResponse<ProductDto> getAllProducts(int pageNumber,int pageSize, String sortBy, String sortDir);

    //get single
    ProductDto getOneProduct(String productId);

    //update
    ProductDto updateProduct(ProductDto productDto,  String productId);

    // get live
    PageableResponse<ProductDto> getLiveProducts(int pageNumber,int pageSize,String sortBy, String sortDir);


    // get with title containing
    PageableResponse<ProductDto> getProductWithTitleContaining(String subTitle, int pageNumber,int pageSize,String sortBy, String sortDir);

    ProductDto createWithCategory(ProductDto productDto,String categoryId);

    ProductDto updateCategory(String productId,String categoryId);

    PageableResponse<ProductDto> getAllProductsofCategory(String categoryId,int pageNumber,int pageSize,String sortBy,String sortDir);
}
