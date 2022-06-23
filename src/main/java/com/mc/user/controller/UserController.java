package com.mc.user.controller;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.mc.user.domain.Role;
import com.mc.user.domain.User;
import com.mc.user.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getBy(@PathVariable String username) {
        return ResponseEntity.ok().body(userService.get(username));
    }

    @PostMapping("/")
    public ResponseEntity<User> save(@RequestBody User user) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/").toString());
        return ResponseEntity.created(uri).body(userService.save(user));
    }

    @PostMapping("/role")
    public ResponseEntity<Role> save(@RequestBody Role role) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/role").toString());
        return ResponseEntity.created(uri).body(userService.save(role));
    }

    @PostMapping("/{username}/{roleName}")
    public ResponseEntity<?> addRoleToUser(@PathVariable String username, @PathVariable String roleName) {
        userService.addRoleToUser(username, roleName);
        return ResponseEntity.ok().build();
    }

}
