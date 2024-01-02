package com.huuphucdang.fashionshop.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "promotion")
public class Promotion {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;
    private String name;
    private String description;
    private float discountRate;
    private Date startDate;
    private Date endDate;
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "promotion_category",
            joinColumns = { @JoinColumn(name = "promotion_id") },
            inverseJoinColumns = { @JoinColumn(name = "product_category_id") })
    private Set<ProductCategory> promotionCategories = new HashSet<>();
}
