package com.nscc.onlinestore2.controllers;

import com.nscc.onlinestore2.dto.LineItemDTO;
import com.nscc.onlinestore2.entity.LineItem;
import com.nscc.onlinestore2.service.LineItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/lineitem")
public class LineItemController {

    private final LineItemService lineItemService;

    public LineItemController(LineItemService lineItemService) {
        this.lineItemService = lineItemService;
    }

    // --- READ (All) ---
    @GetMapping("/")
    public List<LineItem> getAllLineItems() {
        return lineItemService.getAllLineItems();
    }

    // --- READ (One) ---
    @GetMapping("/{id}")
    public LineItem getLineItemById(@PathVariable long id) {
        return lineItemService.getLineItemById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "LineItem not found"));
    }

    // --- CREATE ---
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public LineItem createLineItem(@Valid @RequestBody LineItemDTO lineItemDTO) {
        LineItem lineItem = new LineItem();
        lineItem.setProductId(lineItemDTO.getProductId());
        lineItem.setQuantity(lineItemDTO.getQuantity());
        lineItem.setUnitPrice(lineItemDTO.getUnitPrice());

        return lineItemService.createLineItem(lineItemDTO.getPurchaseId(), lineItem);
    }
}
