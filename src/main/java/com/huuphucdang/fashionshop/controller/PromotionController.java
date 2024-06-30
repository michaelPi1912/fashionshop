package com.huuphucdang.fashionshop.controller;

import com.huuphucdang.fashionshop.model.entity.ProductCategory;
import com.huuphucdang.fashionshop.model.entity.Promotion;
import com.huuphucdang.fashionshop.model.payload.request.PromotionRequest;
import com.huuphucdang.fashionshop.model.payload.response.CategoryResponse;
import com.huuphucdang.fashionshop.model.payload.response.PromotionResponse;
import com.huuphucdang.fashionshop.service.CategoryService;
import com.huuphucdang.fashionshop.service.PromotionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/promotion")
@RequiredArgsConstructor
public class PromotionController {

    private final PromotionService service;



    @PostMapping("/insert")
    public void savePromotion(
            @RequestBody PromotionRequest body
    ){
        service.savePromotion(body);
    }

    @GetMapping("/all")
    public ResponseEntity<PromotionResponse> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{promotionId}/categories")
    public ResponseEntity<CategoryResponse> getAllCategoryByPromotionId(@PathVariable(value = "promotionId") UUID promotionId) {

        return ResponseEntity.ok(service.findCategoryByPromotion(promotionId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Promotion> getPromotionById(@PathVariable(value = "id") UUID id) {
        return ResponseEntity.ok(service.getPromotionById(id));
    }

//    @GetMapping("/{promotionId}/categories")
//    public ResponseEntity<CategoryResponse> getAllCategoriesByPromotionId(@PathVariable(value = "promotionId") UUID promotionId) {
//
//        return ResponseEntity.ok(categoryService.getCategoriesByPromotionId(promotionId));
//    }


    @PutMapping("update/{id}")
    public ResponseEntity<Promotion> updatePromotion(@PathVariable("id") UUID id, @RequestBody PromotionRequest body) {

        return ResponseEntity.ok(service.updatePromotion(body, id));
    }


    @DeleteMapping("delete/{id}")
    public void deletePromotion(@PathVariable("id") UUID id) {
       service.deletePromotion(id);
    }

}
