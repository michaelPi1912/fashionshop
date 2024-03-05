package com.huuphucdang.fashionshop.controller;

import com.huuphucdang.fashionshop.model.entity.CartItem;
import com.huuphucdang.fashionshop.model.entity.Product;
import com.huuphucdang.fashionshop.model.payload.request.CartItemRequest;
import com.huuphucdang.fashionshop.model.payload.request.ProductRequest;
import com.huuphucdang.fashionshop.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
@RestController
@RequestMapping("/api/v1/cartItem")
@RequiredArgsConstructor
public class CartItemController {

    private final CartItemService service;

    @PostMapping("/insert")
    public void saveCartItem(
            @RequestBody CartItemRequest body
    ){
        service.saveCartItem(body);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<List<CartItem>> getItemByCart(@PathVariable("cartId") UUID cartId){
        return ResponseEntity.ok(service.getAllItemByCartId(cartId));
    }

    @PutMapping("/update/{id}")
    public void updateAddress(
            @RequestBody CartItemRequest body,
            @PathVariable("id") UUID id
    ){
        service.updateCartItem(body, id);
    }
    @DeleteMapping("/delete/{id}")
    public void deleteCartItem(@PathVariable("id") UUID id){
        service.deleteCartItem(id);
    }
}
