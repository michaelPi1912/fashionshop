package com.huuphucdang.fashionshop.model.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequest {
    private String comment;
    private UUID productId;
    private int rating;
    private UUID parentId;
    private String fit;
    private String gender;
    private String age;
    private String height;
    private String weight;
    private String size;
}
