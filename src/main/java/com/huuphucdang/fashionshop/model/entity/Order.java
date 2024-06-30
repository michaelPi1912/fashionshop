package com.huuphucdang.fashionshop.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@Table(name = "shop_order")
public class Order {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "order_date")
    private Date orderDate;
    private String code;
    private String address;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private Set<OrderLine> orderLines;
    private String paymentType;
    private String shippingMethod;
    private Integer shipCost;
    @Column(name = "order_total")
    private double orderTotal;
    @ManyToOne
    @JoinColumn(name = "order_status_id")
    private OrderStatus orderStatus;
    private String phone;
    private Date deliveryDate;

    private Set<String> coupons = new HashSet<>();
    private String description;

}
