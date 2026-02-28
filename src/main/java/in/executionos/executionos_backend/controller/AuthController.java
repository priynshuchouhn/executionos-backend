package in.executionos.executionos_backend.controller;

import in.executionos.executionos_backend.dto.auth.AuthResponse;
import in.executionos.executionos_backend.dto.auth.LoginRequest;
import in.executionos.executionos_backend.dto.auth.RegisterRequest;
import in.executionos.executionos_backend.security.JwtService;
import in.executionos.executionos_backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(
            @RequestBody Map<String, String> request) {

        String refreshToken = request.get("refreshToken");

        String email = jwtService.extractEmail(refreshToken);

        String newAccessToken = jwtService.generateAccessToken(email);

        return ResponseEntity.ok(
                new AuthResponse(newAccessToken, refreshToken)
        );
    }
}
