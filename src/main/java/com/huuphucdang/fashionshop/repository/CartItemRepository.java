package com.huuphucdang.fashionshop.repository;

import com.huuphucdang.fashionshop.model.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItem, UUID> {

    @Query("""
            select i
            from CartItem i   
            where i.cart.id = :cartId
            """)
    List<CartItem> findAllByCartId(UUID cartId);

    @Query("""
            select i
            from CartItem i  
            where i.cart.id = :cartId and i.product.id = :productId
            """)
    CartItem findByCartIdNProductId(UUID cartId, UUID productId);
}
