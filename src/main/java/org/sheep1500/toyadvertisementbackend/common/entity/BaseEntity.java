package org.sheep1500.toyadvertisementbackend.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public abstract class BaseEntity {
    @Column(name = "reg_date", nullable = false)
    private LocalDateTime regDate;

    @Column(name = "upt_date")
    private LocalDateTime uptDate;

    @Column(name = "del_date")
    private LocalDateTime delDate;

    public BaseEntity() {this.regDate = LocalDateTime.now();}
}
