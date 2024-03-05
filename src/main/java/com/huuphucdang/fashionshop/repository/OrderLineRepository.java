package com.huuphucdang.fashionshop.repository;

import com.huuphucdang.fashionshop.model.entity.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface OrderLineRepository extends JpaRepository<OrderLine, UUID> {

    @Query("""
            Select l from OrderLine l where l.order.id = :orderId
            """)
    List<OrderLine> findAllByOrderId(UUID orderId);
}
