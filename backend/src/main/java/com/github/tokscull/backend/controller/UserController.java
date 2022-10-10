package com.github.tokscull.backend.controller;

import com.github.tokscull.backend.exeption.EntityNotFoundException;
import com.github.tokscull.backend.model.User;
import com.github.tokscull.backend.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    /**
     * Get all users
     *
     * @return the ResponseEntity with status 200 (OK) and the List of User in body
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        log.info("Received request to get all users");
        return ResponseEntity.ok().body(userService.getUsers());
    }

    /**
     * Get current user
     *
     * @param authentication contains the current logged username
     * @return the ResponseEntity with status 200 (OK) and the User in body
     * @throws EntityNotFoundException when user with username not exist
     */
    @GetMapping("/current")
    public ResponseEntity<User> getCurrentUser(Authentication authentication) {
        log.info("Received request to get current user");
        return ResponseEntity.ok().body(userService.getUserByUsername(authentication.getName()));
    }

    /**
     * Get user by id
     *
     * @param userId the User id which the requested entity should match
     * @return the ResponseEntity with status 200 (OK) and the User in body
     * @throws EntityNotFoundException when user with id not exist
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        log.info("Received request to get user by id: {}", userId);
        return ResponseEntity.ok().body(userService.getUserById(userId));
    }

    /**
     * Get user by username
     *
     * @param username the User username which the requested entity should match
     * @return the ResponseEntity with status 200 (OK) and the User in body
     * @throws EntityNotFoundException when user with username not exist
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(params = "username")
    public ResponseEntity<User> getUserByUsername(@RequestParam(value = "username") String username) {
        log.info("Received request to get user by username: {}", username);
        return ResponseEntity.ok().body(userService.getUserByUsername(username));
    }

}
