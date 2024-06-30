package com.huuphucdang.fashionshop.model.payload.request;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponRequest {
    private String code;
    private String name;
    private String description;
    private String discountType;
    private Integer amount;
    private String startDate;
    private String endDate;
    private Integer limitUsage;
    private Integer times;
    private Integer maxValue;
}
