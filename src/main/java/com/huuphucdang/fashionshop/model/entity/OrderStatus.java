package com.huuphucdang.fashionshop.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_status")
public class OrderStatus {

    @Id
    @GeneratedValue
    private Integer id;
    private String status;
    @JsonIgnore
    @OneToMany(mappedBy = "orderStatus", cascade = CascadeType.ALL)
    private Set<Order> orders;
}
