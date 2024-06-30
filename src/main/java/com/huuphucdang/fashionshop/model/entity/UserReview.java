package com.huuphucdang.fashionshop.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_review")
public class UserReview {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private UserReview parent;
    @JsonIgnore
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<UserReview> reviews;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    @ManyToOne
    @JoinColumn(name = "ordered_product_id")
    private Product product;

    @Column(name = "rating_value")
    private Integer ratingValue;
    private String comment;
    private boolean active;
    private Date commentDate;
    private String fit;
    private String gender;
    private String age;
    private String height;
    private String weight;
    private String size;
}
