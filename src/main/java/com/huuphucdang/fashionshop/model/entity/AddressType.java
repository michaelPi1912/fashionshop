package com.huuphucdang.fashionshop.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "address_type")
public class AddressType {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    @JsonIgnore
    @OneToMany(mappedBy = "addressType", cascade = CascadeType.ALL)
    private List<Address> addresses;
}
