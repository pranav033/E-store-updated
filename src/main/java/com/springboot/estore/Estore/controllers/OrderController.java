package com.springboot.estore.Estore.controllers;

import com.springboot.estore.Estore.dtos.ApiResponseMessage;
import com.springboot.estore.Estore.dtos.CreateOrderRequest;
import com.springboot.estore.Estore.dtos.OrderDto;
import com.springboot.estore.Estore.dtos.PageableResponse;
import com.springboot.estore.Estore.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;


    @PostMapping("/create")
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody CreateOrderRequest request)
    {
        OrderDto order = orderService.createOrder(request);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }


    @DeleteMapping("/remove/{orderId}")
    public ResponseEntity<ApiResponseMessage> removeOrder(@PathVariable("orderId") String orderId)
    {
        orderService.removeOrder(orderId);
        ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
                .message("Order removed successfully")
                .status(HttpStatus.OK)
                .success(true)
                .build();
        return ResponseEntity.ok(apiResponseMessage);
    }


    @GetMapping("/getordersofuser/{userId}")
    public ResponseEntity<PageableResponse<OrderDto>> getOrdersofUser(@PathVariable("userId") String userId
                                                                        ,@RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
                                                                      @RequestParam(value = "pageSize",defaultValue = "2",required = false) int pageSize,
                                                                      @RequestParam(value = "sortBy",defaultValue = "orderedDate",required = false) String sortBy,
                                                                      @RequestParam(value = "sortDir",defaultValue = "desc" ,required = false) String sortDir)
    {
        PageableResponse<OrderDto> orders = orderService.getOrders(userId, pageNumber, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(orders);
    }


    @GetMapping("/getallorders")
    public ResponseEntity<PageableResponse<OrderDto>> getAllOrders(@RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
                                                                   @RequestParam(value = "pageSize",defaultValue = "2",required = false) int pageSize,
                                                                   @RequestParam(value = "sortBy",defaultValue = "orderedDate",required = false) String sortBy,
                                                                   @RequestParam(value = "sortDir",defaultValue = "desc" ,required = false) String sortDir)
    {
        PageableResponse<OrderDto> allOrders = orderService.getAllOrders(pageNumber, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(allOrders);
    }




}
