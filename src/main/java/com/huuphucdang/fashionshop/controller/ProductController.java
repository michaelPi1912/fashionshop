package com.huuphucdang.fashionshop.controller;

import com.huuphucdang.fashionshop.model.entity.Product;
import com.huuphucdang.fashionshop.model.entity.Promotion;
import com.huuphucdang.fashionshop.model.entity.User;
import com.huuphucdang.fashionshop.model.entity.VariationOption;
import com.huuphucdang.fashionshop.model.payload.request.FilterRequest;
import com.huuphucdang.fashionshop.model.payload.request.ProductRequest;
import com.huuphucdang.fashionshop.model.payload.response.OptionResponse;
import com.huuphucdang.fashionshop.model.payload.response.ProductResponse;
import com.huuphucdang.fashionshop.model.payload.response.PromotionResponse;
import com.huuphucdang.fashionshop.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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


    @GetMapping("/all")
    public ResponseEntity<ProductResponse> getAllProduct(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "") String category,
            @RequestParam(defaultValue = "") String search
    ){

        return ResponseEntity.ok(service.getAll(page, size,category,search));
    }

    @GetMapping("/list")
    public ResponseEntity<ProductResponse> getAllProductByCategoryName(
            @RequestParam(defaultValue = "all") String category,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "") List<String> colors,
            @RequestParam(defaultValue = "") List<String> sizes,
            @RequestParam(defaultValue = "0") int min,
            @RequestParam(defaultValue = "0") int max
            ){
        return ResponseEntity.ok(service.getAllByCategoryName(search,category,page,size,sizes, colors, min, max));
    }

    @PostMapping("/filter")
    public ResponseEntity<ProductResponse> getAllByFilter(
            @RequestBody FilterRequest body,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ){
        return ResponseEntity.ok(service.getAllByFilter(body, page, size));
    }

    @GetMapping("/related/{category}/{id}")
    public ResponseEntity<ProductResponse> getProductsRelated(@PathVariable("category") String category, @PathVariable("id") UUID id){

        return ResponseEntity.ok(service.getAllRelated(category,id));
    }


    @GetMapping("/{categoryId}/products")
    public ResponseEntity<ProductResponse> getAllProductByCategory(@PathVariable("categoryId") UUID categoryId){

        return ResponseEntity.ok(service.getAllByCategoryId(categoryId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") UUID id){

        return ResponseEntity.ok(service.getProductById(id));
    }

    @PutMapping("/update/{id}")
    public void updateProduct(
            @RequestBody ProductRequest body,
            @PathVariable("id") UUID id
    ){
        service.updateProduct(body, id);
    }
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public void deleteProduct(@PathVariable("id") UUID id){
        service.deleteProduct(id);
    }


    @GetMapping("/wish-list")
    public ProductResponse getWishListByUser(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ){
        return service.getWishListByUser(user.getId(), page, size);
    }

}
