package com.huuphucdang.fashionshop.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class ShoppingCartController {
//    @PostMapping("/insert")
//    public void saveProduct(
//            @RequestBody ProductRequest body
//    ){
//        service.saveProduct(body);
//    }
//    @GetMapping("/{id}")
//    public ResponseEntity<Product> getProductById(@PathVariable("id") UUID id){
//
//        return ResponseEntity.ok(service.getProductById(id));
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
