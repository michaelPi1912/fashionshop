package com.huuphucdang.fashionshop.controller;

import com.huuphucdang.fashionshop.model.entity.CartItem;
import com.huuphucdang.fashionshop.model.entity.User;
import com.huuphucdang.fashionshop.model.entity.UserReview;
import com.huuphucdang.fashionshop.model.payload.request.CartItemRequest;
import com.huuphucdang.fashionshop.model.payload.request.ReviewRequest;
import com.huuphucdang.fashionshop.service.CartItemService;
import com.huuphucdang.fashionshop.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService service;

    @PostMapping("/insert")
    public void saveReview(
            @AuthenticationPrincipal User user,
            @RequestBody ReviewRequest body
    ){
        service.saveReview(user, body);
    }
    @GetMapping("/{productId}")
    public ResponseEntity<List<UserReview>> getAllByProduct(@PathVariable("productId") UUID productId){
        return ResponseEntity.ok(service.getAllReviewByProduct(productId));
    }
    @GetMapping("/user")
    public ResponseEntity<List<UserReview>> getAllReviewByUser(
            @AuthenticationPrincipal User user
    ){
        return ResponseEntity.ok(service.getAllReviewByUser(user));
    }

    @PutMapping("/update/{id}")
    public void updateReview(
            @RequestBody ReviewRequest body,
            @PathVariable("id") UUID id
    ){
        service.updateReview(body, id);
    }
    @DeleteMapping("/delete/{id}")
    public void deleteReview(@PathVariable("id") UUID id){
        service.deleteReview(id);
    }
}
