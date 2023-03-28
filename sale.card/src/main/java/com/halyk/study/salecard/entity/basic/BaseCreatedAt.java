package com.halyk.study.salecard.entity.basic;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import java.time.LocalDate;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseCreatedAt extends BaseId {

    private LocalDate createdAt;

}
