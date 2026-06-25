package org.example.controller;

import org.example.entity.Review;
import org.example.security.JwtUtil;
import org.example.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "http://localhost:5173")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private JwtUtil jwtUtil;

    // Получить все отзывы — доступно всем
    @GetMapping
    public ResponseEntity<?> getAllReviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    // Оставить отзыв — только авторизованные
    @PostMapping
    public ResponseEntity<?> createReview(@RequestHeader("Authorization") String authHeader,
                                          @RequestBody Review review) {
        try {
            String token = authHeader.replace("Bearer ", "");
            if (!jwtUtil.validateToken(token)) {
                return ResponseEntity.status(401).body(Map.of("error", "Требуется авторизация"));
            }
            Long userId = jwtUtil.getUserIdFromToken(token);
            review.setUserId(userId);
            review.setId(null);
            return ResponseEntity.ok(reviewService.createReview(review));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }
}