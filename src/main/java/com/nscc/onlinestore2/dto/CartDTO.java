package com.nscc.onlinestore2.dto;

import lombok.Data;

import java.util.List;

@Data
public class CartDTO {
    List<CartItemDTO> items;
}

