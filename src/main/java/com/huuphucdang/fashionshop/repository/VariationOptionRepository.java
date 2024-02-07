package com.huuphucdang.fashionshop.repository;

import com.huuphucdang.fashionshop.model.entity.VariationOption;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface VariationOptionRepository extends JpaRepository<VariationOption, UUID> {
    @Query("""
            select o
            from VariationOption o
            where o.variation.id = :variationId
            """)
    List<VariationOption> findOptionsByVariationId(UUID variationId);
    @Transactional
    @Modifying
    @Query(
            value = "delete from product_configuration p where p.product_id = :productId and p.variation_option_id = :optionId",
            nativeQuery = true
    )
    void deleteOptionFromProduct(@Param("productId") UUID productId,@Param("optionId") UUID optionId);
    @Query(value = "Select o.id, o.value, o.variation_id from variation_option o " +
            "inner join product_configuration p " +
            "on o.id = p.variation_option_id " +
            "where p.product_id = :productId",
            nativeQuery = true)
    List<VariationOption> findOptionsByProductId(UUID productId);
}
