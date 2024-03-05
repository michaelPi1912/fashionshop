package com.huuphucdang.fashionshop.controller;

import com.huuphucdang.fashionshop.model.entity.Cart;
import com.huuphucdang.fashionshop.model.entity.Order;
import com.huuphucdang.fashionshop.model.entity.User;
import com.huuphucdang.fashionshop.model.payload.request.OrderRequest;
import com.huuphucdang.fashionshop.model.payload.request.ProductRequest;
import com.huuphucdang.fashionshop.model.payload.response.OrderResponse;
import com.huuphucdang.fashionshop.service.CartService;
import com.huuphucdang.fashionshop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;
    @PostMapping("/insert")
    public ResponseEntity<OrderResponse> saveOrder(
            @AuthenticationPrincipal User user,
            @RequestBody OrderRequest body
    ){
        return ResponseEntity.ok(service.saveOrder(user, body));
    }
    @GetMapping("/user")
    public ResponseEntity<List<Order>> getOrderByUser(@AuthenticationPrincipal User user){

        return ResponseEntity.ok(service.getOrderByUser(user));
    }
    @GetMapping("/all")
    public ResponseEntity<List<Order>> getAll(){

        return ResponseEntity.ok(service.getAll());
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<Order> updateOrder(
            @RequestBody OrderRequest body,
            @PathVariable("id") UUID id
    ){
        return ResponseEntity.ok(service.updateOrder(body,id));
    }


    @DeleteMapping("/delete/{id}")
    public void deleteOrder(@PathVariable("id") UUID id){
        service.deleteOrder(id);
    }
}
