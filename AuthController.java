package org.example.controller;

import org.example.dto.LoginRequest;
import org.example.dto.RegisterRequest;
import org.example.entity.User;
import org.example.security.JwtUtil;
import org.example.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    // Регистрация
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            User user = authService.register(request);
            return ResponseEntity.ok(Map.of(
                    "message", "Регистрация успешна",
                    "userId", user.getId()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Вход
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            String token = authService.login(request);
            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "message", "Вход выполнен"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Информация о текущем пользователе
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");

            if (!jwtUtil.validateToken(token)) {
                return ResponseEntity.status(401).body(Map.of("error", "Недействительный токен"));
            }

            Long userId = jwtUtil.getUserIdFromToken(token);
            User user = authService.getCurrentUser(userId);

            return ResponseEntity.ok(Map.of(
                    "id", user.getId(),
                    "email", user.getEmail(),
                    "phone", user.getPhone(),
                    "name", user.getName(),
                    "createdAt", user.getCreatedAt(),
                    "role", user.getRole()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Ошибка авторизации"));
        }
    }
}
