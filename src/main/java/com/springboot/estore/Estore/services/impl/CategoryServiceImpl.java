package com.springboot.estore.Estore.services.impl;

import com.springboot.estore.Estore.dtos.CategoryDto;
import com.springboot.estore.Estore.dtos.PageableResponse;
import com.springboot.estore.Estore.entities.Category;
import com.springboot.estore.Estore.exceptions.ResourceNotFoundException;
import com.springboot.estore.Estore.helpers.Helper;
import com.springboot.estore.Estore.repository.CategoryRepository;
import com.springboot.estore.Estore.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${categories.image.path}")
    private String categoryImageUploadPath;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        String id = UUID.randomUUID().toString();
        categoryDto.setCategoryId(id);
        Category category = modelMapper.map(categoryDto, Category.class);
        Category savedCategory = categoryRepository.save(category);
        return categoryDto;
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, String categoryId) {

        //get category of the id provided
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found exception."));

        //update the found category
        category.setTitle(categoryDto.getTitle());
        category.setCategoryDesc(categoryDto.getCategoryDesc());
        category.setCoverImage(categoryDto.getCoverImage());
        Category updatedCategory = categoryRepository.save(category);
        return modelMapper.map(updatedCategory, CategoryDto.class);
    }

    @Override
    public void deleteCategory(String categoryId) {

        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found exception."));
        String imageName = category.getCoverImage();
        String fullImagePath = categoryImageUploadPath + imageName;
        Path path = Paths.get(fullImagePath);
        try {
            Files.delete(path);
        } catch (NoSuchFileException ex){
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        categoryRepository.delete(category);

    }

    @Override
    public PageableResponse<CategoryDto> getAllCategory(int pageNumber,int pageSize, String sortBy, String sortDir) {

        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        PageableResponse<CategoryDto> pageableResponse = Helper.getPageableResponse(categoryPage, CategoryDto.class);
        return pageableResponse;
    }

    @Override
    public CategoryDto getOneCategory(String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found exception."));
        return modelMapper.map(category,CategoryDto.class);
    }
}
