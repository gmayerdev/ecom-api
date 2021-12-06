package com.gmayer.ecomapi.config;


import com.gmayer.ecomapi.domains.Item;
import com.gmayer.ecomapi.dtos.ItemDto;
import com.gmayer.ecomapi.repositories.ItemRepository;
import com.gmayer.ecomapi.services.ItemService;
import com.gmayer.ecomapi.services.ItemViewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ItemServiceUnitTest {

    @Autowired
    private ItemService itemService;

    @MockBean
    private ItemRepository itemRepository;

    @MockBean
    private ItemViewService itemViewService;

    private Item itemMock;
    private ItemDto itemDtoMock;

    @BeforeEach
    void setUp(){
        itemMock = Item.builder()
                .name("Basketball")
                .description("An original NBA Basketball")
                .price(40.00)
                .build();

        itemDtoMock = ItemDto.builder()
                .itemName("Basketball")
                .itemDescription("An original NBA Basketball")
                .itemPrice(40.00)
                .build();
    }

    @Test
    void whenFindAllItems_thenReturnAllItems(){
        //Given //When
        when(itemRepository.findAll()).thenReturn(Collections.singletonList(itemMock));
        List<ItemDto> result = itemService.findAllItems();

        //Then
        assertEquals("Basketball", result.get(0).getItemName());
    }

    @Test
    void givenItemId_whenViewedBelowThreshold_thenPriceDoesNotIncrease(){
        //Given
        Optional<Item> itemMockOptional = Optional.of(itemMock);
        UUID itemId = itemMockOptional.get().getId();

        //When
        when(itemRepository.findById(itemId)).thenReturn(itemMockOptional);
        when(itemViewService.createItemView(itemMockOptional.get())).thenReturn(any());
        when(itemViewService.getLastHourItemViewCountByItemId(itemId)).thenReturn(1);
        ItemDto result = itemService.findById(itemId);

        //Then
        assertEquals(40.00, result.getItemPrice());
    }

    @Test
    void givenItemId_whenViewedAboveThreshold_thenPriceIncreases(){
        //Given
        Optional<Item> itemMockOptional = Optional.of(itemMock);
        UUID itemId = itemMockOptional.get().getId();

        //When
        when(itemRepository.findById(itemId)).thenReturn(itemMockOptional);
        when(itemViewService.createItemView(itemMockOptional.get())).thenReturn(any());
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
        //Given //When
        itemMock.setId(UUID.randomUUID());
        when(itemRepository.save(any())).thenReturn(itemMock);
        ItemDto result = itemService.createItem(itemDtoMock);

        //Then
        assertNotNull(result.getItemId());
    }
}
