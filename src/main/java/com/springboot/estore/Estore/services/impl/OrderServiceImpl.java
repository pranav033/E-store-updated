package com.springboot.estore.Estore.services.impl;

import com.springboot.estore.Estore.dtos.CreateOrderRequest;
import com.springboot.estore.Estore.dtos.OrderDto;
import com.springboot.estore.Estore.dtos.PageableResponse;
import com.springboot.estore.Estore.entities.*;
import com.springboot.estore.Estore.exceptions.BadApiRequest;
import com.springboot.estore.Estore.exceptions.ResourceNotFoundException;
import com.springboot.estore.Estore.helpers.Helper;
import com.springboot.estore.Estore.repository.CartRepository;
import com.springboot.estore.Estore.repository.OrderRepository;
import com.springboot.estore.Estore.repository.UserRepository;
import com.springboot.estore.Estore.services.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CartRepository cartRepository;


    @Override
    public OrderDto createOrder(CreateOrderRequest orderDto) {

        String userId = orderDto.getUserId();

        String cartId = orderDto.getCartId();

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("No user found for this ID"));
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("No cart found for this ID"));
        List<CartItem> cartItems = cart.getItems();
        if(cartItems.size()<=0)
        {
            throw new BadApiRequest("Add something in the cart to order.");
        }
        Order order = Order.builder()
                .billingAddress(orderDto.getBillingAddress())
                .billingName(orderDto.getBillingName())
                .billingPhone(orderDto.getBillingPhone())
                .orderedDate(new Date())
                .deliveredDate(null)
                .paymentStatus(orderDto.getPaymentStatus())
                .orderStatus(orderDto.getOrderStatus())
                .orderId(UUID.randomUUID().toString())
                .user(user)
                .build();

        AtomicReference<Integer> orderAmount = new AtomicReference<>(0);

        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {

            OrderItem orderItem = OrderItem.builder()
                    .quantity(cartItem.getQuantity())
                    .product(cartItem.getProduct())
                    .order(order)
                    .totalPrice(cartItem.getQuantity() * cartItem.getProduct().getDiscountedPrice())
                    .build();
            orderAmount.set(orderAmount.get() + orderItem.getTotalPrice());
            return orderItem;
        }).collect(Collectors.toList());



        order.setOrderAmount(orderAmount.get());
        order.setOrderItems(orderItems);

        cart.getItems().clear();
        cartRepository.save(cart);
        Order order1 = orderRepository.save(order);

        return modelMapper.map(order1, OrderDto.class);
    }

    @Override
    public void removeOrder(String orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("No order found for this ID"));
        orderRepository.delete(order);
    }

    @Override
    public PageableResponse<OrderDto> getOrders(String userId, int pageNumber, int pageSize, String sortBy, String sortDir) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("No user found for this ID"));
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Order> orders = orderRepository.findByUser(user, pageable);
        PageableResponse<OrderDto> pageableResponse = Helper.getPageableResponse(orders, OrderDto.class);
        return pageableResponse;
    }

    @Override
    public PageableResponse<OrderDto> getAllOrders(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Order> all = orderRepository.findAll(pageable);
        PageableResponse<OrderDto> pageableResponse = Helper.getPageableResponse(all, OrderDto.class);
        return pageableResponse;
    }
}
