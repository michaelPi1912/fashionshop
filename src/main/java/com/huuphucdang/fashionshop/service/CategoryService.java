package com.huuphucdang.fashionshop.service;

import com.huuphucdang.fashionshop.model.entity.ProductCategory;
import com.huuphucdang.fashionshop.model.entity.Promotion;
import com.huuphucdang.fashionshop.model.payload.request.CategoryRequest;
import com.huuphucdang.fashionshop.model.payload.response.CategoryResponse;
import com.huuphucdang.fashionshop.model.payload.response.PromotionResponse;
import com.huuphucdang.fashionshop.repository.CategoryRepository;
import com.huuphucdang.fashionshop.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private  final CategoryRepository categoryRepository;

    private final PromotionRepository promotionRepository;

    public ProductCategory saveCategory(CategoryRequest body) {
        ProductCategory category = new ProductCategory();
        category.setName(body.getName());
        // enter UUID => get Category by ID => add object to parent. code again
        if(body.getParentId() != null){
            ProductCategory categoryParent = categoryRepository.getReferenceById(body.getParentId());
            category.setParent(categoryParent);
        }else {
            category.setParent(null);
        }
        return categoryRepository.save(category);
    }

    //getAll
    public CategoryResponse findAll(){
        List<ProductCategory> categoryList = categoryRepository.getAll();
        if(categoryList == null){
            return null;
        }

        return CategoryResponse
                .builder()
                .categoryList(categoryList)
                .build();
    }

    //delete
    public void deleteCategory(UUID id) {
        categoryRepository.deleteById(id);
    }

    //update => get promotion -> change promotion
    public ProductCategory updatePromotion(CategoryRequest body, UUID id) {
        ProductCategory category = categoryRepository.getReferenceById(id);
        category.setName(body.getName());
        return categoryRepository.save(category);
    }


    public Promotion addPromotion(UUID categoryId, UUID promotionId) {
        ProductCategory category = categoryRepository.findById(categoryId).orElseThrow();
        Promotion promotion = promotionRepository.findById(promotionId).orElseThrow();
        promotion.getPromotionCategories().add(category);

        return promotionRepository.save(promotion);
    }


    public void deletePromotionFromCategory(UUID categoryId, UUID promotionId) {
        promotionRepository.deletePromotionFromCategory(categoryId, promotionId);
    }


    public PromotionResponse findPromotionByCategory(UUID categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            return null;
        }

        List<Promotion> promotionList = promotionRepository.findPromotionsByCategoryId(categoryId);

        return PromotionResponse
                .builder()
                .promotionList(promotionList)
                .build();
    }
}
