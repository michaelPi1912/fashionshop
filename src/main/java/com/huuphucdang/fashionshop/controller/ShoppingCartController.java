package com.huuphucdang.fashionshop.controller;


import com.huuphucdang.fashionshop.model.entity.Cart;
import com.huuphucdang.fashionshop.model.entity.CartItem;
import com.huuphucdang.fashionshop.model.entity.User;
import com.huuphucdang.fashionshop.model.payload.request.CartRequest;
import com.huuphucdang.fashionshop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class ShoppingCartController {

    private final CartService service;
    @PostMapping("/insert")
    public void saveCart(
            @AuthenticationPrincipal User user
            ){
        service.saveCart(user);
    }
    @GetMapping("/")
    public ResponseEntity<Cart> getCartByUser(@AuthenticationPrincipal User user){

        return ResponseEntity.ok(service.getCartByUser(user));
    }

    @DeleteMapping("/delete/{id}")
    public void deleteCart(@PathVariable("id") UUID id){
        service.deleteCart(id);
    }
}
