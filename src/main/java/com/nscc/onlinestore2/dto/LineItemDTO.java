package com.nscc.onlinestore2.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class LineItemDTO {

    @NotNull
    private Long purchaseId;

    @NotNull
    private Long productId;

    @Positive
    private int quantity;

    @DecimalMin("0.00")
    private int unitPrice; // Still in cents
}
