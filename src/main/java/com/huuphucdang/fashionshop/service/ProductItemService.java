package com.huuphucdang.fashionshop.service;

import com.huuphucdang.fashionshop.model.entity.Product;
import com.huuphucdang.fashionshop.model.entity.ProductItem;
import com.huuphucdang.fashionshop.model.entity.VariationOption;
import com.huuphucdang.fashionshop.model.payload.request.ProductItemRequest;
import com.huuphucdang.fashionshop.model.payload.response.ProductItemResponse;
import com.huuphucdang.fashionshop.repository.ProductItemRepository;
import com.huuphucdang.fashionshop.repository.ProductRepository;
import com.huuphucdang.fashionshop.repository.VariationOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductItemService {

    private final ProductRepository productRepository;
    private final ProductItemRepository productItemRepository;
    private final VariationOptionRepository optionRepository;

    public void saveProductItem(ProductItemRequest body) {
        ProductItem productItem = new ProductItem();
        Product product = productRepository.findById(body.getProductId()).orElseThrow();
        productItem.setName(product.getName());
        productItem.setProduct(product);
        productItem.setProductImages(body.getImages());
        productItem.setSKU(body.getCode());
        productItem.setPrice(body.getPrice());
        productItem.setStock((body.getStock()));

        productItemRepository.save(productItem);

        for(UUID uuid: body.getOptionsId()){
            VariationOption option = optionRepository.findById(uuid).orElseThrow();
            productItem.getOptions().add(option);
            optionRepository.save(option);
        }
    }



    public void addOption(UUID id, ProductItemRequest body) {
        ProductItem productItem = productItemRepository.findById(id).orElseThrow();
        for(UUID uuid: body.getOptionsId()){
            VariationOption option = optionRepository.findById(uuid).orElseThrow();
            productItem.getOptions().add(option);
            optionRepository.save(option);
        }
    }

    public ProductItemResponse getByProductId(UUID id) {
        List<ProductItem> productItems = productItemRepository.findAllByProductId(id);

        return ProductItemResponse.builder()
                .productItems(productItems).build();
    }

    public void deleteById(UUID id) {
        productItemRepository.deleteItemById(id);
        productItemRepository.deleteById(id);
    }

    public void updateItem(UUID id, ProductItemRequest body) {
        ProductItem productItem = productItemRepository.findById(id).orElseThrow();

        productItem.setProductImages(body.getImages());
        productItem.setSKU(body.getCode());
        productItem.setPrice(body.getPrice());
        productItem.setStock((body.getStock()));

        productItemRepository.save(productItem);
    }

    public ProductItemResponse getAllOptionByProduct(UUID id, int page, int size, int inStock, String color, String sizes, int price, String sku) {
        try{
            List<ProductItem> productItems;
            List<ProductItem> checkOption;
            Specification<ProductItem> spec = Specification.where(null);
            spec = spec.and((root, query, cb) -> cb.equal(root.get("product").get("id"), id));

            if(!sku.isEmpty()){
                spec = spec.and((root, query, cb) -> cb.like(root.get("SKU"), "%"+sku+"%"));
            }

            System.out.println(inStock);

            if(inStock == 1){
                spec = spec.and((root, query, cb) -> cb.greaterThan(root.get("stock"), 0));
            }
            if(inStock == 2){
                spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("stock"), 0));
            }

            Pageable paging = PageRequest.of(page, size);


            //
            Specification<ProductItem> spec1 = Specification.where(null);
            spec1 = spec1.and((root, query, cb) -> cb.equal(root.get("product").get("id"), id));
            if(!color.isEmpty() && !sizes.isEmpty()){
                spec = spec.and((root, query, cb) -> cb.like(root.get("options").get("value"), color)).and((root, query, cb) -> cb.like(root.get("options").get("variation").get("name"), "Color"));
                spec1 = spec1.and((root, query, cb) -> cb.like(root.get("options").get("value"), sizes));

            }else if(!sizes.isEmpty() && color.isEmpty()){
                spec = spec.and((root, query, cb) -> cb.like(root.get("options").get("value"), sizes));
            }else if(sizes.isEmpty() && !color.isEmpty()){
                spec = spec.and((root, query, cb) -> cb.like(root.get("options").get("value"), color)).and((root, query, cb) -> cb.like(root.get("options").get("variation").get("name"), "Color"));
            }

            Page<ProductItem> pageProducts = productItemRepository.findAll(spec,paging);

            Page<ProductItem> pageProductsCheck = productItemRepository.findAll(spec1,paging);

            productItems = pageProducts.getContent();
            checkOption = pageProductsCheck.getContent();

            if(!color.isEmpty() &&!sizes.isEmpty()){
                productItems = productItems.stream().filter(productItem ->
                        checkOption.indexOf(productItem) != -1
                ).collect(Collectors.toList());
            }


            productItems.forEach(productItem -> {
                Set<VariationOption> options = optionRepository.findByProductItemId(productItem.getId());
                productItem.setOptions(options);
            });

            return ProductItemResponse
                    .builder()
                    .productItems(productItems)
                    .currentPage(pageProducts.getNumber())
                    .totalItems(pageProducts.getTotalElements())
                    .totalPages(pageProducts.getTotalPages())
                    .build();
        }catch (Exception e){
            System.out.println(e);
            return null;
        }
    }

//    public ProductItemResponse getByTest(UUID id, String color, String size) {
//        List<ProductItem> productItems = productItemRepository.findAllByTest(id,color,size);
//
//        return ProductItemResponse.builder()
//                .productItems(productItems).build();
//    }
}
