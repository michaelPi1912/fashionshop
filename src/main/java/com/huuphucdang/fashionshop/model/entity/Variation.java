package com.huuphucdang.fashionshop.model.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "variation")
public class Variation {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonIgnore
    private ProductCategory category;
    private String name;
    @JsonIgnore
    @OneToMany(mappedBy = "variation", cascade = CascadeType.ALL)
    private Set<VariationOption> variationOptions;

}
