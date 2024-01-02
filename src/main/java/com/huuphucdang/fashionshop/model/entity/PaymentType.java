package com.huuphucdang.fashionshop.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payment_type")
public class PaymentType {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;
    private String name;
    @JsonIgnore
    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL)
    private Set<Order> orders;
}
