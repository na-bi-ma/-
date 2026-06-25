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

    // Получить все услуги или поиск — доступно всем
    @GetMapping
    public ResponseEntity<List<Usluga>> getUslugi(@RequestParam(required = false) String name) {
        if (name != null && !name.isEmpty()) {
            return ResponseEntity.ok(uslugaService.searchUslugi(name));
        }
        return ResponseEntity.ok(uslugaService.getAllUslugi());
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

    // Изменить услугу — только авторизованные
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUsluga(@RequestHeader("Authorization") String authHeader,
                                          @PathVariable("id") Long id,
                                          @RequestBody Usluga usluga) {
        try {
            String token = authHeader.replace("Bearer ", "");
            if (!jwtUtil.validateToken(token)) {
                return ResponseEntity.status(401).body(Map.of("error", "Требуется авторизация"));
            }
            Usluga existing = uslugaService.getUslugaById(id);
            existing.setName(usluga.getName());
            existing.setPrice(usluga.getPrice());
            existing.setDate(usluga.getDate());
            existing.setTime(usluga.getTime());
            existing.setVrachId(usluga.getVrachId());
            existing.setCategoryId(usluga.getCategoryId());
            return ResponseEntity.ok(uslugaService.createUsluga(existing));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }

    // Удалить услугу — только авторизованные
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUsluga(@RequestHeader("Authorization") String authHeader,
                                          @PathVariable("id") Long id) {
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