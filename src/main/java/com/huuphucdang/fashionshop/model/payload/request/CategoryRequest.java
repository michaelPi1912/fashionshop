package com.huuphucdang.fashionshop.model.payload.request;

import com.huuphucdang.fashionshop.model.entity.ProductCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class CategoryRequest {
    private String name;
    private UUID parentId;
}
