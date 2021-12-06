package com.gmayer.ecomapi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
import java.util.ArrayList;

public class JwtValidatorFilter extends OncePerRequestFilter {

	private static final String HEADER = "Authorization";

	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String jwt = request.getHeader(HEADER);

		if (jwt != null && !jwt.isEmpty()) {
			try {
				String JWT_SECRET = "3cded68a9e0f9b83f2c5de1b79fc4dac45004523e6658d46145156fa6a03eced";
				SecretKey key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
				Claims claims = Jwts.parserBuilder()
						.setSigningKey(key)
						.build()
						.parseClaimsJws(jwt)
						.getBody();
				String username = String.valueOf(claims.get("username"));
				Authentication authentication = new UsernamePasswordAuthenticationToken(username,null, new ArrayList<>());
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}catch (Exception e) {
				throw new BadCredentialsException("Token invalid");
			}
		}
		chain.doFilter(request, response);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		return request.getServletPath().equals("/login");
	}
}
