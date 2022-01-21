package com.store.store.common;

import com.store.store.common.enums.ItemType;
import com.store.store.common.enums.UserType;
import com.store.store.common.entity.Invoice;
import com.store.store.common.entity.Items;
import com.store.store.common.entity.User;
import com.store.store.common.request.ItemPurchaseRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class TestUtils {


    public static List<ItemPurchaseRequest> getListOfItems() {
        ItemPurchaseRequest itemPurchaseRequest = new ItemPurchaseRequest();
        itemPurchaseRequest.setId(1l);
        itemPurchaseRequest.setPrice(BigDecimal.valueOf(100));
        itemPurchaseRequest.setQuantity(2);
        itemPurchaseRequest.setItemType(ItemType.ELECTRONICS);
        ItemPurchaseRequest itemPurchaseRequest1 = new ItemPurchaseRequest();
        itemPurchaseRequest1.setId(1l);
        itemPurchaseRequest1.setPrice(BigDecimal.valueOf(100));
        itemPurchaseRequest1.setQuantity(2);
        itemPurchaseRequest1.setItemType(ItemType.GROCERIES);
        return Arrays.asList(itemPurchaseRequest, itemPurchaseRequest1);
    }

    public static Invoice getInvoiceEntity() {
    return Invoice.builder()
            .percentageDiscountAmount(BigDecimal.valueOf(5))
            .netAmount(BigDecimal.valueOf(95))
            .totalAmount(BigDecimal.valueOf(100))
            .user(getUser())
            .build();
    }

    private static User getUser() {
        return User.builder()
                .email("test@email.com")
                .name("test")
                .registeredDate(LocalDateTime.now())
                .userType(UserType.EMPLOYEE)
                .build();
    }

    public static Items getSavedItem() {
        return Items.builder()
                .details("details")
                .itemType(ItemType.ELECTRONICS)
                .name("name")
                .price(BigDecimal.valueOf(100))
                .quantity(5)
                .build();
    }
}
