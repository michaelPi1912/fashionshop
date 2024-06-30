package com.huuphucdang.fashionshop.model.payload.request;

import com.huuphucdang.fashionshop.model.entity.Coupon;
import com.huuphucdang.fashionshop.model.entity.OrderLine;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    private String paymentType;
    private String address;
    private String shipmentMethod;
    private Integer shippingCost;
    private String phone;
//    private int statusId;
    private int total;
    private List<OrderLineRequest> itemRequests;
    private List<Coupon> coupons;
}
