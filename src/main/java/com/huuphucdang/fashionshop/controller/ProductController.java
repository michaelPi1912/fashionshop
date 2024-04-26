package com.huuphucdang.fashionshop.controller;

import com.huuphucdang.fashionshop.model.entity.Product;
import com.huuphucdang.fashionshop.model.entity.Promotion;
import com.huuphucdang.fashionshop.model.entity.VariationOption;
import com.huuphucdang.fashionshop.model.payload.request.ProductRequest;
import com.huuphucdang.fashionshop.model.payload.response.OptionResponse;
import com.huuphucdang.fashionshop.model.payload.response.ProductResponse;
import com.huuphucdang.fashionshop.model.payload.response.PromotionResponse;
import com.huuphucdang.fashionshop.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;
    @PostMapping("/insert")
    public void saveProduct(
            @RequestBody ProductRequest body
    ){
        service.saveProduct(body);
    }

    @PostMapping("/{productId}/option/{optionId}")
    public ResponseEntity<Product> addOption(
            @PathVariable(value = "productId") UUID productId,
            @PathVariable(value = "optionId") UUID optionId) {

        return ResponseEntity.ok(service.addOption(productId, optionId));
    }

    @GetMapping("/all")
    public ResponseEntity<ProductResponse> getAllProduct(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ){

        return ResponseEntity.ok(service.getAll(page, size));
    }

    @GetMapping("/list")
    public ResponseEntity<ProductResponse> getAllProductByCategoryName(@RequestParam(value="name") String categoryName){
        return ResponseEntity.ok(service.getAllByCategoryName(categoryName));
    }

    @GetMapping("/{categoryId}/products")
    public ResponseEntity<ProductResponse> getAllProductByCategory(@PathVariable("categoryId") UUID categoryId){

        return ResponseEntity.ok(service.getAllByCategoryId(categoryId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") UUID id){

        return ResponseEntity.ok(service.getProductById(id));
    }

    @GetMapping("/{productId}/options")
    public ResponseEntity<OptionResponse> getAllOptionsByProductId(@PathVariable(value = "productId") UUID productId) {

        return ResponseEntity.ok(service.findOptionsByProduct(productId));
    }

    @PutMapping("/update/{id}")
    public void updateProduct(
            @RequestBody ProductRequest body,
            @PathVariable("id") UUID id
    ){
        service.updateProduct(body, id);
    }
    @DeleteMapping("/delete/{id}")
    public void deleteProduct(@PathVariable("id") UUID id){
        service.deleteProduct(id);
    }

    @DeleteMapping("/{productId}/option/{optionId}")
    public void deleteOptionFromProduct(
            @PathVariable(value = "productId") UUID productId,
            @PathVariable(value = "optionId") UUID optionId) {

        service.deleteOptionFromProduct(productId, optionId);
    }
}
