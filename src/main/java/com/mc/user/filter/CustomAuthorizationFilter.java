package com.mc.user.filter;

import static java.util.Arrays.stream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.HttpHeaders;

public class CustomAuthorizationFilter extends OncePerRequestFilter {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomAuthorizationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        if(request.getServletContext().equals("/login") || request.getServletContext().equals("/h2-console")) {
            filterChain.doFilter(request, response);
        } else {
            String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            
            if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                
                try {
                    String token = authorizationHeader.substring("Bearer ".length());
                    Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                    JWTVerifier verifier = JWT.require(algorithm).build();
                    DecodedJWT decoder = verifier.verify(token);
                    
                    String username = decoder.getSubject();
                    String[] roles = decoder.getClaim("roles").asArray(String.class);
                    
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    
                    stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
                    
                    UsernamePasswordAuthenticationToken  authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request, response);
                } catch (Exception exception) {
                    LOGGER.error("Error loggin in: {} ", exception.getMessage());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.setHeader("error", exception.getMessage());
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    Map<String, String> errors = new HashMap<>();
                    errors.put("error_message", exception.getMessage());
                    new ObjectMapper().writeValue(response.getOutputStream(), errors);                    
                }
            } else {
                filterChain.doFilter(request, response);
            }
        }
    }

}
