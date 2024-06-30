package com.huuphucdang.fashionshop.repository;

import com.huuphucdang.fashionshop.model.entity.Coupon;
import com.huuphucdang.fashionshop.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CouponRepository  extends JpaRepository<Coupon, UUID>, JpaSpecificationExecutor<Coupon> {
    @Query("""
            Select c From Coupon c Where c.code  = :code
            """)
    Coupon findByCode(String code);
}
