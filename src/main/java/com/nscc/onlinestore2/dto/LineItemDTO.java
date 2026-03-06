package com.nscc.onlinestore2.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class LineItemDTO {

    private Long purchaseId;

    @NotNull
    private Long productId;

    @Positive
    private int quantity;

    @Min(0)
    private int unitPrice; // Still in cents
}
