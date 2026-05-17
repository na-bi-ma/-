package org.example.controller;
import org.example.entity.Usluga;
import org.example.security.JwtUtil;
import org.example.service.UslugaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/uslugi")
@CrossOrigin(origins = "http://localhost:5173")
public class UslugaController {

    @Autowired
    private UslugaService uslugaService;

    @Autowired
    private JwtUtil jwtUtil;

    // Получить все услуги — доступно всем
    @GetMapping
    public ResponseEntity<List<Usluga>> getAllUslugi() {
        return ResponseEntity.ok(uslugaService.getAllUslugi());
    }

    // Поиск услуг — доступно всем
    @GetMapping("/search")
    public ResponseEntity<List<Usluga>> searchUslugi(@RequestParam String name) {
        return ResponseEntity.ok(uslugaService.searchUslugi(name));
    }

    // Создать услугу — только авторизованные
    @PostMapping
    public ResponseEntity<?> createUsluga(@RequestHeader("Authorization") String authHeader,
                                          @RequestBody Usluga usluga) {
        try {
            String token = authHeader.replace("Bearer ", "");
            if (!jwtUtil.validateToken(token)) {
                return ResponseEntity.status(401).body(Map.of("error", "Требуется авторизация"));
            }
            Usluga created = uslugaService.createUsluga(usluga);
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Ошибка авторизации"));
        }
    }

    // Удалить услугу — только авторизованные
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUsluga(@RequestHeader("Authorization") String authHeader,
                                          @PathVariable Long id) {
        try {
            String token = authHeader.replace("Bearer ", "");
            if (!jwtUtil.validateToken(token)) {
                return ResponseEntity.status(401).body(Map.of("error", "Требуется авторизация"));
            }
            uslugaService.deleteUsluga(id);
            return ResponseEntity.ok(Map.of("message", "Услуга удалена"));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Ошибка авторизации"));
        }
    }
}