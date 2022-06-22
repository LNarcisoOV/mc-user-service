package com.mc.user.service;

import java.util.List;
import com.mc.user.domain.Role;
import com.mc.user.domain.User;

public interface UserService {
    User save(User user);
    Role save(Role role);
    void addRoleToUser(String username, String roleName);
    User get(String username);
    List<User> getUsers();
    
}
