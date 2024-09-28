package com.springboot.estore.Estore.services;

import com.springboot.estore.Estore.dtos.AddItemtoCartRequest;
import com.springboot.estore.Estore.dtos.CartDto;

public interface CartService {

    //add item to cart
    //case 1 : if cart not available -> create a cart, and add the requested items to it.
    //case 2 : if cart available -> add items to the cart.

    CartDto addItemtoCart(String userId, AddItemtoCartRequest request);

    void removeFromCart(String userId,int cartItemId);

    void clearCart(String userId);

    CartDto getCartByUser(String userId);
}
