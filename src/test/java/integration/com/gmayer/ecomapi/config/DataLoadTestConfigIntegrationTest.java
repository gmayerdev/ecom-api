package com.gmayer.ecomapi.config;

import com.gmayer.ecomapi.domains.User;
import com.gmayer.ecomapi.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DataLoadTestConfigIntegrationTest {

    @Autowired
    private DataLoadTestConfig dataLoadTestConfig;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    public void whenApplicationRuns_LoadTestDataDevelopment(){
        //Given
        dataLoadTestConfig.loadTestData();

        //When
        dataLoadTestConfig.loadTestData();

        //Then
        String email = "test@ecomapi.com";
        User testUser = userRepository.findByEmail(email);
        assertEquals("test@ecomapi.com", testUser.getEmail());
    }
}
