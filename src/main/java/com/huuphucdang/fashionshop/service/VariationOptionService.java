package com.huuphucdang.fashionshop.service;

import com.huuphucdang.fashionshop.model.entity.*;
import com.huuphucdang.fashionshop.model.payload.request.PromotionRequest;
import com.huuphucdang.fashionshop.model.payload.request.VariationOptionRequest;
import com.huuphucdang.fashionshop.model.payload.response.CategoryResponse;
import com.huuphucdang.fashionshop.model.payload.response.OptionResponse;
import com.huuphucdang.fashionshop.repository.ProductItemRepository;
import com.huuphucdang.fashionshop.repository.VariationOptionRepository;
import com.huuphucdang.fashionshop.repository.VariationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VariationOptionService {

    private final VariationRepository variationRepository;
    private final VariationOptionRepository optionRepository;
    private final ProductItemRepository productItemRepository;
    private final ProductItemService productItemService;

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

        Set<VariationOption> optionList = optionRepository.findOptionsByVariationId(variationId);

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
//        VariationOption option = optionRepository.findByValue(value);

        List<ProductItem> productItems = productItemRepository.findByOption(id);
        productItems.forEach(productItem -> {
            productItemService.deleteById(productItem.getId());
        });
        optionRepository.deleteOptionById(id);
        optionRepository.deleteById(id);
    }

    public OptionResponse findByProductItemId(UUID id) {
        Set<VariationOption> options = optionRepository.findByProductItemId(id);

        return OptionResponse.builder()
                .optionList(options).build();
    }
}
