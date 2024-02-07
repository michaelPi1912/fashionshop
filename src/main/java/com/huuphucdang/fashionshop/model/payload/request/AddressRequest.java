package com.huuphucdang.fashionshop.model.payload.request;

import com.huuphucdang.fashionshop.model.entity.AddressType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressRequest {
    private String province;
    private String district;
    private String commune;
    private String addressDetail;
    private AddressType addressType;
}
