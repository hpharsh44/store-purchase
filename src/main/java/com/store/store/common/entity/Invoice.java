package com.store.store.common.entity;

import com.store.store.common.entity.auditable.BaseRepoEntityAuditable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "invoice")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Invoice extends BaseRepoEntityAuditable<String, Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private BigDecimal totalAmount;

    private BigDecimal percentageDiscountAmount;

    private BigDecimal flatDiscountAmount;

    private BigDecimal netAmount;

}
