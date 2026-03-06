package com.nscc.onlinestore2.controllers;

import com.nscc.onlinestore2.dto.PurchaseDTO;
import com.nscc.onlinestore2.entity.Purchase;
import com.nscc.onlinestore2.service.PurchaseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/purchases")
public class PurchaseController {

    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    // --- READ (All) ---
    @GetMapping("/")
    public List<Purchase> getAllPurchases() {
        return purchaseService.getAllPurchases();
    }

    // --- READ (One) ---
    @GetMapping("/{id}")
    public Purchase getPurchaseById(@PathVariable long id) {
        return purchaseService.getPurchaseById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Purchase not found"));
    }

    // --- CREATE ---

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public Purchase createPurchase(@Valid @RequestBody PurchaseDTO purchaseDTO) {
        return purchaseService.createPurchase(purchaseDTO);
    }

//    @PostMapping("/")
//    @ResponseStatus(HttpStatus.CREATED)
//    public Purchase createPurchase(@Valid @RequestBody PurchaseDTO purchaseDTO) {
//        Purchase purchase = new Purchase();
//        purchase.setStripeSessionId(purchaseDTO.getStripeSessionId());
//        purchase.setStatus("pending"); // Default to pending on creation
//        purchase.setPurchaseTotal(purchaseDTO.getPurchaseTotal());
//        purchase.setPurchaseDateTime(purchaseDTO.getPurchaseDateTime());
//
//        return purchaseService.createPurchase(purchase);
//    }

    // --- UPDATE STATUS ---
    @PatchMapping("/{id}/status")
    public Purchase updateStatus(@PathVariable long id, @RequestBody String newStatus) {
        return purchaseService.updatePurchaseStatus(id, newStatus);
    }

    // --- UPDATE (Full) ---
    @PutMapping("/{id}")
    public Purchase updatePurchase(@PathVariable long id, @Valid @RequestBody PurchaseDTO purchaseDTO) {
        return purchaseService.updatePurchase(id, purchaseDTO);
    }

    @PatchMapping("/stripe/{sessionId}/status")
    public Purchase updateStatusByStripeId(@PathVariable String sessionId, @RequestBody String status) {
        // You'd need to add this method to your Service interface!
        return purchaseService.updateStatusBySessionId(sessionId, status);
    }
}