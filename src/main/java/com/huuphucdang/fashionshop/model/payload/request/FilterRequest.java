package com.huuphucdang.fashionshop.model.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FilterRequest {
    private String key;
    private String category;
    private Integer prices;
    private List<String> sizes;
    private List<String> colors;
    private Integer min;
    private Integer max;
}
