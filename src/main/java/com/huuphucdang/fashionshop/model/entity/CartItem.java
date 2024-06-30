package com.huuphucdang.fashionshop.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "shopping_cart_item")
public class CartItem {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "shopping_cart_id")
    private Cart cart;
    @ManyToOne
    @JoinColumn(name = "product_item_id")
    private ProductItem product;

    private Integer quantity;
}
