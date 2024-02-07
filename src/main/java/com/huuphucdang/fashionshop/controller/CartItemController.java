package com.huuphucdang.fashionshop.controller;

import com.huuphucdang.fashionshop.model.entity.Product;
import com.huuphucdang.fashionshop.model.payload.request.ProductRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
@RestController
@RequestMapping("/api/v1/variation")
@RequiredArgsConstructor
public class CartItemController {
//    @GetMapping("/{id}")
//    public ResponseEntity<Product> getProductById(@PathVariable("id") UUID id){
//
//        return ResponseEntity.ok(service.getProductById(id));
//    }
//
//    @PostMapping("/insert")
//    public void saveProduct(
//            @RequestBody ProductRequest body
//    ){
//        service.saveProduct(body);
//    }
//
//    @PutMapping("/update/{id}")
//    public void updateAddress(
//            @RequestBody ProductRequest body,
//            @PathVariable("id") UUID id
//    ){
//        service.updateProduct(body, id);
//    }
//    @DeleteMapping("/delete/{id}")
//    public void deleteProduct(@PathVariable("id") UUID id){
//        service.deleteProduct(id);
//    }
}
