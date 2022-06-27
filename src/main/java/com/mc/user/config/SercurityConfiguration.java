package com.mc.user.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.mc.user.filter.CustomAuthenticationFilter;
import com.mc.user.filter.CustomAuthorizationFilter;

@Configuration
@EnableWebSecurity
public class SercurityConfiguration {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
        .httpBasic()
        .and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests().antMatchers("/h2-console/**").permitAll()
        .and()
        .authorizeRequests().antMatchers(HttpMethod.GET, "/user/**").hasAuthority("ROLE_USER")
        .and()
        .authorizeRequests().antMatchers(HttpMethod.POST, "/user/**").hasAuthority("ROLE_USER")
        .and()
        .authorizeRequests().anyRequest().authenticated()
        .and()
        .addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
        .addFilter(new CustomAuthenticationFilter(authenticationManager))
        .headers().frameOptions().disable()
        .and()
        .csrf().disable();
        
        return http.build();
    }

}
