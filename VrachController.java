package org.example.controller;

import org.example.entity.Vrach;
import org.example.security.JwtUtil;
import org.example.service.VrachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vrachi")
@CrossOrigin(origins = "http://localhost:5173")
public class VrachController {

    @Autowired
    private VrachService vrachService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<List<Vrach>> getVrachi(@RequestParam(required = false) String q) {
        if (q != null && !q.isEmpty()) {
            return ResponseEntity.ok(vrachService.searchVrachi(q));
        }
        return ResponseEntity.ok(vrachService.getAllVrachi());
    }

    @PostMapping
    public ResponseEntity<?> createVrach(@RequestHeader("Authorization") String authHeader,
                                         @RequestBody Vrach vrach) {
        try {
            String token = authHeader.replace("Bearer ", "");
            if (!jwtUtil.validateToken(token)) {
                return ResponseEntity.status(401).body(Map.of("error", "Требуется авторизация"));
            }
            return ResponseEntity.ok(vrachService.createVrach(vrach));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Ошибка авторизации"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateVrach(@RequestHeader("Authorization") String authHeader,
                                         @PathVariable("id") Long id,
                                         @RequestBody Vrach vrach) {
        try {
            String token = authHeader.replace("Bearer ", "");
            if (!jwtUtil.validateToken(token)) {
                return ResponseEntity.status(401).body(Map.of("error", "Требуется авторизация"));
            }
            Vrach existing = vrachService.getVrachById(id);
            existing.setFullName(vrach.getFullName());
            existing.setCategoryId(vrach.getCategoryId());
            existing.setPhone(vrach.getPhone());
            return ResponseEntity.ok(vrachService.createVrach(existing));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVrach(@RequestHeader("Authorization") String authHeader,
                                         @PathVariable("id") Long id) {
        try {
            String token = authHeader.replace("Bearer ", "");
            if (!jwtUtil.validateToken(token)) {
                return ResponseEntity.status(401).body(Map.of("error", "Требуется авторизация"));
            }
            vrachService.deleteVrach(id);
            return ResponseEntity.ok(Map.of("message", "Врач удалён"));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Ошибка авторизации"));
        }
    }
}