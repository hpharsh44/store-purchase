package com.store.store.api;

import com.store.store.common.dto.InvoiceDto;
import com.store.store.common.request.ItemPurchaseRequest;
import com.store.store.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/store/purchase-items")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    @PostMapping("/user/{user_id}")
    public ResponseEntity<InvoiceDto> purchaseItems(@RequestBody List<ItemPurchaseRequest> request, @PathVariable("user_id") Long userId) {
        return ResponseEntity.ok(purchaseService.purchaseItems(request,userId));
    }
}
