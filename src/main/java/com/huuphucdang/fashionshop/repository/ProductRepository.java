package com.huuphucdang.fashionshop.repository;

import com.huuphucdang.fashionshop.model.entity.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {
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



    @Transactional  
    @Modifying
    @Query(
            value = "delete from wish_list w where w.user_id = :userId and w.product_id = :productId",
            nativeQuery = true
    )
    void deleteProductWishList(UUID userId, UUID productId);

    @Transactional
    @Modifying
    @Query(
            value = "delete from wish_list w where w.product_id = :productId",
            nativeQuery = true
    )
    void deleteProductWishListAdmin(UUID productId);
    @Query(value = "Select * from product p  inner join wish_list w " +
            "on p.id = w.product_id and p.id = :productId " +
            "where w.user_id = :userId", nativeQuery = true)
    List<Product> checkWishList(UUID userId, UUID productId);
    @Query(value = "Select * from product p inner join wish_list w " +
            "on p.id = w.product_id " +
            "where w.user_id = :id", nativeQuery = true)
    Page<Product> getProductByUserId(Pageable paging, UUID id);
}
