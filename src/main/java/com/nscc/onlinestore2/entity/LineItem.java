package com.nscc.onlinestore2.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class LineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_id", nullable = false)
    private Purchase purchase;

    @Column(nullable = false)
    private Long productId; // Reference to Product entity ID

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private int unitPrice; // Stored in cents to match purchaseTotal
}
