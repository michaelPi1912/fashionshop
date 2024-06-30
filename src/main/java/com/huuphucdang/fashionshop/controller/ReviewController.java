package com.huuphucdang.fashionshop.controller;

import com.huuphucdang.fashionshop.model.entity.CartItem;
import com.huuphucdang.fashionshop.model.entity.User;
import com.huuphucdang.fashionshop.model.entity.UserReview;
import com.huuphucdang.fashionshop.model.payload.request.CartItemRequest;
import com.huuphucdang.fashionshop.model.payload.request.ReviewRequest;
import com.huuphucdang.fashionshop.model.payload.response.ReviewResponse;
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
    @GetMapping("/all")
    public ResponseEntity<ReviewResponse> getAll(
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "5") int size,
                @RequestParam(defaultValue = "") String email,
                @RequestParam(defaultValue = "") String start,
                @RequestParam(defaultValue = "") String end,
                @RequestParam(defaultValue = "0") int status,
                @RequestParam(defaultValue = "0") int rating
                ){
        return ResponseEntity.ok(service.getAll(page,size, email, start, end, status, rating));
    }
    @GetMapping("/product/{productId}")
    public ResponseEntity<ReviewResponse> getAllByProduct(
            @PathVariable("productId") UUID productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "0") int rating

            ){
        return ResponseEntity.ok(service.getAllReviewByProduct(productId, page, size,rating));
    }

    @GetMapping("/product/all/{productId}")
    public ResponseEntity<List<UserReview>> getByProduct(
            @PathVariable("productId") UUID productId
    ){
        return ResponseEntity.ok(service.getAllProduct(productId));
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
    @PutMapping("/update/active/{id}")
    public void updateActive(
            @PathVariable("id") UUID id
    ){
        service.changeActive(id);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteReview(@PathVariable("id") UUID id){
        service.deleteReview(id);
    }
}
