package com.springboot.estore.Estore.services.impl;

import com.springboot.estore.Estore.dtos.PageableResponse;
import com.springboot.estore.Estore.dtos.ProductDto;
import com.springboot.estore.Estore.entities.Category;
import com.springboot.estore.Estore.entities.Product;
import com.springboot.estore.Estore.exceptions.ResourceNotFoundException;
import com.springboot.estore.Estore.helpers.Helper;
import com.springboot.estore.Estore.repository.CategoryRepository;
import com.springboot.estore.Estore.repository.ProductRepository;
import com.springboot.estore.Estore.services.ProductService;
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
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${product.image.path}")
    private String productImageUploadPath;

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        String productId = UUID.randomUUID().toString();

        productDto.setProductId(productId);
        productDto.setAddedDate(new Date());
        Product product = modelMapper.map(productDto, Product.class);
        productRepository.save(product);
        return productDto;
    }

    @Override
    public void deleteProduct(String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("No product found."));
        String imageName = product.getProductImageName();
        String fullImagePath = productImageUploadPath + imageName;
        Path path = Paths.get(fullImagePath);
        try {
            Files.delete(path);
        } catch (NoSuchFileException ex){
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        productRepository.delete(product);

    }

    @Override
    public PageableResponse<ProductDto> getAllProducts(int pageNumber,int pageSize,String sortBy, String sortDir) {

        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> products = productRepository.findAll(pageable);
        PageableResponse<ProductDto> pageableResponse = Helper.getPageableResponse(products, ProductDto.class);
        return pageableResponse;
    }

    @Override
    public ProductDto getOneProduct(String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found."));
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        return productDto;
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto, String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found."));
        //ProductDto productDto1 = modelMapper.map(product, ProductDto.class);
        product.setTitle(productDto.getTitle());
        product.setLive(productDto.isLive());
        product.setPrice(productDto.getPrice());
        product.setDescription(productDto.getDescription());
        product.setStock(productDto.isStock());
        product.setQuantity(productDto.getQuantity());
        product.setAddedDate(productDto.getAddedDate());
        product.setDiscountedPrice(productDto.getDiscountedPrice());
        product.setProductImageName(productDto.getProductImageName());
        Product saved = productRepository.save(product);
        ProductDto productDto1 = modelMapper.map(saved, ProductDto.class);
        return productDto1;

    }

    @Override
    public PageableResponse<ProductDto> getLiveProducts(int pageNumber,int pageSize,String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> products = productRepository.findByLiveTrue(pageable);
        PageableResponse<ProductDto> pageableResponse = Helper.getPageableResponse(products, ProductDto.class);
        return pageableResponse;
    }

    @Override
    public PageableResponse<ProductDto> getProductWithTitleContaining(String subTitle,int pageNumber,int pageSize,String sortBy, String sortDir) {

        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> products = productRepository.findByTitleContaining(subTitle,pageable);
        PageableResponse<ProductDto> pageableResponse = Helper.getPageableResponse(products, ProductDto.class);
        return pageableResponse;
    }

    @Override
    public ProductDto createWithCategory(ProductDto productDto, String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found."));
        String productId = UUID.randomUUID().toString();

        productDto.setProductId(productId);
        productDto.setAddedDate(new Date());
        Product product = modelMapper.map(productDto, Product.class);
        product.setCategory(category);
        productRepository.save(product);
        return productDto;
    }

    @Override
    public ProductDto updateCategory(String productId, String categoryId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product with this ID not found"));
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category with given ID not found"));
        product.setCategory(category);
        Product product1 = productRepository.save(product);
        return modelMapper.map(product1, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAllProductsofCategory(String categoryId,int pageNumber,int pageSize,String sortBy,String sortDir) {

        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category with given ID not found"));
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> page = productRepository.findByCategory(category,pageable);
        return Helper.getPageableResponse(page, ProductDto.class);
    }
}
