package com.springboot.estore.Estore.services;

import com.springboot.estore.Estore.dtos.CreateOrderRequest;
import com.springboot.estore.Estore.dtos.OrderDto;
import com.springboot.estore.Estore.dtos.PageableResponse;

public interface OrderService {

    //create order
    OrderDto createOrder(CreateOrderRequest orderDto);



    //remove order
    void removeOrder(String orderId);


    //get order of user
    PageableResponse<OrderDto> getOrders(String userId,int pageNumber,int pageSize,String sortBy,String sortDir);


    //get all orders
    PageableResponse<OrderDto> getAllOrders(int pageNumber,int pageSize,String sortBy,String sortDir);
}
