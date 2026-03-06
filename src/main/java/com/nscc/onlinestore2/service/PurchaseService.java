package com.nscc.onlinestore2.service;

import com.nscc.onlinestore2.dto.PurchaseDTO;
import com.nscc.onlinestore2.entity.LineItem;
import com.nscc.onlinestore2.entity.Purchase;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

public interface PurchaseService {

    List<Purchase> getAllPurchases();

    Optional<Purchase> getPurchaseById(Long Id);

    Purchase createPurchase(Purchase purchase);

    LineItem createLineItem(LineItem lineItem);

    Purchase updatePurchase(long id, @Valid PurchaseDTO purchaseDTO);

    Purchase updatePurchaseStatus(Long id, String status);

    Purchase updateStatusBySessionId(String sessionId, String status);

}
