package com.gmayer.ecomapi.service;


import com.gmayer.ecomapi.domains.Item;
import com.gmayer.ecomapi.domains.ItemView;
import com.gmayer.ecomapi.repositories.ItemViewRepository;
import com.gmayer.ecomapi.services.ItemViewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ItemViewServiceUnitTest {

    @Autowired
    private ItemViewService itemViewService;

    @MockBean
    private ItemViewRepository itemViewRepository;

    private Item itemMock;

    @BeforeEach
    void setUp(){
        itemMock = Item.builder()
                .id(UUID.randomUUID())
                .name("Basketball")
                .description("An original NBA Basketball")
                .price(40.00)
                .build();
    }

    @Test
    void givenItemId_whenCreateItemView_thenReturnCreatedItemView(){
        //Given //When
        ItemView result = itemViewService.createItemView(itemMock);

        //Then
        assertEquals(itemMock.getId(), result.getItem().getId());
        assertNotNull(result.getViewedOn());
    }

    @Test
    void givenItemId_whenLastHourItemViewCount_thenReturnItemViewCreatedLastHour(){
        //Given
        ItemView itemViewMock1 = ItemView.builder()
                .id(UUID.randomUUID())
                .item(itemMock)
                .viewedOn(LocalDateTime.now().minusMinutes(1))
                .build();
        ItemView itemViewMock2 = ItemView.builder()
                .id(UUID.randomUUID())
                .item(itemMock)
                .viewedOn(LocalDateTime.now().minusMinutes(61))
                .build();

        //When
        when(itemViewRepository.findAllByItemId(itemMock.getId())).thenReturn(Arrays.asList(itemViewMock1, itemViewMock2));
        Integer result = itemViewService.getLastHourItemViewCountByItemId(itemMock.getId());

        //Then
        assertEquals(1, result);
    }
}
