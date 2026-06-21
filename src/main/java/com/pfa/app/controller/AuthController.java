package com.pfa.app.controller;

import com.pfa.app.constants.StringValues;
import com.pfa.app.dto.request.*;
import com.pfa.app.dto.response.AuthResponse;
import com.pfa.app.service.AuthService;
import com.pfa.app.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequestMapping(StringValues.AUTH_PATH)
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register/coach")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthResponse> registerCoach(@Valid @RequestBody CoachRegisterRequest request) {
        return ResponseEntity.ok(authService.registerCoach(request));
    }

    @PostMapping(value = "/register/player", consumes = {"multipart/form-data"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthResponse> registerPlayer(
            @Valid @ModelAttribute PlayerRegisterRequest request,
            @RequestParam(value = "passportPhoto", required = false) MultipartFile passportPhoto
    ) {
        return ResponseEntity.ok(authService.registerPlayer(request, passportPhoto));
    }

    @PostMapping("/register/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthResponse> registerAdmin(@Valid @RequestBody AdminRegisterRequest request) {
        return ResponseEntity.ok(authService.registerAdmin(request));
    }

    @PostMapping("/register/parent")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthResponse> registerParent(@Valid @RequestBody ParentRegisterRequest request) {
        return ResponseEntity.ok(authService.registerParent(request));
    }

    @PostMapping("/force-change-password")
    public ResponseEntity<String> forceChangePassword(
            @RequestBody PasswordChangeRequest request,
            Principal principal) {

        userService.changeInitialPassword(principal.getName(), request.oldPassword(), request.newPassword());
        return ResponseEntity.ok("Password updated successfully. You can now access the application.");
    }
}