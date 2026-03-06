package com.nscc.onlinestore2.dto;

import lombok.Data;

import java.util.List;

@Data
public class PurchaseDTO {
    //    @NotNull
//    @NotBlank(message = "Stripe Session Id is Required")
    private String stripeSessionId;

    //    @NotNull
//    @NotBlank(message = "Status is Required")
    private String status;

    private int purchaseTotal;

    private String purchaseDateTime;

    private List<LineItemDTO> lineItems;
}
