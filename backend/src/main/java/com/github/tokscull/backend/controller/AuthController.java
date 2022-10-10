package com.github.tokscull.backend.controller;

import com.github.tokscull.backend.exeption.EntityAlreadyExistsException;
import com.github.tokscull.backend.exeption.EntityNotFoundException;
import com.github.tokscull.backend.model.rest.AuthenticationResponse;
import com.github.tokscull.backend.model.rest.LoginRequest;
import com.github.tokscull.backend.model.rest.RefreshTokenRequest;
import com.github.tokscull.backend.model.rest.RegisterRequest;
import com.github.tokscull.backend.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * Create new user
     *
     * @param registerRequest include required User information
     * @return the ResponseEntity with status 201 (CREATED) and the username in body
     * @throws EntityAlreadyExistsException when user with id already exist
     */
    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody RegisterRequest registerRequest) {
        log.info("Received request to signup new user: {}", registerRequest.getUsername());
        authService.signup(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Authenticate user
     *
     * @param loginRequest include required User information
     * @return the ResponseEntity with status 200 (OK) and the AuthenticationResponse in body
     * @throws org.springframework.security.authentication.BadCredentialsException when provided credentials don't match
     */
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest loginRequest) {
        log.info("Received request to authenticate user: {}", loginRequest.getUsername());
        return ResponseEntity.ok().body(authService.login(loginRequest));
    }

    /**
     * Refresh user access, generate new tokens
     *
     * @param refreshTokenRequest include refreshToken and username
     * @return the ResponseEntity with status 200 (OK) and the AuthenticationResponse in body
     * @throws EntityNotFoundException when refreshToken not exist
     */
    @PostMapping("/refresh/token")
    public AuthenticationResponse refreshTokens(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        log.info("Received request to refresh token user: {}", refreshTokenRequest.getUsername());
        return authService.refreshToken(refreshTokenRequest);
    }

    /**
     * Logout user, remove user refreshToken
     *
     * @param refreshTokenRequest include refreshToken and username
     * @return the ResponseEntity with status 200 (OK) and body message
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        log.info("Received request to logout user: {}", refreshTokenRequest.getUsername());
        authService.logout(refreshTokenRequest);
        return ResponseEntity.status(OK).build();
    }
}
