package com.mc.user.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.mc.user.domain.Role;
import com.mc.user.domain.User;
import com.mc.user.repository.RoleRepository;
import com.mc.user.repository.UserRepository;

@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final User user = userRepository.findByUsername(username);
        if (user == null) {
            LOGGER.info("User not found in the database.");
            throw new UsernameNotFoundException("User not found in the database.");
        }

        LOGGER.info("User found in the database {}.", username);

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));

        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(), authorities);
    }


    @Override
    public User save(User user) {
        LOGGER.info("Saving new user {} to the database.", user.getName());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
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
        final User userDB = userRepository.findByUsername(username);
        final Role roleDB = roleRepository.findByName(roleName);
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
