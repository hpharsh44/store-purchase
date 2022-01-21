package com.store.store.service;

import com.store.store.common.exception.BadRequestException;
import com.store.store.common.enums.ItemType;
import com.store.store.common.exception.NotFoundException;
import com.store.store.common.enums.UserType;
import com.store.store.common.dto.InvoiceDto;
import com.store.store.common.entity.Invoice;
import com.store.store.common.entity.Items;
import com.store.store.common.entity.User;
import com.store.store.common.repository.InvoiceRepository;
import com.store.store.common.repository.ItemRepository;
import com.store.store.common.repository.UserRepository;
import com.store.store.common.request.ItemPurchaseRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PurchaseService {


    private final UserRepository userRepository;

    private final ItemRepository itemRepository;

    private final InvoiceRepository invoiceRepository;

    private static long getDiffYears(LocalDateTime first, LocalDateTime last) {
        LocalDateTime tempDateTime = LocalDateTime.from(first);

        return tempDateTime.until(last, ChronoUnit.YEARS);
    }

    public InvoiceDto purchaseItems(List<ItemPurchaseRequest> request, Long userId) {
        validateRequest(request,userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found by id: " + userId));


        List<ItemPurchaseRequest> otherItemDetails = request.stream().filter(p -> p.getItemType().compareTo(ItemType.GROCERIES) == 1).collect(Collectors.toList());
        log.info("Non groceries items {}",otherItemDetails);
        List<ItemPurchaseRequest> groceriesItemDetails = request.stream().filter(p -> p.getItemType().compareTo(ItemType.GROCERIES) == 0).collect(Collectors.toList());
        log.info("Groceries items {}",otherItemDetails);
        BigDecimal grossAmountForOtherItems = getGrossAmountForOtherItems(otherItemDetails);
        log.info("GrossAmount for non groceriesItem: "+grossAmountForOtherItems);
        BigDecimal grossAmountForGroceriesItems = getGrossAmountForOtherItems(groceriesItemDetails);
        log.info("GrossAmount for groceriesItem: "+grossAmountForGroceriesItems);
        BigDecimal grossAmount = grossAmountForOtherItems.add(grossAmountForGroceriesItems);
        log.info("GrossAmount: "+grossAmount);

        BigDecimal percentageDiscount = calculateRateByPercentage(user, grossAmountForOtherItems);

        BigDecimal tempGrossAmount = grossAmount.subtract(percentageDiscount);

        BigDecimal flatDiscount = calculateRateByFaltDiscount(tempGrossAmount);

        BigDecimal netAmount = tempGrossAmount.subtract(flatDiscount);

        Invoice invoice = new Invoice();
        invoice.setPercentageDiscountAmount(percentageDiscount);
        invoice.setFlatDiscountAmount(flatDiscount);
        invoice.setNetAmount(netAmount);
        invoice.setTotalAmount(grossAmount);
        invoice.setUser(user);
        Invoice savedInvoice = invoiceRepository.save(invoice);
        return InvoiceDto.build(savedInvoice);
    }

    private BigDecimal calculateRateByPercentage(User user, BigDecimal totalAmount) {
        long diffYear = getDiffYears(user.getRegisteredDate(), LocalDateTime.now());
        if (user.getUserType().equals(UserType.EMPLOYEE)) {
            log.info("Rate calculation for userType EMPLOYEE which is 30%.");
            return calculateDiscount(totalAmount, BigDecimal.valueOf(30));
        } else if (user.getUserType().equals(UserType.AFFILIATE)) {
            log.info("Rate calculation for userType AFFILIATE which is 10%.");
            return calculateDiscount(totalAmount, BigDecimal.valueOf(10));
        } else if (diffYear >= 2) {
            log.info("Rate calculation for customer who has registered over two years which is 5%.");
            return calculateDiscount(totalAmount, BigDecimal.valueOf(5));
        } else {
            log.info("User is not valid for percentage discount.");
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal calculateDiscount(BigDecimal totalAmount, BigDecimal discount) {
        return (totalAmount.multiply(discount)).divide(BigDecimal.valueOf(100));
    }

    private BigDecimal calculateRateByFaltDiscount(BigDecimal grossAmount) {
        Integer number = grossAmount.divide(new BigDecimal(100)).intValue();
        if (number <= 0) {
            return BigDecimal.ZERO;
        } else {
            BigDecimal discountAmount = BigDecimal.valueOf(number*5);
            return discountAmount;
        }
    }

    public BigDecimal getGrossAmountForOtherItems(List<ItemPurchaseRequest> itemPurchaseRequestList) {
        return itemPurchaseRequestList.stream().map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity()))).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void validateRequest(List<ItemPurchaseRequest> item, Long userId) {

        if (CollectionUtils.isEmpty(item)) {
            log.error("Itemlist is empty in input.");
            throw new BadRequestException("Itemlist is empty in input.");
        }
        List<ItemPurchaseRequest> itemDetails = item.stream().filter(p -> p.getPrice().compareTo(BigDecimal.ZERO) == 0 || p.getQuantity().compareTo(0) == 0).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(itemDetails)) {
            log.error("UnitPrice is ZERO Or quantity is ZERO for Item :" + itemDetails.get(0).getId());
            throw new BadRequestException("UnitPrice is ZERO Or quantity is ZERO for Item :" + itemDetails.get(0).getId());
        }
        if (userId == null) {
            log.error("UserId is missing in input.");
            throw new BadRequestException("UserId is missing in input.");
        }

        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            log.error("user not found by id:" + userId);
            throw new NotFoundException("user not found by id:" + userId);
        }
        if (CollectionUtils.isNotEmpty(item)) {
            item.forEach(i -> {
                Items checkValidItem = itemRepository.findById(i.getId()).orElseThrow(() -> new NotFoundException("item not found by id: " + i.getId()));
                if (checkValidItem.getQuantity() <= 0 || checkValidItem.getQuantity() < i.getQuantity()) {
                    log.error("item not found by id: " + i.getId() + " Or may quantity is not matched with input: " + i.getQuantity());
                    throw new NotFoundException("item not found by id: " + i.getId() + " Or may quantity is not matched with input: " + i.getQuantity());
                }
            });
        }
        log.info("Request is validated.");
    }
}
