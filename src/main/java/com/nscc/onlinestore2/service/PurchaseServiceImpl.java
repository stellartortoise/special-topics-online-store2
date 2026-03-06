package com.nscc.onlinestore2.service;

import com.nscc.onlinestore2.dto.PurchaseDTO;
import com.nscc.onlinestore2.entity.LineItem;
import com.nscc.onlinestore2.entity.Purchase;
import com.nscc.onlinestore2.repository.PurchaseRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class PurchaseServiceImpl implements PurchaseService{

    private final PurchaseRepository purchaseRepository;

    public PurchaseServiceImpl(PurchaseRepository purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
    }

    @Override
    public List<Purchase> getAllPurchases() {
        return purchaseRepository.findAll();
    }

    @Override
    public Optional<Purchase> getPurchaseById(Long id) {
        return purchaseRepository.findById(id);
    }

    @Override
    public Purchase createPurchase(Purchase purchase) {

        if (purchase.getLineItems() != null) {
            purchase.getLineItems().forEach(item -> item.setPurchase(purchase));
        }
        return purchaseRepository.save(purchase);
    }

    @Override
    public LineItem createLineItem(LineItem lineItem) {
        throw new UnsupportedOperationException("Line items should be created via createPurchase");
    }

    @Override
    public Purchase updatePurchase(long id, @Valid PurchaseDTO purchaseDTO) {
        Purchase existingPurchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Purchase not found"));

        existingPurchase.setStripeSessionId(purchaseDTO.getStripeSessionId());
        existingPurchase.setStatus(purchaseDTO.getStatus());
        existingPurchase.setPurchaseTotal(purchaseDTO.getPurchaseTotal());

        return purchaseRepository.save(existingPurchase);
    }

    @Override
    public Purchase updatePurchaseStatus(Long id, String status) {
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Purchase not found"));

        purchase.setStatus(status);
        return purchaseRepository.save(purchase);
    }

    @Override
    public Purchase updateStatusBySessionId(String sessionId, String status) {
        // 1. Find the purchase using the custom repository method
        Purchase purchase = purchaseRepository.findByStripeSessionId(sessionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Purchase with Stripe Session " + sessionId + " not found"));

        // 2. Update the status (e.g., from 'pending' to 'paid')
        purchase.setStatus(status);

        // 3. Save and return
        return purchaseRepository.save(purchase);
    }
}
