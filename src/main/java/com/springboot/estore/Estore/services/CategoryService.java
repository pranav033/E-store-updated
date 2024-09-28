package com.springboot.estore.Estore.services;

import com.springboot.estore.Estore.dtos.CategoryDto;
import com.springboot.estore.Estore.dtos.PageableResponse;


public interface CategoryService {

    //create
    CategoryDto createCategory(CategoryDto categoryDto);

    //update
    CategoryDto updateCategory(CategoryDto categoryDto, String categoryId);

    //delete
    void deleteCategory(String categoryId);

    //view all
    PageableResponse<CategoryDto> getAllCategory(int pageNumber,int pageSize, String sortBy, String sortDir);

    //view one
    CategoryDto getOneCategory(String categoryId);

}
