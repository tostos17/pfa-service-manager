package com.pfa.app.controller;

import com.pfa.app.dto.ParentRegisterRequest;
import com.pfa.app.dto.request.*;
import com.pfa.app.dto.response.AuthResponse;
import com.pfa.app.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register/coach")
    public ResponseEntity<AuthResponse> registerCoach(@Valid @RequestBody CoachRegisterRequest request) {
        return ResponseEntity.ok(authService.registerCoach(request));
    }

    @PostMapping(value = "/register/player", consumes = {"multipart/form-data"})
    public ResponseEntity<AuthResponse> registerPlayer(
            @Valid @ModelAttribute PlayerRegisterRequest request,
            @RequestParam(value = "passportPhoto", required = false) MultipartFile passportPhoto
    ) {
        return ResponseEntity.ok(authService.registerPlayer(request, passportPhoto));
    }

    @PostMapping("/register/admin")
    public ResponseEntity<AuthResponse> registerAdmin(@Valid @RequestBody AdminRegisterRequest request) {
        return ResponseEntity.ok(authService.registerAdmin(request));
    }

    @PostMapping("/register/parent")
    public ResponseEntity<AuthResponse> registerParent(@Valid @RequestBody ParentRegisterRequest request) {
        return ResponseEntity.ok(authService.registerParent(request));
    }
}