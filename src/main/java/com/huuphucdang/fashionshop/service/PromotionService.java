package com.huuphucdang.fashionshop.service;

import com.huuphucdang.fashionshop.model.entity.ProductCategory;
import com.huuphucdang.fashionshop.model.entity.Promotion;
import com.huuphucdang.fashionshop.model.payload.request.PromotionRequest;
import com.huuphucdang.fashionshop.model.payload.response.CategoryResponse;
import com.huuphucdang.fashionshop.model.payload.response.PromotionResponse;
import com.huuphucdang.fashionshop.repository.CategoryRepository;
import com.huuphucdang.fashionshop.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PromotionService {

    private final PromotionRepository promotionRepository;
    private final CategoryRepository categoryRepository;


    public Promotion savePromotion(PromotionRequest body) {
        Promotion promotion = new Promotion();
        promotion.setName(body.getName());
        promotion.setDescription(body.getDescription());
        promotion.setDiscountRate(body.getDiscountRate());
        promotion.setStartDate(body.getStartDate());
        promotion.setEndDate(body.getEndDate());
        return promotionRepository.save(promotion);
    }

    //getAll
    public PromotionResponse findAll(){
        List<Promotion> promotionList = promotionRepository.getAll();
        if(promotionList.isEmpty()){
            return null;
        }

        return PromotionResponse
                .builder()
                .promotionList(promotionList)
                .build();
    }


    //delete
    public void deletePromotion(UUID id) {
        promotionRepository.deleteById(id);
    }

    //update => get promotion -> change promotion
    public Promotion updatePromotion(PromotionRequest body, UUID id) {
        Promotion promotion = promotionRepository.getReferenceById(id);
        promotion.setName(body.getName());
        promotion.setDescription(body.getDescription());
        promotion.setDiscountRate(body.getDiscountRate());
        promotion.setStartDate(body.getStartDate());
        promotion.setEndDate(body.getEndDate());
        return promotionRepository.save(promotion);
    }

    public Promotion getPromotionById(UUID id) {
        Promotion promotion = promotionRepository.getReferenceById(id);
        return promotion;
    }

    public CategoryResponse findCategoryByPromotion(UUID promotionId){
        if (!promotionRepository.existsById(promotionId)) {
            return null;
        }

        List<ProductCategory> categoryList = categoryRepository.findCategoriesByPromotionId(promotionId);

        return CategoryResponse
                .builder()
                .categoryList(categoryList)
                .build();
    }

}
