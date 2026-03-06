package com.nscc.onlinestore2.service;

import com.nscc.onlinestore2.entity.LineItem;
import com.nscc.onlinestore2.entity.Purchase;
import com.nscc.onlinestore2.repository.LineItemRepository;
import com.nscc.onlinestore2.repository.PurchaseRepository;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;

public class LineItemServiceImpl implements LineItemService {

    private final PurchaseRepository purchaseRepository;
    private final LineItemRepository lineItemRepository;

    public LineItemServiceImpl(PurchaseRepository purchaseRepository, LineItemRepository lineItemRepository) {
        this.purchaseRepository = purchaseRepository;
        this.lineItemRepository = lineItemRepository;
    }

    @Override
    public List<LineItem> getAllLineItems() {
        return lineItemRepository.findAll();
    }

    @Override
    public Optional<LineItem> getLineItemById(Long id) {
        return lineItemRepository.findById(id);
    }

//    @Override
//    public LineItem createLineItem(LineItem lineItem) {
//        return lineItemRepository.save(lineItem);
//    }

    @Override
    public LineItem createLineItem(@NotNull Long purchaseId, LineItem lineItem) {

        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new IllegalArgumentException("Purchase not found: " + purchaseId));


        // Keep both sides in sync
        lineItem.setPurchase(purchase);
        return lineItemRepository.save(lineItem);

    }
}
