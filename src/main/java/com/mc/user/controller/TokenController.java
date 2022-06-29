package com.mc.user.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.HttpHeaders;
import com.mc.user.domain.Role;
import com.mc.user.domain.User;
import com.mc.user.service.UserService;

@RestController
@RequestMapping("/token")
public class TokenController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenController.class);
    private static final int TOKEN_CREATE_EXPIRATION_TIME = 10 * 60 * 1000;
    
    @Autowired
    private UserService userService;

    @GetMapping("/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decoder = verifier.verify(refresh_token);
                
                String username = decoder.getSubject();
                User user = userService.get(username);
                
                String access_token = JWT.create().withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_CREATE_EXPIRATION_TIME))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                        .sign(algorithm);
                
                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);
                
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
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
            throw new RuntimeException("Refresh token is missing.");
        }
    }

}
