package com.huuphucdang.fashionshop.model.payload.response;

import com.huuphucdang.fashionshop.model.entity.Product;
import com.huuphucdang.fashionshop.model.entity.UserReview;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponse {
    private List<UserReview> reviewList;
    int currentPage;
    long totalItems;
    int totalPages;
}
