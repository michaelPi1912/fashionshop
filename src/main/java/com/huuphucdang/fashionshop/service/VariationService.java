package com.huuphucdang.fashionshop.service;

import com.huuphucdang.fashionshop.model.entity.*;
import com.huuphucdang.fashionshop.model.payload.request.VariationRequest;
import com.huuphucdang.fashionshop.model.payload.response.VariationResponse;
import com.huuphucdang.fashionshop.repository.CategoryRepository;
import com.huuphucdang.fashionshop.repository.VariationOptionRepository;
import com.huuphucdang.fashionshop.repository.VariationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VariationService {

    private final VariationRepository variationRepository;
    private final CategoryRepository categoryRepository;
    private final VariationOptionRepository variationOptionRepository;
    private final VariationOptionService optionService;

    public Variation saveVariation(VariationRequest body){

        Variation variation = new Variation();
        ProductCategory category = categoryRepository.findById(body.getCategoryId()).orElseThrow();
        variation.setName(body.getName());
        if(category != null){
            variation.setCategory(category);
        }
        return variationRepository.save(variation);
    }
    public void deleteVariation(UUID id) {
        Set<VariationOption> options = variationOptionRepository.findOptionsByVariationId(id);
        options.forEach(variationOption -> {
            optionService.deleteOption(variationOption.getId());
        });
        variationRepository.deleteById(id);
    }


    public Variation updateVariation(VariationRequest body, UUID id) {
        Variation variation = variationRepository.findById(id).orElseThrow();
        ProductCategory category = categoryRepository.findById(body.getCategoryId()).orElseThrow();
        variation.setName(body.getName());
        if(category != null){
            variation.setCategory(category);
        }
        return variationRepository.save(variation);
    }

    public VariationResponse getAllByCategoryId(UUID categoryId) {
        List<Variation> variationList = variationRepository.getByCategoryId(categoryId);
        for(Variation variation: variationList){
            Set<VariationOption> options = variationOptionRepository.findOptionsByVariationId(variation.getId());
            variation.setVariationOptions(options);
        }
        return VariationResponse.builder()
                .variations(variationList).build();
    }
}
