package com.gmayer.ecomapi.resources;

import com.gmayer.ecomapi.dtos.ItemDto;
import com.gmayer.ecomapi.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("items")
public class ItemResource {

    @Autowired
    private ItemService itemService;

    @GetMapping("")
    public ResponseEntity<List<ItemDto>> getAllItems(){
        List<ItemDto> items = itemService.findAllItems();
        return ResponseEntity.status(HttpStatus.OK).body(items);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id){
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
        ItemDto createdItems = itemService.createItem(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdItems);
    }

//    @PostMapping("/buy")
//    public ResponseEntity<?> buyItem(@RequestBody ItemPurchaseDto item){
//        itemService.buyItem(item);
//        return ResponseEntity.status(HttpStatus.CREATED).body("Purchase Successful!");
//    }
}
