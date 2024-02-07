package com.huuphucdang.fashionshop.model.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PromotionRequest {

    private String name;
    private String description;
    private float discountRate;
    private Date startDate;
    private Date endDate;
}
