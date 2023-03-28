package com.halyk.study.salecard.entity;

import com.halyk.study.salecard.entity.basic.BaseCreatedAt;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users_cards")
public class UserCard extends BaseCreatedAt {

    @ManyToOne
    @JoinColumn(
            name = "user_id"
    )
    private User user;

    @ManyToOne
    @JoinColumn(
            name = "product_id"
    )
    private Product product;

    private String barcode;

}
