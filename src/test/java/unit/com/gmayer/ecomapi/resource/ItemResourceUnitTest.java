package com.gmayer.ecomapi.resource;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmayer.ecomapi.domains.Item;
import com.gmayer.ecomapi.dtos.ItemDto;
import com.gmayer.ecomapi.resources.ItemResource;
import com.gmayer.ecomapi.services.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemResource.class)
@AutoConfigureMockMvc
public class ItemResourceUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ItemService itemService;

    private ItemDto itemDtoMock;

    @BeforeEach
    void setUp(){
        itemDtoMock = ItemDto.builder()
                .itemName("Basketball")
                .itemDescription("An original NBA Basketball")
                .itemPrice(40.00)
                .build();
    }

    @Test
    void whenGetItems_thenReturnAllItems() throws Exception {
        //Given
        when(itemService.findAllItems()).thenReturn(new ArrayList<>());

        //When //Then
        mockMvc.perform(get("/items/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void givenItemId_whenItemIdValid_thenReturnItem() throws Exception {
        //Given
        String itemId = "3df5e84b-bc55-4afc-a93f-09bbb855b821";
        when(itemService.findById(UUID.fromString(itemId))).thenReturn(itemDtoMock);

        //When //Then
        mockMvc.perform(get("/items/"+itemId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void givenItemId_whenItemIdInvalid_thenThrowException() throws Exception {
        //Given
        String invalidItemId = "123456";

        //When //Then
        String expectedErrorMessage = "Unable to parse UUID, UUID invalid.";
        mockMvc.perform(get("/items/"+invalidItemId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(expectedErrorMessage));
    }

    @Test
    void givenItem_whenItemValid_thenCreateItem() throws Exception {
        //Given
        ObjectMapper objectMapper = new ObjectMapper();
        String payload = objectMapper.writeValueAsString(itemDtoMock);
        when(itemService.createItem(itemDtoMock)).thenReturn(itemDtoMock);

        //When //Then
        mockMvc.perform(post("/items/")
                .content(payload)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }
}
