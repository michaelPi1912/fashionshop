package com.huuphucdang.fashionshop.repository;

import com.huuphucdang.fashionshop.model.entity.Order;
import com.huuphucdang.fashionshop.model.entity.UserReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<UserReview, UUID>, JpaSpecificationExecutor<UserReview> {
    @Query("""
            select r from UserReview r 
            where r.user.id = :userId
            """)
    List<UserReview> findAllByUserId(UUID userId);
    @Query("""
            Select r From UserReview r Where r.product.id = :productId and r.active = true
            """)
    Page<UserReview> findAllByProduct(Pageable paging, UUID productId);
    @Query("""
            Select r From UserReview r Where r.product.id = :productId and r.active = true
            """)
    List<UserReview> findAllByProductId(UUID productId);
//    @Query("""
//            select r from UserReview r
//            where r.orderLine.product.id = :productId
//            """)
//    List<UserReview> findAllByProductId(UUID productId);
}
