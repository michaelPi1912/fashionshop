package com.huuphucdang.fashionshop.service;

import com.huuphucdang.fashionshop.model.entity.*;
import com.huuphucdang.fashionshop.model.payload.request.AddressRequest;
import com.huuphucdang.fashionshop.model.payload.request.PromotionRequest;
import com.huuphucdang.fashionshop.model.payload.request.VariationRequest;
import com.huuphucdang.fashionshop.model.payload.response.ProductResponse;
import com.huuphucdang.fashionshop.model.payload.response.PromotionResponse;
import com.huuphucdang.fashionshop.model.payload.response.VariationResponse;
import com.huuphucdang.fashionshop.repository.CategoryRepository;
import com.huuphucdang.fashionshop.repository.VariationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VariationService {

    private final VariationRepository variationRepository;
    private final CategoryRepository categoryRepository;

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
        variationRepository.deleteById(id);
    }

    //getAll
    public VariationResponse findAll(){
        List<Variation> variationList = variationRepository.findAll();
        if(variationList.isEmpty()){
            return null;
        }

        return VariationResponse.builder()
                .variationList(variationList)
                .build();
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
        List<Variation> variationList = variationRepository.getProductsByCategoryId(categoryId);

        return VariationResponse.builder()
                .variationList(variationList).build();
    }
}
