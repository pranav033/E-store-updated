package com.springboot.estore.Estore.services.impl;

import com.springboot.estore.Estore.dtos.AddItemtoCartRequest;
import com.springboot.estore.Estore.dtos.CartDto;
import com.springboot.estore.Estore.entities.Cart;
import com.springboot.estore.Estore.entities.CartItem;
import com.springboot.estore.Estore.entities.Product;
import com.springboot.estore.Estore.entities.User;
import com.springboot.estore.Estore.exceptions.BadApiRequest;
import com.springboot.estore.Estore.exceptions.ResourceNotFoundException;
import com.springboot.estore.Estore.repository.CartItemRepository;
import com.springboot.estore.Estore.repository.CartRepository;
import com.springboot.estore.Estore.repository.ProductRepository;
import com.springboot.estore.Estore.repository.UserRepository;
import com.springboot.estore.Estore.services.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
public class CartServiceimpl implements CartService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public CartDto addItemtoCart(String userId, AddItemtoCartRequest request) {

        String productId = request.getProductId();
        int quantity = request.getQuantity();
        if(quantity<=0)
        {
            throw new BadApiRequest("Quantity must be greater than zero.");
        }
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found with given ID"));
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found for given ID"));
        Cart cart = null;
        try{
            cart = cartRepository.findByUser(user).get();
        }
        catch(NoSuchElementException ex){
            cart = new Cart();
            cart.setCreatedAt(new Date());
            cart.setCartId(UUID.randomUUID().toString());

        }



        //if item already present then update quantity
        List<CartItem> items = cart.getItems();
        AtomicBoolean updated = new AtomicBoolean(false);
        items = items.stream().map(
                item -> {
                    if (item.getProduct().getProductId().equalsIgnoreCase(productId)) {
                        //item already present
                        item.setQuantity(item.getQuantity() + quantity);
                        item.setTotalPrice(item.getTotalPrice() + quantity * product.getDiscountedPrice());
                        updated.set(true);
                    }
                    return item;
                }
        ).collect(Collectors.toList());

      //  cart.setItems(updatedItems);

        if(!updated.get()) {
            CartItem cartItem = CartItem.builder()
                    .product(product)
                    .cart(cart)
                    .totalPrice(quantity * product.getDiscountedPrice())
                    .quantity(quantity)
                    .build();
            cart.getItems().add(cartItem);
        }
        cart.setUser(user);
        Cart cart1 = cartRepository.save(cart);
        CartDto cartDto = modelMapper.map(cart1, CartDto.class);

        return cartDto;
    }

    @Override
    public void removeFromCart(String userId, int cartItemId) {

        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(() -> new ResourceNotFoundException("No cart item found for given ID"));
        cartItemRepository.delete(cartItem);

    }

    @Override
    public void clearCart(String userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found for given ID"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("No cart found for the given user."));
        cart.getItems().clear();
        cartRepository.save(cart);

    }

    @Override
    public CartDto getCartByUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found for given ID"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("No cart found for the given user."));
        return modelMapper.map(cart,CartDto.class);
    }
}
