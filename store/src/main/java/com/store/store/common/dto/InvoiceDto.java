package com.store.store.common.dto;

import com.store.store.common.entity.Invoice;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

@Data
@Builder
public class InvoiceDto {

    private Long id;

    private String customerName;

    private BigDecimal totalAmount;

    private BigDecimal percentageDiscountAmount;

    private BigDecimal flatDiscountAmount;

    private BigDecimal netAmount;

    private UserDto userDto;

    public static InvoiceDto build(Invoice invoice) {

        return InvoiceDto.builder()
                .customerName(invoice.getUser()!=null && StringUtils.isNotEmpty(invoice.getUser().getName())?invoice.getUser().getName():StringUtils.EMPTY)
                .totalAmount(invoice.getTotalAmount())
                .percentageDiscountAmount(invoice.getPercentageDiscountAmount())
                .netAmount(invoice.getNetAmount())
                .flatDiscountAmount(invoice.getFlatDiscountAmount())
                .build();
    }
}
