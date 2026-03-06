package com.nscc.onlinestore2.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String stripeSessionId;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private int purchaseTotal; // Int in case Stripe uses cents

    @Column(nullable = false)
    private String purchaseDateTime;

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LineItem> lineItems = new ArrayList<>();
}
