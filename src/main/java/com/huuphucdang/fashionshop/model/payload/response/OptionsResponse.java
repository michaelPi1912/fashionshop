package com.huuphucdang.fashionshop.model.payload.response;

import com.huuphucdang.fashionshop.model.entity.VariationOption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OptionsResponse {
    List<VariationOption> optionList;
}
