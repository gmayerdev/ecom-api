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
public class ItemDto {

    private UUID itemId;
    private String itemName;
    private String itemDescription;
    private Double itemPrice;
}
