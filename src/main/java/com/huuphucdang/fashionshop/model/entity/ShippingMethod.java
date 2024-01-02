package com.huuphucdang.fashionshop.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "shipping_method")
public class ShippingMethod {

    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private Integer price;
    @JsonIgnore
    @OneToMany(mappedBy = "shippingMethod", cascade = CascadeType.ALL)
    private Set<Order> orders;
}
