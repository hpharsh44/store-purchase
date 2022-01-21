package com.store.store.service;

import com.store.store.common.*;
import com.store.store.common.enums.UserType;
import com.store.store.common.exception.BadRequestException;
import com.store.store.common.exception.NotFoundException;
import com.store.store.common.dto.InvoiceDto;
import com.store.store.common.entity.User;
import com.store.store.common.repository.InvoiceRepository;
import com.store.store.common.repository.ItemRepository;
import com.store.store.common.repository.UserRepository;
import com.store.store.common.request.ItemPurchaseRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import(PurchaseService.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PurchaseServiceTest {

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ItemRepository itemRepository;

    private ItemPurchaseRequest itemRequest;

    @Test
    public void purchase_ShouldReturn30PercentageDiscountAndFaltDiscountForMultipleTypeOfItem_ForEmployeeUser() {
        when(userRepository.findById(1l)).thenReturn(Optional.of(TestUtils.getInvoiceEntity().getUser()));
        when(itemRepository.findById(1l)).thenReturn(Optional.of(TestUtils.getSavedItem()));

        InvoiceDto invoiceDto = purchaseService.purchaseItems(TestUtils.getListOfItems(), 1l);

        assertEquals(BigDecimal.valueOf(325), invoiceDto.getNetAmount());
        assertEquals(BigDecimal.valueOf(400), invoiceDto.getTotalAmount());
        assertEquals(BigDecimal.valueOf(60), invoiceDto.getPercentageDiscountAmount());
        assertEquals(BigDecimal.valueOf(15), invoiceDto.getFlatDiscountAmount());
        assertEquals("test", invoiceDto.getCustomerName());

    }

    @Test
    public void purchase_ShouldReturn10PercentageAndFlatDiscount_ForAffiliateUser() {
        User user = TestUtils.getInvoiceEntity().getUser();
        user.setUserType(UserType.AFFILIATE);
        when(userRepository.findById(1l)).thenReturn(Optional.of(user));
        when(itemRepository.findById(1l)).thenReturn(Optional.of(TestUtils.getSavedItem()));

        InvoiceDto invoiceDto = purchaseService.purchaseItems(TestUtils.getListOfItems(), 1l);

        assertEquals(BigDecimal.valueOf(365), invoiceDto.getNetAmount());
        assertEquals(BigDecimal.valueOf(400), invoiceDto.getTotalAmount());
        assertEquals(BigDecimal.valueOf(20), invoiceDto.getPercentageDiscountAmount());
        assertEquals(BigDecimal.valueOf(15), invoiceDto.getFlatDiscountAmount());
        assertEquals("test", invoiceDto.getCustomerName());
    }

    @Test
    public void purchase_ShouldReturn5PercentageAndFlatDiscount_ForAffiliateUser() {
        User user = TestUtils.getInvoiceEntity().getUser();
        user.setUserType(UserType.OTHER);
        user.setRegisteredDate(LocalDateTime.now().minusYears(3));
        when(userRepository.findById(1l)).thenReturn(Optional.of(user));
        when(itemRepository.findById(1l)).thenReturn(Optional.of(TestUtils.getSavedItem()));

        InvoiceDto invoiceDto = purchaseService.purchaseItems(TestUtils.getListOfItems(), 1l);

        assertEquals(BigDecimal.valueOf(375), invoiceDto.getNetAmount());
        assertEquals(BigDecimal.valueOf(400), invoiceDto.getTotalAmount());
        assertEquals(BigDecimal.valueOf(10), invoiceDto.getPercentageDiscountAmount());
        assertEquals(BigDecimal.valueOf(15), invoiceDto.getFlatDiscountAmount());
        assertEquals("test", invoiceDto.getCustomerName());
    }

    @Test
    public void purchase_ShouldReturn5DollerFlatDiscountForOneYearOverUser_ForEvery100DollerAmount() {
        User user = TestUtils.getInvoiceEntity().getUser();
        user.setUserType(UserType.OTHER);
        user.setRegisteredDate(LocalDateTime.now().minusYears(1));

       List<ItemPurchaseRequest> itemDetails = TestUtils.getListOfItems();
        itemDetails.get(0).setPrice(BigDecimal.valueOf(55));
        itemDetails.get(0).setQuantity(1);
        itemDetails.get(1).setPrice(BigDecimal.valueOf(45));
        itemDetails.get(1).setQuantity(1);
        when(userRepository.findById(1l)).thenReturn(Optional.of(user));
        when(itemRepository.findById(1l)).thenReturn(Optional.of(TestUtils.getSavedItem()));

        InvoiceDto invoiceDto = purchaseService.purchaseItems(itemDetails, 1l);

        assertEquals(BigDecimal.valueOf(95), invoiceDto.getNetAmount());
        assertEquals(BigDecimal.valueOf(100), invoiceDto.getTotalAmount());
        assertEquals(BigDecimal.ZERO, invoiceDto.getPercentageDiscountAmount());
        assertEquals(BigDecimal.valueOf(5), invoiceDto.getFlatDiscountAmount());
        assertEquals("test", invoiceDto.getCustomerName());
    }

    @Test
    public void purchase_ShouldNoApplyAnyDiscount_ForAmountIsBelow100() {
        List<ItemPurchaseRequest> itemDetails = TestUtils.getListOfItems();

        itemDetails.get(0).setPrice(BigDecimal.valueOf(45));
        itemDetails.get(0).setQuantity(1);
        itemDetails.get(1).setPrice(BigDecimal.valueOf(45));
        itemDetails.get(1).setQuantity(1);
        User user = TestUtils.getInvoiceEntity().getUser();
        user.setUserType(UserType.OTHER);
        when(userRepository.findById(1l)).thenReturn(Optional.of(user));
        when(itemRepository.findById(1l)).thenReturn(Optional.of(TestUtils.getSavedItem()));

        InvoiceDto invoiceDto = purchaseService.purchaseItems(itemDetails, 1l);

        assertEquals(BigDecimal.valueOf(90), invoiceDto.getNetAmount());
        assertEquals(BigDecimal.valueOf(90), invoiceDto.getTotalAmount());
        assertEquals(BigDecimal.ZERO, invoiceDto.getFlatDiscountAmount());
        assertEquals(BigDecimal.ZERO, invoiceDto.getPercentageDiscountAmount());
        assertEquals("test", invoiceDto.getCustomerName());
    }

    @Test(expected = NotFoundException.class)
    public void purchase_ShouldThrowNotFoundException_WhenItemNotFound() {
        List<ItemPurchaseRequest> request = TestUtils.getListOfItems();
        when(userRepository.findById(1l)).thenReturn(Optional.of(TestUtils.getInvoiceEntity().getUser()));
        purchaseService.purchaseItems(request, 1l);
    }


    @Test(expected = NotFoundException.class)
    public void purchase_ShouldThrowNotFoundException_WhenUserNotFound() {
        List<ItemPurchaseRequest> request = TestUtils.getListOfItems();
        when(itemRepository.findById(1l)).thenReturn(Optional.of(TestUtils.getSavedItem()));
        purchaseService.purchaseItems(request, 1l);
    }

    @Test(expected = BadRequestException.class)
    public void purchase_ShouldThrowBadRequest_WhenUnitPriceIsZero() {
        List<ItemPurchaseRequest> request = TestUtils.getListOfItems();
        request.get(0).setPrice(BigDecimal.ZERO);
        when(itemRepository.findById(1l)).thenReturn(Optional.of(TestUtils.getSavedItem()));
        when(itemRepository.findById(1l)).thenReturn(Optional.of(TestUtils.getSavedItem()));
        when(userRepository.findById(1l)).thenReturn(Optional.of(TestUtils.getInvoiceEntity().getUser()));


        purchaseService.purchaseItems(request, 1l);
    }

    @Test(expected = BadRequestException.class)
    public void purchase_ShouldThrowBadRequest_WhenQuantityIsZero() {
        List<ItemPurchaseRequest> request = TestUtils.getListOfItems();
        request.get(0).setQuantity(0);

        purchaseService.purchaseItems(request, 1l);
    }

    @Test(expected = BadRequestException.class)
    public void purchase_ShouldThrowBadRequest_WhenRequestItemListIsNull() {
        List<ItemPurchaseRequest> request = new ArrayList<>();
        purchaseService.purchaseItems(request, 1l);
    }

    @Test(expected = BadRequestException.class)
    public void purchase_ShouldThrowBadRequest_WhenRequestUserIdIsNull() {
        List<ItemPurchaseRequest> request = TestUtils.getListOfItems();
        purchaseService.purchaseItems(request, null);
    }
}
