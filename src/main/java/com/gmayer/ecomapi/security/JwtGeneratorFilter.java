package com.gmayer.ecomapi.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Objects;

public class JwtGeneratorFilter extends OncePerRequestFilter {

	private static final String HEADER = "Authorization";

	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (Objects.isNull(authentication)) {
			chain.doFilter(request, response);
			return;
		}

		String JWT_SECRET = "3cded68a9e0f9b83f2c5de1b79fc4dac45004523e6658d46145156fa6a03eced";
		SecretKey key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
		String jwtToken = Jwts.builder()
				.setIssuer("ECOM-API")
				.setSubject("JWT Token")
				.claim("username", authentication.getName())
				.setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + 900000))
				.signWith(key).compact();
		response.setHeader(HEADER, jwtToken);
		chain.doFilter(request, response);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		return !request.getServletPath().equals("/login");
	}
}
