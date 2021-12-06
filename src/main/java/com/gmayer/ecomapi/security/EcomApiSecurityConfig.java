package com.gmayer.ecomapi.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

@Configuration
public class EcomApiSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String HEADER = "Authorization";
    private static final String PERMIT_ALL = "*";

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
            .cors().configurationSource(new CorsConfigurationSource() {
                @Override
                public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(Collections.singletonList(PERMIT_ALL));
                    config.setAllowedMethods(Collections.singletonList(PERMIT_ALL));
                    config.setAllowCredentials(true);
                    config.setAllowedHeaders(Collections.singletonList(PERMIT_ALL));
                    config.setExposedHeaders(Collections.singletonList(HEADER));
                    config.setMaxAge(3600L);
                    return config;
                }
            })
        .and()
            .csrf().disable()
            .addFilterBefore(new JwtValidatorFilter(), BasicAuthenticationFilter.class)
            .addFilterAfter(new JwtGeneratorFilter(), BasicAuthenticationFilter.class)
            .authorizeRequests()
            .antMatchers("/items").permitAll()
            .antMatchers("/items/buy").authenticated()
            .antMatchers("/login").authenticated()
        .and()
            .httpBasic();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
