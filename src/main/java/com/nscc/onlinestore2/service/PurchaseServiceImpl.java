package com.nscc.onlinestore2.service;

import com.nscc.onlinestore2.dto.LineItemDTO;
import com.nscc.onlinestore2.dto.PurchaseDTO;
import com.nscc.onlinestore2.entity.LineItem;
import com.nscc.onlinestore2.entity.Purchase;
import com.nscc.onlinestore2.repository.LineItemRepository;
import com.nscc.onlinestore2.repository.PurchaseRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
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

//    @Override
//    public Purchase createPurchase(Purchase purchase) {
//
//        if (purchase.getLineItems() != null) {
//            purchase.getLineItems().forEach(item -> item.setPurchase(purchase));
//        }
//        return purchaseRepository.save(purchase);
//    }

    @Override
    public Purchase createPurchase(@Valid PurchaseDTO dto) {
        Purchase p = mapFromDto(dto);
        return purchaseRepository.save(p); // see entity options below
    }

    // Keep your existing createPurchase(Purchase) if needed:
    @Override
    public Purchase createPurchase(Purchase purchase) {
        // set defaults + backrefs + compute total if items present
        if (isBlank(purchase.getStatus())) purchase.setStatus("pending");
        if (isBlank(purchase.getPurchaseDateTime())) purchase.setPurchaseDateTime(nowIso());

        if (purchase.getLineItems() != null && !purchase.getLineItems().isEmpty()) {
            int computed = 0;
            for (LineItem li : purchase.getLineItems()) {
                li.setPurchase(purchase);
                computed += Math.max(0, li.getUnitPrice()) * Math.max(0, li.getQuantity());
            }
            if (purchase.getPurchaseTotal() <= 0) purchase.setPurchaseTotal(computed);
        }
        return purchaseRepository.save(purchase);
    }

    // ---- mapping helper ----
    private Purchase mapFromDto(PurchaseDTO dto) {
        Purchase p = new Purchase();
        p.setStripeSessionId(dto.getStripeSessionId());
        p.setStatus(isBlank(dto.getStatus()) ? "pending" : dto.getStatus());
        p.setPurchaseDateTime(isBlank(dto.getPurchaseDateTime()) ? nowIso() : dto.getPurchaseDateTime());

        List<LineItem> items = new ArrayList<>();
        int computed = 0;

        if (dto.getLineItems() != null) {
            for (LineItemDTO li : dto.getLineItems()) {
                LineItem e = new LineItem();
                // INTENTIONAL: ignore li.getPurchaseId() here — we attach to 'p'
                e.setProductId(li.getProductId());
                e.setQuantity(Math.max(0, li.getQuantity()));
                e.setUnitPrice(Math.max(0, li.getUnitPrice()));
                e.setPurchase(p);        // <-- back-reference to the *new* Purchase
                items.add(e);
                computed += e.getUnitPrice() * e.getQuantity();
            }
        }

        p.setLineItems(items);
        p.setPurchaseTotal(dto.getPurchaseTotal() > 0 ? dto.getPurchaseTotal() : computed);
        return p;
    }

    private static String nowIso() {
        return java.time.OffsetDateTime.now()
                .format(java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
    private static boolean isBlank(String s) { return s == null || s.isBlank(); }

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
