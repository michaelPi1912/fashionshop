package com.huuphucdang.fashionshop.repository;

import com.huuphucdang.fashionshop.model.entity.Product;
import com.huuphucdang.fashionshop.model.entity.ProductItem;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ProductItemRepository extends JpaRepository<ProductItem, UUID>, JpaSpecificationExecutor<ProductItem> {
    @Query("""
            Select p From  ProductItem p Where p.product.id = :productId
            """)
    List<ProductItem> findAllByProductId(UUID productId);
    @Transactional
    @Modifying
    @Query(
            value = "delete from product_configuration p where p.product_item_id = :id",
            nativeQuery = true
    )
    void deleteItemById(UUID id);

    @Query(value = "select * from product_item p inner join product_configuration c on p.id = c.product_item_id " +
            "where p.id IN (select * from (select c.product_item_id from product_configuration c " +
            "where c.variation_option_id = :optionId) b)", nativeQuery = true)
    List<ProductItem> findByOption(UUID optionId);

}
