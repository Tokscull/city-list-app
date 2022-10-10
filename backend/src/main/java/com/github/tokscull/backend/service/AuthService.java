package com.github.tokscull.backend.service;

import com.github.tokscull.backend.model.Role;
import com.github.tokscull.backend.model.User;
import com.github.tokscull.backend.model.rest.AuthenticationResponse;
import com.github.tokscull.backend.model.rest.LoginRequest;
import com.github.tokscull.backend.model.rest.RefreshTokenRequest;
import com.github.tokscull.backend.model.rest.RegisterRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final RoleService roleService;
    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;

    public void signup(RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRoles(Set.of(roleService.getRoleByName("ROLE_USER")));

        userService.createUser(user);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = tokenService.generateToken(authenticate);
        return AuthenticationResponse.builder()
                .accessToken(token)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .build();
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        Set<String> roles = userService.getUserByUsername(refreshTokenRequest.getUsername())
                .getRoles().stream().map(Role::getName).collect(Collectors.toSet());
        String token = tokenService.generateTokenWithUserNameAndRoles(refreshTokenRequest.getUsername(), roles);
        return AuthenticationResponse.builder()
                .accessToken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .build();
    }

    public void logout(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
    }

}
