package com.femmie.shoppingmall.controller;

import com.femmie.shoppingmall.config.JwtUtil;
import com.femmie.shoppingmall.model.AppUser;
import com.femmie.shoppingmall.model.RefreshToken;
import com.femmie.shoppingmall.model.Role;
import com.femmie.shoppingmall.repository.RoleRepository;
import com.femmie.shoppingmall.repository.UserRepository;
import com.femmie.shoppingmall.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;

    @PostMapping("/register")
    public ResponseEntity<AppUser> register(@RequestBody AppUser user) {
        Role role = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Role not found"));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Set.of(role));
        AppUser savedUser = userRepository.save(user);
        System.out.println("Register endpoint hit");
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody AppUser user) {
        System.out.println("Login attempt for username: " + user.getUsername());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

            if (authentication.isAuthenticated()) {
                AppUser appUser = userRepository.findByUsername(user.getUsername()).orElseThrow();

                List<String> roles = appUser.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toList());

                String accessToken = jwtUtil.generateAccessToken(appUser.getUsername(), roles);
                RefreshToken refreshToken = refreshTokenService.createRefreshToken(appUser.getId());

                Map<String, String> tokens = new HashMap<>();
                tokens.put("accessToken", accessToken);
                tokens.put("refreshToken", refreshToken.getToken());
                return ResponseEntity.ok(tokens);
            }

            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        } catch (AuthenticationException e) {
            throw new RuntimeException(e);
        }
    }


    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        Optional<RefreshToken> tokenOpt = refreshTokenService.findByToken(refreshToken);
        if (tokenOpt.isPresent()) {
            RefreshToken token = refreshTokenService.verifyExpiration(tokenOpt.get());
            String accessToken = jwtUtil.generateAccessToken(token.getUser().getUsername());
            return ResponseEntity.ok(Map.of("accessToken", accessToken, "refreshToken", refreshToken));
        }
        return ResponseEntity.status(401).body(Map.of("error", "Invalid or expired refresh token"));
    }
}
