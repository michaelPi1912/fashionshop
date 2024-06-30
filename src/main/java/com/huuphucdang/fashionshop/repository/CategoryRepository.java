package com.huuphucdang.fashionshop.repository;

import com.huuphucdang.fashionshop.model.entity.ProductCategory;
import com.huuphucdang.fashionshop.model.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface CategoryRepository extends JpaRepository<ProductCategory, UUID> {

    @Query("""
            select c
            from ProductCategory c where c.parent.id is null
            """)
    List<ProductCategory> getParentCategory();

    @Query("""
            select p
            from ProductCategory p
            """)
    List<ProductCategory> getAll();

    @Query(value = "Select c.id, c.name, c.parent_id from product_category c " +
            "inner join promotion_category t " +
            "on c.id = t.product_category_id " +
            "where t.promotion_id = :promotionId",
            nativeQuery = true)
    List<ProductCategory> findCategoriesByPromotionId(@Param("promotionId")UUID promotionId);

}
