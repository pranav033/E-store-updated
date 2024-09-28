package com.springboot.estore.Estore.controllers;

import com.springboot.estore.Estore.dtos.*;
import com.springboot.estore.Estore.services.CategoryService;
import com.springboot.estore.Estore.services.FileService;
import com.springboot.estore.Estore.services.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Value("${categories.image.path}")
    private String categoryImageUploadPath;

    @Autowired
    private FileService fileService;
    //create
    @PostMapping("/create")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto)
    {
        CategoryDto category = categoryService.createCategory(categoryDto);
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    // update
    @PutMapping("/update/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto, @PathVariable String categoryId)
    {
        CategoryDto updatedCategory = categoryService.updateCategory(categoryDto, categoryId);
        return new ResponseEntity<>(updatedCategory,HttpStatus.OK);
    }

    // delete
    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable String categoryId)
    {
        categoryService.deleteCategory(categoryId);
        ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
                .message("Category deleted")
                .status(HttpStatus.OK)
                .success(true)
                .build();
        return new ResponseEntity<>(apiResponseMessage,HttpStatus.OK);
    }


    // get all
    @GetMapping("/getall")
    public ResponseEntity<PageableResponse<CategoryDto>> getAllCategory(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "2  ",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc" ,required = false) String sortDir
    )
    {
        PageableResponse<CategoryDto> allCategory = categoryService.getAllCategory(pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(allCategory,HttpStatus.OK);
    }


    // get single
    @GetMapping("/getone/{categoryId}")
    public ResponseEntity<CategoryDto> getSingleCategory(@PathVariable String categoryId)
    {
        CategoryDto category = categoryService.getOneCategory(categoryId);
        return ResponseEntity.ok(category);
    }


    @PostMapping("/image/{categoryId}")
    public ResponseEntity<ImageResponse> uploadUserImage(
            @RequestParam("categoryImage") MultipartFile image,
            @PathVariable("categoryId") String categoryId
    ) throws IOException {
        String uploadedFile = fileService.uploadFile(image, categoryImageUploadPath);
        CategoryDto categoryDto = categoryService.getOneCategory(categoryId);
        categoryDto.setCoverImage(uploadedFile);
        categoryService.updateCategory(categoryDto,categoryId);
        ImageResponse imageResponse = ImageResponse.builder().imageName(uploadedFile).success(true).status(HttpStatus.CREATED).message("Image uploaded successfully!").build();
        return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);
    }


    @GetMapping("/serveimage/{categoryId}")
    public void serveUserImage(@PathVariable("categoryId") String categoryId, HttpServletResponse response) throws IOException {

        CategoryDto categoryDto = categoryService.getOneCategory(categoryId);
        logger.info("user image name : "+categoryDto.getCoverImage());
        InputStream resource = fileService.getResource(categoryImageUploadPath, categoryDto.getCoverImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
    }


    @PostMapping("createproduct/{categoryId}/product")
    public ResponseEntity<ProductDto> createProductWithCategory(
            @PathVariable("categoryId") String categoryId,
            @Valid @RequestBody ProductDto productDto
    ){
        ProductDto productDto1 = productService.createWithCategory(productDto, categoryId);
        return new ResponseEntity<>(productDto1,HttpStatus.CREATED);
    }

    @PutMapping("updateproduct/{categoryId}/product/{productId}")
    public ResponseEntity<ProductDto> updateCategoryofProduct(
            @PathVariable("categoryId") String categoryId,
            @PathVariable("productId") String productId
    ){
        ProductDto productDto = productService.updateCategory(productId, categoryId);
        return new ResponseEntity<>(productDto,HttpStatus.OK);
    }

    @GetMapping("getproductsofcategory/{categoryId}/products")
    public ResponseEntity<PageableResponse<ProductDto>> getProductsofCategory(@PathVariable("categoryId") String categoryId,
                                                                              @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
                                                                              @RequestParam(value = "pageSize",defaultValue = "2",required = false) int pageSize,
                                                                              @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy,
                                                                              @RequestParam(value = "sortDir",defaultValue = "asc" ,required = false) String sortDir)
    {
        PageableResponse<ProductDto> allProductsofCategory = productService.getAllProductsofCategory(categoryId,pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(allProductsofCategory,HttpStatus.OK);
    }
}
