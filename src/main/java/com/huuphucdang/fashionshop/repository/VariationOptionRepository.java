package com.huuphucdang.fashionshop.repository;

import com.huuphucdang.fashionshop.model.entity.VariationOption;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface VariationOptionRepository extends JpaRepository<VariationOption, UUID> {
    @Query("""
            select o
            from VariationOption o
            where o.variation.id = :variationId
            """)
    Set<VariationOption> findOptionsByVariationId(UUID variationId);

    @Transactional
    @Modifying
    @Query(
            value = "delete from product_configuration p " +
                    "where p.product_item_id IN(select * from (select c.product_item_id from product_configuration c " +
                    "where c.variation_option_id = :id) b)",
            nativeQuery = true
    )
    void deleteOptionById(UUID id);



    @Query(value = "select o.id, o.value, o.variation_id from variation_option o inner join " +
            "product_configuration p on p.variation_option_id = o.id where  p.product_item_id = :productItemId", nativeQuery = true)
    Set<VariationOption> findByProductItemId(UUID productItemId);


}
