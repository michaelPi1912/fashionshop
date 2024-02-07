package com.huuphucdang.fashionshop.repository;

import com.huuphucdang.fashionshop.model.entity.Variation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface VariationRepository extends JpaRepository<Variation, UUID> {
    @Query("""
            select v
            from Variation v
            where v.category.id = :categoryId
            """)
    List<Variation> getProductsByCategoryId(UUID categoryId);

}
