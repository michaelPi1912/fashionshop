package com.huuphucdang.fashionshop.repository;

import com.huuphucdang.fashionshop.model.entity.Promotion;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Transactional
public interface PromotionRepository extends JpaRepository<Promotion, UUID> {
    @Query("""
            select p
            from Promotion p
            """)
    List<Promotion> getAll();

    @Query(value = "Select p.id, p.description, p.discount_rate, p.end_date, p.start_date, p.name from promotion p " +
            "inner join promotion_category t " +
            "on p.id = t.promotion_id " +
            "where t.product_category_id = :categoryId",
    nativeQuery = true)
    List<Promotion> findPromotionsByCategoryId(@Param("categoryId")UUID categoryId);
    @Modifying
    @Query(
            value = "delete from promotion_category p where p.promotion_id = :promotionId and p.product_category_id = :categoryId",
            nativeQuery = true
    )
    void deletePromotionFromCategory(@Param("categoryId") UUID categoryId,@Param("promotionId") UUID promotionId);
    //get category by promotion ID

}
