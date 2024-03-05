package com.huuphucdang.fashionshop.repository;

import com.huuphucdang.fashionshop.model.entity.UserReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<UserReview, UUID> {
    @Query("""
            select r from UserReview r 
            where r.user.id = :userId
            """)
    List<UserReview> findAllByUserId(UUID userId);
    @Query("""
            select r from UserReview r 
            where r.orderLine.product.id = :productId
            """)
    List<UserReview> findAllByProductId(UUID productId);
}
