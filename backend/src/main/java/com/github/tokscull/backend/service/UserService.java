package com.github.tokscull.backend.service;

import com.github.tokscull.backend.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface UserService extends UserDetailsService {

    void createUser(User user);

    Boolean existsByUsername(String username);

    User getUserById(Long id);

    User getUserByUsername(String username);

    List<User> getUsers();

    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

}
