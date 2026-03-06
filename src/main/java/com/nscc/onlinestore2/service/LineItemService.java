package com.nscc.onlinestore2.service;

import com.nscc.onlinestore2.entity.LineItem;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;

public interface LineItemService {

    List<LineItem> getAllLineItems();

    Optional<LineItem> getLineItemById(Long Id);

    LineItem createLineItem(@NotNull Long purchaseId, LineItem lineItem);

}
