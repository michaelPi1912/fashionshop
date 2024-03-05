package com.huuphucdang.fashionshop.repository;

import com.huuphucdang.fashionshop.model.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderStatusRepository extends JpaRepository<OrderStatus, Integer> {
}
