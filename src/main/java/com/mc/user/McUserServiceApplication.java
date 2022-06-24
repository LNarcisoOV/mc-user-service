package com.mc.user;

import java.util.ArrayList;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.mc.user.domain.Role;
import com.mc.user.domain.User;
import com.mc.user.service.UserService;

@EnableEurekaClient
@EnableDiscoveryClient
@SpringBootApplication
public class McUserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(McUserServiceApplication.class, args);
	}
	
	@Bean
	CommandLineRunner run(UserService userService) {
	    return args -> {
	        userService.save(new Role(null, "USER"));
	        userService.save(new User(null, "Leonardo Narciso", "LNarcisoOV", "1234", new ArrayList<>()));
	    };
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
