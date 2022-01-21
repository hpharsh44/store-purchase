package com.store.store.common.request;

import com.store.store.common.enums.ItemType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ItemPurchaseRequest {

    private Long id;

    private Integer quantity;

    private ItemType itemType;

    private BigDecimal price;

}
