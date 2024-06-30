package com.huuphucdang.fashionshop.model.payload.response;

import com.huuphucdang.fashionshop.model.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressResponse {
    List<Address> addressList;
    int currentPage;
    long totalItems;
    int totalPages;
}
