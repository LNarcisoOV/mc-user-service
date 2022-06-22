package com.mc.user.service;

import java.util.List;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.mc.user.domain.Role;
import com.mc.user.domain.User;
import com.mc.user.repository.RoleRepository;
import com.mc.user.repository.UserRepository;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public User save(User user) {
        LOGGER.info("Saving new user {} to the database.", user.getName());
        return userRepository.save(user);
    }

    @Override
    public Role save(Role role) {
        LOGGER.info("Saving new role {} to the database.", role.getName());
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        LOGGER.info("Adding new role {} to the user {}.", roleName, username);
        User userDB = userRepository.findByUsername(username);
        Role roleDB = roleRepository.findByName(roleName);  
        userDB.getRoles().add(roleDB);

    }

    @Override
    public User get(String username) {
        LOGGER.info("Fetching user {}.", username);
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> getUsers() {
        LOGGER.info("Fetching all users.");
        return userRepository.findAll();
    }

}
