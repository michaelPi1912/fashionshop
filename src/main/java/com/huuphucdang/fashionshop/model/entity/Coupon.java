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
@Table(name = "coupon")
public class Coupon {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;
    @Column(unique = true)
    private String code;
    private String name;
    private String description;
    private DiscountType discountType;
    private Integer amount;
    private Date startDate;
    private Date endDate;
    private Integer limitUsage;
    private Integer times;
    private Integer maxValue;

}
