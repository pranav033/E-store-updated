package com.springboot.estore.Estore.controllers;

import com.springboot.estore.Estore.dtos.AddItemtoCartRequest;
import com.springboot.estore.Estore.dtos.ApiResponseMessage;
import com.springboot.estore.Estore.dtos.CartDto;
import com.springboot.estore.Estore.services.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class CartController {


    @Autowired
    private CartService cartService;


    @PostMapping("/additem/{userId}")
    public ResponseEntity<CartDto> addItemtoCart(@PathVariable("userId") String userID, @Valid @RequestBody AddItemtoCartRequest request)
    {
        CartDto cartDto = cartService.addItemtoCart(userID, request);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }

    @DeleteMapping("/removeitem/{userId}/{cartItemId}")
    public ResponseEntity<ApiResponseMessage> removeFromCart(@PathVariable("cartItemId") int cartItemId,@PathVariable("userId") String userId)
    {
        cartService.removeFromCart(userId,cartItemId);
        ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
                .status(HttpStatus.OK)
                .success(true)
                .message("Cart item removed successfully")
                .build();
        return ResponseEntity.ok(apiResponseMessage);
    }

    @DeleteMapping("/clearcart/{userId}")
    public  ResponseEntity<ApiResponseMessage> clearCart(@PathVariable("userId") String userId)
    {
        cartService.clearCart(userId);
        ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
                .status(HttpStatus.OK)
                .success(true)
                .message("Cart cleared successfully")
                .build();
        return ResponseEntity.ok(apiResponseMessage);
    }

    @GetMapping("/getcartofuser/{userId}")
    public ResponseEntity<CartDto> getOneCartByUser(@PathVariable("userId") String userId)
    {
        CartDto cartByUser = cartService.getCartByUser(userId);
        return ResponseEntity.ok(cartByUser);
    }
}
