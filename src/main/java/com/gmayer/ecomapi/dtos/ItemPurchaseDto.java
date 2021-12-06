package com.gmayer.ecomapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ItemPurchaseDto {

    private UUID itemId;
    private Integer itemQuantity;
    private Double itemPrice;
    private String purchaseId;
}
