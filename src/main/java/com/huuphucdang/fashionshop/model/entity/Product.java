package com.huuphucdang.fashionshop.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
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
@Table(name = "product")
public class Product {
    @Id
    @UuidGenerator
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "product_category_id")
    private ProductCategory productCategory;
    private String name;
    private String description;
    private String productImage;
    private Integer price;
    private Integer sold;
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "product_configuration",
            joinColumns = { @JoinColumn(name = "product_id") },
            inverseJoinColumns = { @JoinColumn(name = "variation_option_id") })
    private Set<VariationOption> options = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<CartItem> cartItems;
    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<OrderLine> orderLines;


    public void addVariationOption(VariationOption variationOption) {
        this.options.add(variationOption);
        variationOption.getProducts().add(this);
    }

    public void removeVariationOption(UUID variationOptionId) {
        VariationOption variationOption = this.options.stream().filter(t -> t.getId() == variationOptionId).findFirst().orElse(null);
        if (variationOption != null) {
            this.options.remove(variationOption);
            variationOption.getProducts().remove(this);
        }
    }
}
