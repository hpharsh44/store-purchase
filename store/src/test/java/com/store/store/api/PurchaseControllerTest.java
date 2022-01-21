package com.store.store.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.store.common.TestUtils;
import com.store.store.common.dto.InvoiceDto;
import com.store.store.common.request.ItemPurchaseRequest;
import com.store.store.service.PurchaseService;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@RunWith(SpringRunner.class)
@Import(PurchaseController.class)
public class PurchaseControllerTest {

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private PurchaseService purchaseService;

    @Test
    public void purchase_shouldHitCorrectEndpoint_withStatusOk() throws Exception {
        List<ItemPurchaseRequest> itemDetails = TestUtils.getListOfItems();
        InvoiceDto invoiceDto = InvoiceDto.build(TestUtils.getInvoiceEntity());
        Mockito.when(purchaseService.purchaseItems(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(invoiceDto);
        String json = mapper.writeValueAsString(itemDetails);
        mvc.perform(post("/api/store/purchase-items/user/1").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
                .content(json).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName", Matchers.equalTo("test")))
                .andExpect(jsonPath("$.totalAmount", Matchers.equalTo(100)))
                .andExpect(jsonPath("$.percentageDiscountAmount", Matchers.equalTo(5)))
                .andExpect(jsonPath("$.netAmount", Matchers.equalTo(95)));
    }

}
