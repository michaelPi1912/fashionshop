package com.huuphucdang.fashionshop.controller;

import com.huuphucdang.fashionshop.model.entity.Product;
import com.huuphucdang.fashionshop.model.payload.request.ProductRequest;
import com.huuphucdang.fashionshop.model.payload.request.VariationRequest;
import com.huuphucdang.fashionshop.model.payload.response.ProductResponse;
import com.huuphucdang.fashionshop.model.payload.response.VariationResponse;
import com.huuphucdang.fashionshop.service.ProductService;
import com.huuphucdang.fashionshop.service.VariationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
@RestController
@RequestMapping("/api/v1/variation")
@RequiredArgsConstructor
public class VariationController {
    private final VariationService service;
    @PostMapping("/insert")
    public void saveVariation(
            @RequestBody VariationRequest body
    ){
        service.saveVariation(body);
    }
//    change API get all to get all by category
//    @GetMapping("/all")
//    public ResponseEntity<VariationResponse> getAllVariation(){
//
//        return ResponseEntity.ok(service.findAll());
//    }

    @GetMapping("/{categoryId}/variations")
    public ResponseEntity<VariationResponse> getAllVariationByCategory(@PathVariable("categoryId") UUID categoryId){

        return ResponseEntity.ok(service.getAllByCategoryId(categoryId));
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<Product> getProductById(@PathVariable("id") UUID id){
//
//        return ResponseEntity.ok(service.getProductById(id));
//    }

    @PutMapping("/update/{id}")
    public void updateAddress(
            @RequestBody VariationRequest body,
            @PathVariable("id") UUID id
    ){
        service.updateVariation(body, id);
    }
    @DeleteMapping("/delete/{id}")
    public void deleteVariation(@PathVariable("id") UUID id){
        service.deleteVariation(id);
    }
}
