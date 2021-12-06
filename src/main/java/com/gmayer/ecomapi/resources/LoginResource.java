package com.gmayer.ecomapi.resources;

import com.gmayer.ecomapi.domains.User;
import com.gmayer.ecomapi.dtos.UserDto;
import com.gmayer.ecomapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("login")
public class LoginResource {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<?> getUserLoginDetails(Principal principal) {
        User user = userRepository.findByEmail(principal.getName());
        UserDto userDto = UserDto.builder().username(user.getEmail()).build();
        return ResponseEntity.status(HttpStatus.OK).body(userDto);
    }
}
