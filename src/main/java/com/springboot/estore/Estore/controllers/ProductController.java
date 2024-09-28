package com.springboot.estore.Estore.controllers;

import com.springboot.estore.Estore.dtos.*;
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
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private FileService fileService;

    @Value("${product.image.path}")
    private String productImageUploadPath;

    Logger logger = LoggerFactory.getLogger(ProductController.class);

    @PostMapping("/create")
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto productDto)
    {
        ProductDto product = productService.createProduct(productDto);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@Valid @RequestBody ProductDto productDto, @PathVariable String productId)
    {
        ProductDto productDto1 = productService.updateProduct(productDto, productId);
        return new ResponseEntity<>(productDto1,HttpStatus.OK);
    }


    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<ApiResponseMessage> deleteProduct(@PathVariable String productId)
    {
        productService.deleteProduct(productId);
        ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
                .message("Product deleted succcessfully.")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return ResponseEntity.ok(apiResponseMessage);
    }

    @GetMapping("/getone/{productId}")
    public ResponseEntity<ProductDto> getOneProduct(@PathVariable String productId)
    {
        ProductDto oneProduct = productService.getOneProduct(productId);
        return ResponseEntity.ok(oneProduct);
    }


    @GetMapping("/getall")
    public ResponseEntity<PageableResponse<ProductDto>> getAllProduct(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "2",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc" ,required = false) String sortDir
    ) {
        PageableResponse<ProductDto> allProducts = productService.getAllProducts(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(allProducts, HttpStatus.OK);
    }


    @GetMapping("/getliveproducts")
    public ResponseEntity<PageableResponse<ProductDto>> getLiveProducts(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "2",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc" ,required = false) String sortDir
    ) {
        PageableResponse<ProductDto> liveProducts = productService.getLiveProducts(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(liveProducts, HttpStatus.OK);
    }



    @GetMapping("/getwithsubtitle/{subTitle}")
    public ResponseEntity<PageableResponse<ProductDto>> getProductsWithSubtitle(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "2",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc" ,required = false) String sortDir,
            @PathVariable String subTitle
    ) {
        PageableResponse<ProductDto> allProducts = productService.getProductWithTitleContaining(subTitle,pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(allProducts, HttpStatus.OK);
    }

    @PostMapping("/image/{productId}")
    public ResponseEntity<ImageResponse> uploadProductImage(
            @RequestParam("productImage") MultipartFile image,
            @PathVariable("productId") String productId
    ) throws IOException {
        String uploadedFile = fileService.uploadFile(image, productImageUploadPath);
        ProductDto productDto = productService.getOneProduct(productId);
        productDto.setProductImageName(uploadedFile);
        productService.updateProduct(productDto,productId);
        ImageResponse imageResponse = ImageResponse.builder().imageName(productDto.getProductImageName()).success(true).status(HttpStatus.CREATED).build();
        return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);
    }

    @GetMapping("/serveimage/{productId}")
    public void serveproductImage(@PathVariable("productId") String productId, HttpServletResponse response) throws IOException {

        ProductDto productDto = productService.getOneProduct(productId);
        logger.info("product image name : "+productDto.getProductImageName());
        InputStream resource = fileService.getResource(productImageUploadPath, productDto.getProductImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
    }








}


