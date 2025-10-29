// AuthController.java
package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.security.JwtProvider;
import com.deliverytech.delivery_api.service.CustomUserDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    public AuthController(AuthenticationManager authenticationManager, JwtProvider jwtProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    record LoginRequest(String username, String password) {}

    record LoginResponse(String token) {}

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        Authentication auth = authenticationManager
            .authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));

        String token = jwtProvider.generateToken(auth.getName());
        return ResponseEntity.ok(new LoginResponse(token));
    }
}
