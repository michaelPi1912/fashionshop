package com.huuphucdang.fashionshop.repository;

import com.huuphucdang.fashionshop.model.entity.Order;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID>, JpaSpecificationExecutor<Order> {

    @Query("""
            Select o from Order o where o.user.id = :id 
            """)
    Page<Order> findAllByUserId(UUID id, Pageable paging);
    @Query("""
            Select o from Order o where o.code Like :code
            """)
    List<Order> checkCode(String code);
    @Query("""
            Select o from Order o where o.orderStatus.status = :status
            """)
    Page<Order> findByStatus(Pageable paging, String status);
}
