package com.huuphucdang.fashionshop.controller;

import com.huuphucdang.fashionshop.model.entity.ProductCategory;
import com.huuphucdang.fashionshop.model.entity.Promotion;
import com.huuphucdang.fashionshop.model.payload.request.CategoryRequest;
import com.huuphucdang.fashionshop.model.payload.request.PromotionRequest;
import com.huuphucdang.fashionshop.model.payload.response.CategoryResponse;
import com.huuphucdang.fashionshop.model.payload.response.PromotionResponse;
import com.huuphucdang.fashionshop.model.payload.response.addPromotionResponse;
import com.huuphucdang.fashionshop.service.CategoryService;
import com.huuphucdang.fashionshop.service.PromotionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService service;

    @PostMapping("/insert")
    public void saveCategory(
            @RequestBody CategoryRequest body
    ){
        service.saveCategory(body);
    }

    @GetMapping("/all")
    public ResponseEntity<CategoryResponse> getAll(){

        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{categoryId}/promotions")
    public ResponseEntity<PromotionResponse> getAllPromotionByCategoryId(@PathVariable(value = "categoryId") UUID categoryId) {

        return ResponseEntity.ok(service.findPromotionByCategory(categoryId));
    }

    @DeleteMapping("/delete/{id}")
    public void deleteCategory(@PathVariable("id") UUID id){
        service.deleteCategory(id);
    }

    @PutMapping("/update/{id}")
    public void updateCategory(
            @RequestBody CategoryRequest body,
            @PathVariable("id") UUID id
    ){
        service.updatePromotion( body, id);
    }

    @PostMapping("/{categoryId}/promotion/{promotionId}")
    public ResponseEntity<Promotion> addPromotion(
            @PathVariable(value = "categoryId") UUID categoryId,
            @PathVariable(value = "promotionId") UUID promotionId) {

        return ResponseEntity.ok(service.addPromotion(categoryId, promotionId));
    }
    //sua lai : tao DeletePromotionByCategoryID join voi bang category_promotion roi xoa
    @DeleteMapping("/{categoryId}/promotion/{promotionId}")
    public void deletePromotionFromCategory(
            @PathVariable(value = "categoryId") UUID categoryId,
            @PathVariable(value = "promotionId") UUID promotionId) {

        service.deletePromotionFromCategory(categoryId, promotionId);
    }



}
