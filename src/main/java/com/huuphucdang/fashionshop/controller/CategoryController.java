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
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService service;

    @PostMapping("/insert")
    public ResponseEntity<ProductCategory> saveCategory(
            @RequestBody CategoryRequest body
    ){
        return ResponseEntity.ok(service.saveCategory(body));
    }

    @GetMapping("/all")

    public ResponseEntity<CategoryResponse> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ){
        return ResponseEntity.ok(service.findAll(page,size));
    }

    @GetMapping("/list")
    public ResponseEntity<CategoryResponse> findAll(

    ){
        return ResponseEntity.ok(service.getAll());
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
