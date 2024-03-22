package com.huuphucdang.fashionshop.repository;

import com.huuphucdang.fashionshop.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    @Query("""
            select p
            from Product p
            where p.productCategory.id = :categoryId
            """)
    List<Product> getProductsByCategoryId(UUID categoryId);
    @Query("""
            select p
            from Product p
            where p.productCategory.name = :categoryName
            """)
    List<Product> getProductsByCategoryName(String categoryName);
}
