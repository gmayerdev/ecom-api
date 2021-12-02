package com.gmayer.ecomapi.service;


import com.gmayer.ecomapi.domains.ItemView;
import com.gmayer.ecomapi.repositories.ItemViewRepository;
import com.gmayer.ecomapi.services.ItemViewService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ItemViewServiceUnitTest {

    @Autowired
    private ItemViewService itemViewService;

    @MockBean
    private ItemViewRepository itemViewRepository;

    @Test
    void givenItemId_whenCreateItemView_thenReturnCreatedItemView(){
        //Given
        UUID itemId = UUID.randomUUID();

        //When
        ItemView result = itemViewService.createItemView(itemId);

        //Then
        assertEquals(itemId, result.getItemId());
        assertNotNull(result.getViewedOn());
    }

    @Test
    void givenItemId_whenLastHourItemViewCount_thenReturnItemViewCreatedLastHour(){
        //Given
        UUID itemId = UUID.randomUUID();
        ItemView itemViewMock1 = ItemView.builder()
                .id(UUID.randomUUID())
                .itemId(itemId)
                .viewedOn(LocalDateTime.now().minusMinutes(1))
                .build();
        ItemView itemViewMock2 = ItemView.builder()
                .id(UUID.randomUUID())
                .itemId(itemId)
                .viewedOn(LocalDateTime.now().minusMinutes(61))
                .build();

        //When
        when(itemViewRepository.findAllByItemId(itemId)).thenReturn(Arrays.asList(itemViewMock1, itemViewMock2));
        Integer result = itemViewService.getLastHourItemViewCountByItemId(itemId);

        //Then
        assertEquals(1, result);
    }
}
