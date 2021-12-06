package com.gmayer.ecomapi.resource;

import com.gmayer.ecomapi.dtos.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginResourceIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @LocalServerPort
    private int serverPort;

    private static final String BASE_URL = "http://localhost:";

    @Test
    public void givenUser_whenUsernamePasswordProvided_thenSuccessfullyPerformLogin(){
        //Given //When
        ResponseEntity<UserDto> responseEntity = testRestTemplate
                .withBasicAuth("test@ecomapi.com", "12345")
                .exchange(
                BASE_URL + serverPort + "/login",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        //Then
        assertEquals("test@ecomapi.com", responseEntity.getBody().getUsername());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getHeaders().get("Authorization"));
    }

    @Test
    public void givenUser_whenUsernameInvalid_thenThrowException(){
        //Given //When
        ResponseEntity<UserDto> responseEntity = testRestTemplate
                .withBasicAuth("bob@ecomapi.com", "12345")
                .exchange(
                        BASE_URL + serverPort + "/login",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {}
                );

        //Then
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    public void givenUser_whenPasswordInvalid_thenThrowException(){
        //Given //When
        ResponseEntity<UserDto> responseEntity = testRestTemplate
                .withBasicAuth("test@ecomapi.com", "abcdef")
                .exchange(
                        BASE_URL + serverPort + "/login",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {}
                );

        //Then
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }
}
