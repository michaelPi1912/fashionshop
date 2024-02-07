package com.huuphucdang.fashionshop.service;

import com.huuphucdang.fashionshop.model.entity.ProductCategory;
import com.huuphucdang.fashionshop.model.entity.Promotion;
import com.huuphucdang.fashionshop.model.entity.Variation;
import com.huuphucdang.fashionshop.model.entity.VariationOption;
import com.huuphucdang.fashionshop.model.payload.request.PromotionRequest;
import com.huuphucdang.fashionshop.model.payload.request.VariationOptionRequest;
import com.huuphucdang.fashionshop.model.payload.response.CategoryResponse;
import com.huuphucdang.fashionshop.model.payload.response.OptionResponse;
import com.huuphucdang.fashionshop.repository.VariationOptionRepository;
import com.huuphucdang.fashionshop.repository.VariationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VariationOptionService {

    private final VariationRepository variationRepository;
    private final VariationOptionRepository optionRepository;
    public VariationOption saveVariationOption(VariationOptionRequest body) {
        VariationOption option = new VariationOption();
        Variation variation = variationRepository.findById(body.getVariationId()).orElseThrow();
        if(variation !=null){
            option.setVariation(variation);
        }
        option.setValue(body.getValue());

        return optionRepository.save(option);
    }

    public OptionResponse findOptionsByVariation(UUID variationId) {
        if (!variationRepository.existsById(variationId)) {
            return null;
        }

        List<VariationOption> optionList = optionRepository.findOptionsByVariationId(variationId);

        return OptionResponse.builder()
                .optionList(optionList)
                .build();
    }

    public VariationOption updateOption(VariationOptionRequest body, UUID id) {
        VariationOption option = optionRepository.findById(id).orElseThrow();
        option.setValue(body.getValue());
        return  optionRepository.save(option);
    }

    public void deleteOption(UUID id) {
        optionRepository.deleteById(id);
    }
}
