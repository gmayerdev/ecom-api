package com.gmayer.ecomapi.resources;

import com.gmayer.ecomapi.dtos.ItemDto;
import com.gmayer.ecomapi.dtos.ItemPurchaseDto;
import com.gmayer.ecomapi.services.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("items")
@Slf4j
public class ItemResource {

    @Autowired
    private ItemService itemService;

    @GetMapping("")
    public ResponseEntity<List<ItemDto>> getAllItems(){
        List<ItemDto> items = itemService.findAllItems();
        return ResponseEntity.status(HttpStatus.OK).body(items);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> getById(@PathVariable String id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Find items by id performed by {}", authentication.getPrincipal());
        UUID uuid;
        try{
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException ex){
            throw new IllegalArgumentException("Unable to parse UUID, UUID invalid.");
        }
        ItemDto item = itemService.findById(uuid);
        return ResponseEntity.status(HttpStatus.OK).body(item);
    }

    @PostMapping()
    public ResponseEntity<ItemDto> createItem(@RequestBody ItemDto item){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Create item performed by {}", authentication.getPrincipal());
        ItemDto createdItems = itemService.createItem(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdItems);
    }

    @PostMapping("/buy")
    public ResponseEntity<ItemPurchaseDto> buyItem(@RequestBody ItemPurchaseDto itemPurchase){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Buying item performed by {}", authentication.getPrincipal());
        itemPurchase.setPurchaseId(UUID.randomUUID().toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(itemPurchase);
    }
}
