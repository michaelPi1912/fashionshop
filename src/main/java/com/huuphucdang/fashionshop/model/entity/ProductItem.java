package com.huuphucdang.fashionshop.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_item")
public class ProductItem {

    @Id
    @UuidGenerator
    private UUID id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
    private String name;
    private Integer stock;
    private Integer sold;
    private String SKU;
    private List<String> productImages;
    private Integer price;
    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<CartItem> cartItems;
    @JsonIgnore
    @OneToMany(mappedBy = "productItem", cascade = CascadeType.ALL)
    private List<OrderLine> orderLines;

    @ManyToMany(fetch = FetchType.LAZY,
        cascade = {
                CascadeType.PERSIST,
                CascadeType.MERGE
        })
    @JoinTable(name = "product_configuration",
            joinColumns = { @JoinColumn(name = "product_item_id") },
            inverseJoinColumns = { @JoinColumn(name = "variation_option_id") })
    private Set<VariationOption> options = new HashSet<>();

}
