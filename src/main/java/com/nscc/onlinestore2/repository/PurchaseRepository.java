package com.nscc.onlinestore2.repository;

import com.nscc.onlinestore2.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    Optional<Purchase> findByStripeSessionId(String stripeSessionId);
}
