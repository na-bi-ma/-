package org.example.controller;

import org.example.entity.Favorite;
import org.example.security.JwtUtil;
import org.example.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/favorites")
@CrossOrigin(origins = "http://localhost:5173")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private JwtUtil jwtUtil;

    // Получить избранное пользователя
    @GetMapping
    public ResponseEntity<?> getFavorites(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            if (!jwtUtil.validateToken(token)) {
                return ResponseEntity.status(401).body(Map.of("error", "Требуется авторизация"));
            }
            Long userId = jwtUtil.getUserIdFromToken(token);
            return ResponseEntity.ok(favoriteService.getUserFavorites(userId));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Ошибка авторизации"));
        }
    }

    // Проверить, в избранном ли
    @GetMapping("/check/{uslugaId}")
    public ResponseEntity<?> checkFavorite(@RequestHeader("Authorization") String authHeader,
                                           @PathVariable("uslugaId") Long uslugaId) {
        try {
            String token = authHeader.replace("Bearer ", "");
            if (!jwtUtil.validateToken(token)) {
                return ResponseEntity.status(401).body(Map.of("error", "Требуется авторизация"));
            }
            Long userId = jwtUtil.getUserIdFromToken(token);
            boolean isFav = favoriteService.isFavorite(userId, uslugaId);
            return ResponseEntity.ok(Map.of("isFavorite", isFav));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Ошибка авторизации"));
        }
    }

    // Добавить в избранное
    @PostMapping("/{uslugaId}")
    public ResponseEntity<?> addFavorite(@RequestHeader("Authorization") String authHeader,
                                         @PathVariable("uslugaId") Long uslugaId) {
        try {
            String token = authHeader.replace("Bearer ", "");
            if (!jwtUtil.validateToken(token)) {
                return ResponseEntity.status(401).body(Map.of("error", "Требуется авторизация"));
            }
            Long userId = jwtUtil.getUserIdFromToken(token);
            favoriteService.addFavorite(userId, uslugaId);
            return ResponseEntity.ok(Map.of("message", "Добавлено в избранное"));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }

    // Удалить из избранного
    @DeleteMapping("/{uslugaId}")
    public ResponseEntity<?> removeFavorite(@RequestHeader("Authorization") String authHeader,
                                            @PathVariable("uslugaId") Long uslugaId) {
        try {
            String token = authHeader.replace("Bearer ", "");
            if (!jwtUtil.validateToken(token)) {
                return ResponseEntity.status(401).body(Map.of("error", "Требуется авторизация"));
            }
            Long userId = jwtUtil.getUserIdFromToken(token);
            favoriteService.removeFavorite(userId, uslugaId);
            return ResponseEntity.ok(Map.of("message", "Удалено из избранного"));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }
}