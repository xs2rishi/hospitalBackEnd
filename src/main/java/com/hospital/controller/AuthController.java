package com.hospital.controller;

import com.hospital.dto.AuthRequest;
import com.hospital.dto.AuthResponse;
import com.hospital.entity.User;
import com.hospital.repository.UserRepository;
import com.hospital.security.JwtUtil;
import com.hospital.security.CustomUserDetailsService;
import com.hospital.annotation.Audited;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    @Audited(action = "USER_REGISTRATION", entityType = "User")
    public ResponseEntity<User> register(@RequestBody User user) {
        logger.info("User registration attempt for username: {}", user.getUsername());
        // Encode password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        // Don't return password in response
        savedUser.setPassword(null);
        logger.info("User registered successfully: {}", savedUser.getUsername());
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/login")
    @Audited(action = "USER_LOGIN", entityType = "User")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        logger.info("Login attempt for username: {}", request.getUsername());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        final String token = jwtUtil.generateToken(userDetails);

        logger.info("User logged in successfully: {}", request.getUsername());
        return ResponseEntity.ok(AuthResponse.builder().token(token).build());
    }

    @GetMapping("/me")
    @Audited(action = "GET_CURRENT_USER", entityType = "User")
    public ResponseEntity<User> getCurrentUser(org.springframework.security.core.Authentication authentication) {
        String username = authentication.getName();
        logger.debug("Fetching current user: {}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        // Don't return password in response
        user.setPassword(null);
        return ResponseEntity.ok(user);
    }
}
