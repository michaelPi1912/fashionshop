package com.huuphucdang.fashionshop.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_category")
public class ProductCategory {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private ProductCategory parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private Collection<ProductCategory> categories;
    @JsonIgnore
    @OneToMany(mappedBy = "productCategory", cascade = CascadeType.ALL)
    private List<Product> products;

    @JsonIgnore
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private Set<Variation> variations;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },
            mappedBy = "promotionCategories")
    @JsonIgnore
    private Set<Promotion> promotions = new HashSet<>();

    public void addPromotion(Promotion promotion) {
        this.promotions.add(promotion);
        promotion.getPromotionCategories().add(this);
    }

    public void removePromotion(UUID promotionId) {
        Promotion promotion = this.promotions.stream().filter(t -> t.getId() == promotionId).findFirst().orElse(null);
        if (promotion != null) {
            this.promotions.remove(promotion);
            promotion.getPromotionCategories().remove(this);
        }
    }
}
