package com.huuphucdang.fashionshop.model.payload.response;

import com.huuphucdang.fashionshop.model.entity.Coupon;
import com.huuphucdang.fashionshop.model.entity.ProductCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponResponse {
    List<Coupon> couponList;
    int currentPage;
    long totalItems;
    int totalPages;
}
