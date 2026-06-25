package org.example.controller;

import org.example.entity.Akcija;
import org.example.security.JwtUtil;
import org.example.service.AkcijaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/akcii")
@CrossOrigin(origins = "http://localhost:5173")
public class AkcijaController {

    @Autowired
    private AkcijaService akcijaService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<List<Akcija>> getActiveAkcii() {
        return ResponseEntity.ok(akcijaService.getActiveAkcii());
    }

    @PostMapping
    public ResponseEntity<?> createAkcija(@RequestHeader("Authorization") String authHeader,
                                          @RequestBody Akcija akcija) {
        try {
            String token = authHeader.replace("Bearer ", "");
            if (!jwtUtil.validateToken(token)) {
                return ResponseEntity.status(401).body(Map.of("error", "Требуется авторизация"));
            }
            return ResponseEntity.ok(akcijaService.createAkcija(akcija));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Ошибка авторизации"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAkcija(@RequestHeader("Authorization") String authHeader,
                                          @PathVariable("id") Long id) {
        try {
            String token = authHeader.replace("Bearer ", "");
            if (!jwtUtil.validateToken(token)) {
                return ResponseEntity.status(401).body(Map.of("error", "Требуется авторизация"));
            }
            akcijaService.deleteAkcija(id);
            return ResponseEntity.ok(Map.of("message", "Акция удалена"));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Ошибка авторизации"));
        }
    }
}