package com.huuphucdang.fashionshop.service;

import com.huuphucdang.fashionshop.model.entity.*;
import com.huuphucdang.fashionshop.model.payload.request.AddressRequest;
import com.huuphucdang.fashionshop.model.payload.request.ProductRequest;
import com.huuphucdang.fashionshop.model.payload.response.OptionResponse;
import com.huuphucdang.fashionshop.model.payload.response.ProductResponse;
import com.huuphucdang.fashionshop.model.payload.response.PromotionResponse;
import com.huuphucdang.fashionshop.repository.CategoryRepository;
import com.huuphucdang.fashionshop.repository.ProductRepository;
import com.huuphucdang.fashionshop.repository.VariationOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final VariationOptionRepository optionRepository;

    public Product saveProduct(ProductRequest body){
        Product product = new Product();
        ProductCategory category = categoryRepository.findById(body.getCategoryId()).orElseThrow();
        product.setProductCategory(category);
        product.setName(body.getName());
        product.setDescription(body.getDescription());
        product.setProductImage(body.getImage());
        product.setPrice(body.getPrice());
        return productRepository.save(product);
    }


    public ProductResponse getAll(){

        List<Product> productList = productRepository.findAll();

        return ProductResponse.builder()
                .productList(productList).build();
    }

    public ProductResponse getAllByCategoryId(UUID categoryId){

        List<Product> productList = productRepository.getProductsByCategoryId(categoryId);

        return ProductResponse.builder()
                .productList(productList).build();
    }
    public Product updateProduct(ProductRequest body, UUID id){
        Product product = productRepository.findById(id).orElseThrow();
        ProductCategory category = categoryRepository.findById(body.getCategoryId()).orElseThrow();
        product.setProductCategory(category);
        product.setName(body.getName());
        product.setDescription(body.getDescription());
        product.setProductImage(body.getImage());
        product.setPrice(body.getPrice());
        return productRepository.save(product);
    }

    public void deleteProduct(UUID id) {
        productRepository.deleteById(id);
    }

    public Product getProductById(UUID id) {
        Product product = productRepository.findById(id).orElseThrow();

        return product;
    }

    public Product addOption(UUID productId, UUID optionId) {
        Product product = productRepository.findById(productId).orElseThrow();
        VariationOption option = optionRepository.findById(optionId).orElseThrow();
        product.getOptions().add(option);

        return productRepository.save(product);
    }

    public void deleteOptionFromProduct(UUID productId, UUID optionId) {
        optionRepository.deleteOptionFromProduct(productId, optionId);
    }

    public OptionResponse findOptionsByProduct(UUID productId) {
        if (!productRepository.existsById(productId)) {
            return null;
        }

        List<VariationOption> optionList = optionRepository.findOptionsByProductId(productId);

        return OptionResponse
                .builder()
                .optionList(optionList).build();
    }
}
