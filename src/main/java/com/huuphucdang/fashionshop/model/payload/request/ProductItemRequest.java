package com.huuphucdang.fashionshop.model.payload.request;

import com.huuphucdang.fashionshop.model.entity.VariationOption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductItemRequest {
    private UUID productId;
    private String code;
    private List<String> images;
    private Integer price;
    private Integer stock;
    private List<UUID> optionsId;
}
