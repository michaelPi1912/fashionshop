package com.huuphucdang.fashionshop.model.payload.response;

import com.huuphucdang.fashionshop.model.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private Order order;
}
