package com.gmayer.ecomapi.config;

import com.gmayer.ecomapi.domains.User;
import com.gmayer.ecomapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

@Configuration
@Profile("dev")
public class DataLoadTestConfig {

    @Autowired
    private UserRepository userRepository;

    @Bean
    public void loadTestData(){
        User testUser = userRepository.findByEmail("test@ecomapi.com");
        if(testUser == null){
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String encryptedPassword = encoder.encode("12345");
            User newTestUser = User.builder()
                    .id(UUID.randomUUID())
                    .email("test@ecomapi.com")
                    .password(encryptedPassword)
                    .build();
            userRepository.save(newTestUser);
        }
    }
}