package com.huuphucdang.fashionshop.controller;

import com.huuphucdang.fashionshop.model.entity.Coupon;
import com.huuphucdang.fashionshop.model.entity.ProductCategory;
import com.huuphucdang.fashionshop.model.entity.User;
import com.huuphucdang.fashionshop.model.payload.request.CategoryRequest;
import com.huuphucdang.fashionshop.model.payload.request.CouponRequest;
import com.huuphucdang.fashionshop.model.payload.response.CategoryResponse;
import com.huuphucdang.fashionshop.model.payload.response.CouponResponse;
import com.huuphucdang.fashionshop.service.CategoryService;
import com.huuphucdang.fashionshop.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/coupon")
@RequiredArgsConstructor
public class CouponController {
    private final CouponService service;

    @PostMapping("/insert")
    public ResponseEntity<Coupon> saveCategory(
            @RequestBody CouponRequest body
    ){
        return ResponseEntity.ok(service.saveCoupon(body));
    }

    @GetMapping("/all")
    public ResponseEntity<CouponResponse> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "0") int status,
            @RequestParam(defaultValue = "") String code
    ){
        return ResponseEntity.ok(service.findAll(page,size, status,code));
    }

    @PutMapping("/update/{id}")
    public void updateCoupon(
            @RequestBody CouponRequest body,
            @PathVariable("id") UUID id
    ){
        service.updateCoupon( body, id);
    }

    @GetMapping("/check/{code}")
    public ResponseEntity<Coupon> checkCoupon(
            @PathVariable("code") String code,
            @AuthenticationPrincipal User user
    ){
        return ResponseEntity.ok(service.checkCoupon(user, code));
    }

    @DeleteMapping("/delete/{id}")
    public void deleteCategory(@PathVariable("id") UUID id){
        service.deleteCoupon(id);
    }
}
