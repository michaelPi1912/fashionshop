package com.huuphucdang.fashionshop.repository;

import com.huuphucdang.fashionshop.model.entity.Cart;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {

    @Query("""
            select a
            from Cart a  
            where a.user.id = :userId
            """)
    List<Cart> findCartByUser(UUID userId);
    @Transactional
    @Modifying
    @Query(value = "delete from shopping_cart c where c.user_id = :userId", nativeQuery = true)
    void deleteAllByUser(UUID userId);
}
