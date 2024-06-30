package com.huuphucdang.fashionshop.controller;

import com.huuphucdang.fashionshop.model.entity.Cart;
import com.huuphucdang.fashionshop.model.entity.Coupon;
import com.huuphucdang.fashionshop.model.entity.Order;
import com.huuphucdang.fashionshop.model.entity.User;
import com.huuphucdang.fashionshop.model.payload.request.OrderRequest;
import com.huuphucdang.fashionshop.model.payload.request.ProductRequest;
import com.huuphucdang.fashionshop.model.payload.response.OrderResponse;
import com.huuphucdang.fashionshop.service.CartService;
import com.huuphucdang.fashionshop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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

    @GetMapping("/{id}")
    public ResponseEntity<Order> getById(
        @PathVariable("id") UUID id
    ){
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/check/{id}")
    public ResponseEntity<OrderResponse> checkOrdered(
            @AuthenticationPrincipal User user,
            @PathVariable("id") UUID productItemId
    ){
        return ResponseEntity.ok(service.checkOrderedByProductItemId(user,productItemId));
    }
    @GetMapping("/user")
    public ResponseEntity<OrderResponse> getOrderByUser(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "0") int status,
            @RequestParam(defaultValue = "") String startDate,
            @RequestParam(defaultValue = "") String endDate
            ){

        return ResponseEntity.ok(service.getOrderByUser(user, page, size,status,startDate,endDate));
    }
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<OrderResponse> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "0") int status,
            @RequestParam(defaultValue = "") String code,
            @RequestParam(defaultValue = "") String phone,
            @RequestParam(defaultValue = "") String startDate,
            @RequestParam(defaultValue = "") String endDate

            ){

        return ResponseEntity.ok(service.getAll(page,size,status,code,phone,startDate,endDate));
    }

    //get by status
    @GetMapping("/list")
    public ResponseEntity<OrderResponse> getByStatus(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam("status") String status
    ){
        return ResponseEntity.ok(service.getAllByStatus(page,size,status));
    }
    @PutMapping("/update/{id}/status/{statusId}")
    public ResponseEntity<Order> updateOrder(
            @PathVariable("statusId") Integer statusId,
            @PathVariable("id") UUID id,
            @RequestParam("") String desc
    ){
        return ResponseEntity.ok(service.updateOrder(id, statusId,desc));
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<Order> cancelOrder(
//            @PathVariable("statusId") Integer statusId,
            @PathVariable("id") UUID id
    ){
        return ResponseEntity.ok(service.cancelOrder(id));
    }


    @DeleteMapping("/delete/{id}")
    public void deleteOrder(@PathVariable("id") UUID id){
        service.deleteOrder(id);
    }


}
