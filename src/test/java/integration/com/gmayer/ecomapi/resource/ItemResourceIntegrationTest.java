package com.gmayer.ecomapi.resource;

import com.gmayer.ecomapi.domains.Item;
import com.gmayer.ecomapi.dtos.ItemDto;
import com.gmayer.ecomapi.dtos.ItemPurchaseDto;
import com.gmayer.ecomapi.dtos.UserDto;
import com.gmayer.ecomapi.repositories.ItemRepository;
import com.gmayer.ecomapi.repositories.ItemViewRepository;
import com.gmayer.ecomapi.services.ItemService;
import com.gmayer.ecomapi.services.ItemViewService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ItemResourceIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemViewRepository itemViewRepository;

    @LocalServerPort
    private int serverPort;

    @SpyBean
    private ItemService itemServiceSpy;

    @SpyBean
    private ItemViewService itemViewService;

    private static final String BASE_URL = "http://localhost:";

    private Item itemMock;
    private ItemDto itemDtoMock;
    private ItemPurchaseDto itemPurchaseDtoMock;

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

        itemPurchaseDtoMock = ItemPurchaseDto.builder()
                .itemId(UUID.randomUUID())
                .itemQuantity(3)
                .itemPrice(40.00)
                .build();
    }

    @AfterEach
    void tearDown() {
        itemViewRepository.deleteAll();
        itemRepository.deleteAll();
    }

    @Test
    public void whenGetItem_thenReturnAllItems(){
        //Given
        ItemDto item1 = itemDtoMock;
        ItemDto item2 = ItemDto.builder()
                .itemName("Football")
                .itemDescription("An original World Cup Football")
                .itemPrice(100.00)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.set("content-type", MediaType.APPLICATION_JSON.toString());
        HttpEntity<ItemDto> request1 = new HttpEntity<>(item1, headers);
        testRestTemplate.exchange(
                BASE_URL + serverPort + "/items/",
                HttpMethod.POST,
                request1,
                new ParameterizedTypeReference<>() {}
        );
        HttpEntity<ItemDto> request2 = new HttpEntity<>(item2, headers);
        testRestTemplate.exchange(
                BASE_URL + serverPort + "/items/",
                HttpMethod.POST,
                request2,
                new ParameterizedTypeReference<>() {}
        );

        //When
        ResponseEntity<List<ItemDto>> responseEntity = testRestTemplate.exchange(
                BASE_URL + serverPort + "/items/",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        //Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(2, responseEntity.getBody().size());
        verify(itemServiceSpy, times(1)).findAllItems();
    }

    @Test
    public void givenItem_whenCreateItem_thenSaveItem(){
        //Given
        HttpHeaders headers = new HttpHeaders();
        headers.set("content-type", MediaType.APPLICATION_JSON.toString());
        HttpEntity<ItemDto> request = new HttpEntity<>(itemDtoMock, headers);

        //When
        ResponseEntity<ItemDto> responseEntity = testRestTemplate.exchange(
                BASE_URL + serverPort + "/items/",
                HttpMethod.POST,
                request,
                ItemDto.class
        );

        //Then
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody().getItemId());
        assertEquals("Basketball", responseEntity.getBody().getItemName());
        verify(itemServiceSpy, times(1)).createItem(any(ItemDto.class));
    }

    @Test
    public void givenItemExists_whenItemByIdProvided_thenReturnItem(){
        //Given
        Item item = itemRepository.save(itemMock);

        //When
        ResponseEntity<ItemDto> responseEntity = testRestTemplate.exchange(
                BASE_URL + serverPort + "/items/"+item.getId(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        //Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody().getItemId());
        verify(itemServiceSpy, times(1)).findById(item.getId());
        verify(itemViewService, times(1)).createItemView(any());
        verify(itemViewService, times(1)).getLastHourItemViewCountByItemId(item.getId());
        assertEquals(40.00, responseEntity.getBody().getItemPrice());
    }

    @Test
    public void givenItemDoesNotExist_throwException() {
        //Given //When
        UUID invalidItemId = UUID.randomUUID();
        ResponseEntity<?> responseEntity = testRestTemplate.exchange(
                BASE_URL + serverPort + "/items/"+invalidItemId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        //Then
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void givenItemId_whenUserAuthenticated_thenPurchaseItem() {
        //Given
        ResponseEntity<UserDto> loginResponse = testRestTemplate
                .withBasicAuth("test@ecomapi.com", "12345")
                .exchange(
                        BASE_URL + serverPort + "/login",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {}
                );
        String jwtToken = loginResponse.getHeaders().get("Authorization").get(0);
        HttpHeaders headers = new HttpHeaders();
        headers.set("content-type", MediaType.APPLICATION_JSON.toString());
        headers.set("Authorization", jwtToken);
        HttpEntity<ItemPurchaseDto> request = new HttpEntity<>(itemPurchaseDtoMock, headers);

        //When
        ResponseEntity<ItemPurchaseDto> responseEntity = testRestTemplate
                .exchange(
                BASE_URL + serverPort + "/items/buy",
                HttpMethod.POST,
                request,
                ItemPurchaseDto.class
        );

        //Then
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody().getPurchaseId());
    }

    @Test
    public void givenItemId_whenUserNotAuthenticated_thenThrowException() {
        //Given
        ResponseEntity<UserDto> loginResponse = testRestTemplate
                .withBasicAuth("test@ecomapi.com", "12345")
                .exchange(
                        BASE_URL + serverPort + "/login",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {}
                );
        String jwtToken = loginResponse.getHeaders().get("Authorization").get(0);
        HttpHeaders headers = new HttpHeaders();
        headers.set("content-type", MediaType.APPLICATION_JSON.toString());
        headers.set("Authorization", jwtToken+"invalid");
        HttpEntity<ItemPurchaseDto> request = new HttpEntity<>(itemPurchaseDtoMock, headers);

        //When
        ResponseEntity<ItemPurchaseDto> responseEntity = testRestTemplate
                .exchange(
                        BASE_URL + serverPort + "/items/buy",
                        HttpMethod.POST,
                        request,
                        ItemPurchaseDto.class
                );

        //Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }
}
