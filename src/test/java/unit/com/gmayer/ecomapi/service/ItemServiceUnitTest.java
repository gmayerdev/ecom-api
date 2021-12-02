package com.gmayer.ecomapi.service;


import com.gmayer.ecomapi.domains.Item;
import com.gmayer.ecomapi.dtos.ItemDto;
import com.gmayer.ecomapi.repositories.ItemRepository;
import com.gmayer.ecomapi.services.ItemService;
import com.gmayer.ecomapi.services.ItemViewService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ItemServiceUnitTest {

    @Autowired
    private ItemService itemService;

    @MockBean
    private ItemRepository itemRepository;

    @MockBean
    private ItemViewService itemViewService;

    @Test
    void whenFindAllItems_thenReturnAllItems(){
        //Given
        Item itemMock = Item.builder()
                .id(UUID.randomUUID())
                .name("Basketball")
                .description("An original NBA Basketball")
                .price(40.00)
                .build();

        //When
        when(itemRepository.findAll()).thenReturn(Collections.singletonList(itemMock));
        List<ItemDto> result = itemService.findAllItems();

        //Then
        assertEquals("Basketball", result.get(0).getItemName());
    }

    @Test
    void givenItemId_whenViewedBelowThreshold_thenPriceDoesNotIncrease(){
        //Given
        UUID itemId = UUID.randomUUID();
        Optional<Item> itemMock = Optional.of(Item.builder()
                .id(itemId)
                .name("Basketball")
                .description("An original NBA Basketball")
                .price(40.00)
                .build());

        //When
        when(itemRepository.findById(itemId)).thenReturn(itemMock);
        when(itemViewService.createItemView(itemId)).thenReturn(any());
        when(itemViewService.getLastHourItemViewCountByItemId(itemId)).thenReturn(1);
        ItemDto result = itemService.findById(itemId);

        //Then
        assertEquals(40.00, result.getItemPrice());
    }

    @Test
    void givenItemId_whenViewedAboveThreshold_thenPriceIncreases(){
        //Given
        UUID itemId = UUID.randomUUID();
        Optional<Item> itemMock = Optional.of(Item.builder()
                .id(itemId)
                .name("Basketball")
                .description("An original NBA Basketball")
                .price(40.00)
                .build());

        //When
        when(itemRepository.findById(itemId)).thenReturn(itemMock);
        when(itemViewService.createItemView(itemId)).thenReturn(any());
        when(itemViewService.getLastHourItemViewCountByItemId(itemId)).thenReturn(10);
        ItemDto result = itemService.findById(itemId);

        //Then
        assertEquals(44.00, result.getItemPrice());
    }

    @Test
    void givenItemId_whenItemDoesNotExist_thenThrowException(){
        //Given
        UUID itemId = UUID.randomUUID();

        //When //Then
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> {
            itemService.findById(itemId);
        }, "Item does not exist");
    }

    @Test
    void givenItem_whenCreateItem_thenReturnCreatedItem(){
        //Given
        ItemDto newItemDtoMock = ItemDto.builder()
                .itemId(UUID.randomUUID())
                .itemName("Basketball")
                .itemDescription("An original NBA Basketball")
                .itemPrice(40.00)
                .build();

        Item newItemMock = Item.builder()
                .id(UUID.randomUUID())
                .name("Basketball")
                .description("An original NBA Basketball")
                .price(40.00)
                .build();

        //When
        when(itemRepository.save(any())).thenReturn(newItemMock);
        ItemDto result = itemService.createItem(newItemDtoMock);

        //Then
        assertNotNull(result.getItemId());
    }
}
