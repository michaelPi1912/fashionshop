package com.huuphucdang.fashionshop.repository;

import com.huuphucdang.fashionshop.model.entity.PaymentType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentMethodRepository extends JpaRepository<PaymentType, Integer> {
}
