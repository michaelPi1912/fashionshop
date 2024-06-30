package com.huuphucdang.fashionshop.controller;

import com.huuphucdang.fashionshop.model.entity.Product;
import com.huuphucdang.fashionshop.model.entity.ProductItem;
import com.huuphucdang.fashionshop.model.entity.User;
import com.huuphucdang.fashionshop.model.entity.VariationOption;
import com.huuphucdang.fashionshop.model.payload.request.ProductItemRequest;
import com.huuphucdang.fashionshop.model.payload.request.ProductRequest;
import com.huuphucdang.fashionshop.model.payload.response.ProductItemResponse;
import com.huuphucdang.fashionshop.service.ProductItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/product-item")
@RequiredArgsConstructor
public class ProductItemController {

    private final ProductItemService service;
    @PostMapping("/insert")
    public void saveProduct(
            @RequestBody ProductItemRequest body
    ){
       service.saveProductItem(body);
    }

    @PostMapping("/add-options/{id}")
    public void addProduct(
            @PathVariable("id") UUID id,
            @RequestBody ProductItemRequest body
    ){
        service.addOption(id,body);
    }

    @GetMapping("/all/{id}")
    public ResponseEntity<ProductItemResponse> getAllByProductId(@PathVariable("id") UUID id){
        return ResponseEntity.ok(service.getByProductId(id));
    }

//    @GetMapping("/test/{id}")
//    public ResponseEntity<ProductItemResponse> getAllByTest(
//            @PathVariable("id") UUID id,
//            @RequestParam(defaultValue = "") String color,
//            @RequestParam(defaultValue = "") String size
//    ){
//        return ResponseEntity.ok(service.getByTest(id,color,size));
//    }

    @PutMapping("/update/{id}")
    public void updateProductItem(
            @PathVariable("id") UUID id,
            @RequestBody ProductItemRequest body
    ){
        service.updateItem(id, body);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteProductItem(@PathVariable("id") UUID id){
        service.deleteById(id);
    }

    @GetMapping("/{id}/items")
    public ResponseEntity<ProductItemResponse> getAllOptionByProduct(
            @PathVariable("id") UUID id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "0") int inStock,
            @RequestParam(defaultValue = "") String sku,
            @RequestParam(defaultValue = "") String color,
            @RequestParam(defaultValue = "") String sizes,
            @RequestParam(defaultValue = "0") int price
    ){
        return  ResponseEntity.ok(service.getAllOptionByProduct(id,page,size, inStock,color,sizes, price,sku));
    }
}
