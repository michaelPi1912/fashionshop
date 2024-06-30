package com.huuphucdang.fashionshop.service;

import com.huuphucdang.fashionshop.model.entity.*;
import com.huuphucdang.fashionshop.model.payload.request.FilterRequest;
import com.huuphucdang.fashionshop.model.payload.request.ProductRequest;
import com.huuphucdang.fashionshop.model.payload.response.ProductResponse;
import com.huuphucdang.fashionshop.repository.CategoryRepository;
import com.huuphucdang.fashionshop.repository.ProductItemRepository;
import com.huuphucdang.fashionshop.repository.ProductRepository;
import com.huuphucdang.fashionshop.repository.VariationOptionRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final VariationOptionRepository optionRepository;
    private final ProductItemRepository productItemRepository;

    public Product saveProduct(ProductRequest body){
        Product product = new Product();
        ProductCategory category = categoryRepository.findById(body.getCategoryId()).orElseThrow();
        product.setProductCategory(category);
        product.setName(body.getName());
        product.setDescription(body.getDescription());
        product.setProductImage(body.getImage());
        product.setSold(0);
        return productRepository.save(product);
    }


    public ProductResponse getAll(int page, int size, String category, String search){
        try{
            List<Product> productList;
            Specification<Product> spec = Specification.where(null);
            if(!search.isEmpty()){
                spec = spec.and((root, query, cb) -> cb.like(root.get("name"), "%"+search+"%"));
            }
            if(!category.isEmpty()){
                spec = spec.and((root, query, cb) -> cb.equal(root.get("productCategory").get("name"), category));
            }

            Pageable paging = PageRequest.of(page, size);
            Page<Product> pageProducts = productRepository.findAll(spec,paging);
            productList = pageProducts.getContent();

            return ProductResponse.builder()
                    .productList(productList)
                    .currentPage(pageProducts.getNumber())
                    .totalItems(pageProducts.getTotalElements())
                    .totalPages(pageProducts.getTotalPages())
                    .build();
        }catch (Exception e){
            System.out.println(e);
            return null;
        }

    }

    public ProductResponse getAllByCategoryId(UUID categoryId){

        List<Product> productList = productRepository.getProductsByCategoryId(categoryId);

        return ProductResponse.builder()
                .productList(productList).build();
    }
    public Product updateProduct(ProductRequest body, UUID id){
        Product product = productRepository.findById(id).orElseThrow();
        ProductCategory category = categoryRepository.findById(body.getCategoryId()).orElseThrow();
        if(body.getCategoryId() != product.getProductCategory().getId()){
            List<ProductItem> productItems  = productItemRepository.findAllByProductId(product.getId());
            productItems.forEach(productItem -> {
                productItemRepository.deleteItemById(productItem.getId());
            });
        }
        product.setProductCategory(category);
        product.setName(body.getName());
        product.setDescription(body.getDescription());
        product.setProductImage(body.getImage());
//        product.setPrice(body.getPrice());
//        product.setStock(body.getStock());
        return productRepository.save(product);
    }

    public void deleteProduct(UUID id) {
        List<ProductItem> productItems = productItemRepository.findAllByProductId(id);
        productItems.forEach(productItem -> {
            productRepository.deleteById(productItem.getId());
        });
        productRepository.deleteProductWishListAdmin(id);
        productRepository.deleteById(id);
    }

    public Product getProductById(UUID id) {
        Product product = productRepository.findById(id).orElseThrow();

        return product;
    }

    public ProductResponse getAllByCategoryName(String search, String categoryName, int page, int size, List<String> sizes, List<String> colors, int min, int max) {
        try {
            Specification<Product> spec = Specification.where(null);
            Specification<Product> spec1 = Specification.where(null);
            if(categoryName.equals("Men") || categoryName.equals("Women")){
                spec = spec.and((root, query, cb) -> {
                    query.distinct(true);
                    return cb.equal(root.get("productCategory").get("name"), categoryName);
                });
                spec1 = spec1.and((root, query, cb) -> cb.equal(root.get("productCategory").get("parent").get("name"),categoryName));
            }else{
                spec = spec.and((root, query, cb) ->
                {
                    query.distinct(true);
                    return cb.equal(root.get("productCategory").get("id"), UUID.fromString(categoryName));
                });
            }

//
            if(!sizes.isEmpty() || !colors.isEmpty()){
                sizes.addAll(colors);
                spec = spec.and((root, query, cb) -> root.get("productItem").get("options").get("value").in(sizes));
                spec1 = spec1.and((root, query, cb) -> root.get("productItem").get("options").get("value").in(sizes));

            }

            if(min != 0 && max != 0){
                spec = spec.and((root, query, cb) -> cb.between(root.get("productItem").get("price"), min, max));
                spec1 = spec1.and((root, query, cb) -> root.get("productItem").get("options").get("value").in(sizes));

            }

            Pageable paging = PageRequest.of(page, size);
            Page<Product> pageProducts = productRepository.findAll(spec,paging);
            List<Product> productList  = pageProducts.getContent();
            Page<Product> pageProduct1 = productRepository.findAll(spec1,paging);
            List<Product> productList1 = pageProduct1.getContent();

            if((categoryName.equals("Men") || categoryName.equals("Women")) && productList1.size() > 0){
//                System.out.println("l1 "+ productList1.size() + " l2 "+ productList.size());

                List<Product> newList = new ArrayList<>();
                newList.addAll(productList);
                newList.addAll(productList1);
                if(size <7 && newList.size() > 6){
                    newList.removeLast();
                }
                System.out.println("l1 "+ newList.size());
                long totalItems = pageProducts.getTotalElements()+pageProduct1.getTotalElements();
                int totalPages = (int) (totalItems/size +1);
                return ProductResponse.builder()
                        .productList(newList)
                        .currentPage(pageProducts.getNumber())
                        .totalItems(totalItems)
                        .totalPages(totalPages)
                        .build();
            }

            return ProductResponse.builder()
                    .productList(productList)
                    .currentPage(pageProducts.getNumber())
                    .totalItems(pageProducts.getTotalElements())
                    .totalPages(pageProducts.getTotalPages())
                    .build();
        }catch (Exception e){
            return  null;
        }

    }

    public ProductResponse getWishListByUser(UUID id, int page, int size) {
//        List<Product> products = productRepository.getProductByUserId(id);
//
//        return products;
        try{
            List<Product> productList;

            Pageable paging = PageRequest.of(page, size);
            Page<Product> pageProducts = productRepository.getProductByUserId(paging,id);
            productList = pageProducts.getContent();

            return ProductResponse.builder()
                    .productList(productList)
                    .currentPage(pageProducts.getNumber())
                    .totalItems(pageProducts.getTotalElements())
                    .totalPages(pageProducts.getTotalPages())
                    .build();
        }catch (Exception e){
            System.out.println(e);
            return null;
        }
    }


    public ProductResponse getAllByFilter(FilterRequest body, int page, int size) {
        try{
            List<Product> productList;
            Specification<Product> spec = Specification.where(null);
            System.out.println(body.getMax());
//            System.out.println(body.getCategory());
            spec = spec.and((root, query, cb) -> {
                query.distinct(true);
                return cb.like(root.get("name"), "%"+body.getKey()+"%");
            });

            if(!body.getCategory().equals("all")){
                spec = spec.and((root, query, cb) -> cb.equal(root.get("productCategory").get("name"), body.getCategory()));
            }
            if(!body.getSizes().isEmpty() || !body.getColors().isEmpty()){
                body.getSizes().addAll(body.getColors());
                spec = spec.and((root, query, cb) -> root.get("productItem").get("options").get("value").in(body.getSizes()));
            }
//            if(body.getPrices() != null){
//                if(body.getPrices() == 0){
//                    spec = spec.and((root, query, cb) -> cb.lt(root.get("productItem").get("price"), 199000));
//                } else if (body.getPrices() == 1) {
//                    spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("productItem").get("price"), 199000));
//                    spec = spec.and((root, query, cb) -> cb.lt(root.get("productItem").get("price"), 299000));
//                } else if (body.getPrices() ==2) {
//                    spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("productItem").get("price"), 299000));
//                    spec = spec.and((root, query, cb) -> cb.lt(root.get("productItem").get("price"), 399000));
//                } else if (body.getPrices() ==3) {
//                    spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("productItem").get("price"), 399000));
//                    spec = spec.and((root, query, cb) -> cb.lt(root.get("productItem").get("price"), 499000));
//                } else if (body.getPrices() ==4) {
//                    spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("productItem").get("price"),  499000));
//                }
//            }
            if(body.getMin() != null && body.getMax() != null){
                spec = spec.and((root, query, cb) -> cb.between(root.get("productItem").get("price"), body.getMin(), body.getMax()));
            }




            Pageable paging = PageRequest.of(page, size);
            Page<Product> pageProducts = productRepository.findAll(spec,paging);
            productList = pageProducts.getContent();
//
            System.out.println(productList.size());
            return ProductResponse
                    .builder()
                    .productList(productList)
                    .currentPage(pageProducts.getNumber())
                    .totalItems(pageProducts.getTotalElements())
                    .totalPages(pageProducts.getTotalPages())
                    .build();
        }catch (Exception e){
            System.out.println(e);
            return null;
        }
    }

    public ProductResponse getAllRelated(String category, UUID id) {
        try {
            Specification<Product> spec = Specification.where(null);
            spec = spec.and((root, query, cb) -> {
                    query.distinct(true);
                    return cb.equal(root.get("productCategory").get("name"), category);
            });

            spec = spec.and((root, query, cb) -> cb.notEqual(root.get("id"), id));


            Pageable paging = PageRequest.of(0, 4);
            Page<Product> pageProducts = productRepository.findAll(spec,paging);
            List<Product> productList  = pageProducts.getContent();


            return ProductResponse.builder()
                    .productList(productList)
                    .currentPage(pageProducts.getNumber())
                    .totalItems(pageProducts.getTotalElements())
                    .totalPages(pageProducts.getTotalPages())
                    .build();
        }catch (Exception e){
            return  null;
        }
    }
}
