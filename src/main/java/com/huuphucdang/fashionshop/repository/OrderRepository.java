package com.huuphucdang.fashionshop.repository;

import com.huuphucdang.fashionshop.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    @Query("""
            Select o from Order o where o.user.id = :id
            """)
    List<Order> findAllByUserId(UUID id);
}
