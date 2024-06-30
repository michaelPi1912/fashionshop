package com.huuphucdang.fashionshop.service;

import com.huuphucdang.fashionshop.model.entity.Product;
import com.huuphucdang.fashionshop.model.entity.ProductCategory;
import com.huuphucdang.fashionshop.model.entity.Promotion;
import com.huuphucdang.fashionshop.model.entity.Variation;
import com.huuphucdang.fashionshop.model.payload.request.CategoryRequest;
import com.huuphucdang.fashionshop.model.payload.request.VariationRequest;
import com.huuphucdang.fashionshop.model.payload.response.CategoryResponse;
import com.huuphucdang.fashionshop.model.payload.response.PromotionResponse;
import com.huuphucdang.fashionshop.repository.CategoryRepository;
import com.huuphucdang.fashionshop.repository.ProductRepository;
import com.huuphucdang.fashionshop.repository.PromotionRepository;
import com.huuphucdang.fashionshop.repository.VariationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private  final CategoryRepository categoryRepository;

    private final PromotionRepository promotionRepository;
    private final VariationRepository variationRepository;
    private final VariationService variationService;
    private final ProductRepository productRepository;
    private final ProductService productService;

    public ProductCategory saveCategory(CategoryRequest body) {
        ProductCategory category = new ProductCategory();
        category.setName(body.getName());
        if(body.getParentId() != null){
            ProductCategory categoryParent = categoryRepository.findById(body.getParentId()).orElseThrow();
            category.setParent(categoryParent);
        }else {
            category.setParent(null);
        }
        categoryRepository.save(category);

        VariationRequest color = new VariationRequest();
        color.setCategoryId(category.getId());
        color.setName("Color");
        variationService.saveVariation(color);
        VariationRequest size = new VariationRequest();
        size.setCategoryId(category.getId());
        size.setName("Size");
        variationService.saveVariation(size);

        return category;
    }

    //getAll
    public CategoryResponse findAll(int page, int size){

        try {
            List<ProductCategory> categoryList;
            Pageable paging = PageRequest.of(page, size);
            Page<ProductCategory> pageCategories = categoryRepository.findAll(paging);
            categoryList = pageCategories.getContent();
            return CategoryResponse
                    .builder()
                    .categoryList(categoryList)
                    .currentPage(pageCategories.getNumber())
                    .totalItems(pageCategories.getTotalElements())
                    .totalPages(pageCategories.getTotalPages())
                    .build();
        }catch (Exception e){
            System.out.println(e);
            return null;
        }
    }

    public CategoryResponse getAll(){
        List<ProductCategory> categories = new ArrayList<>();

        categoryRepository.findAll().forEach(categories::add);

        return CategoryResponse.builder()
                .categoryList(categories)
                .build();
    }

    //delete
    public void deleteCategory(UUID id) {
        List<Variation> variations = variationRepository.getByCategoryId(id);
        variations.forEach(variation -> {
            variationService.deleteVariation(variation.getId());
        });
        List<Product> products = productRepository.getProductsByCategoryId(id);
        products.forEach(product -> {
            productService.deleteProduct(product.getId());
        });
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
