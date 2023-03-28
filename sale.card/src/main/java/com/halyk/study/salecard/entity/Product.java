package com.halyk.study.salecard.entity;

import com.halyk.study.salecard.entity.basic.BaseCreatedAt;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product extends BaseCreatedAt {

    @Column(unique = true)
    private String name;

    private String description;

    private String iconPath;

    private String color;

    private Integer number;

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "product"
    )
    private List<UserCard> userCards = new ArrayList<>();

}
