package com.store.store.common.entity;

import com.store.store.common.enums.ItemType;
import com.store.store.common.entity.auditable.BaseRepoEntityAuditable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "item")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Items extends BaseRepoEntityAuditable<String, Long> {

    private String name;

    private String details;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ItemType itemType;

    private Integer quantity;

    private BigDecimal price;
}
