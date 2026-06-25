package org.example.controller;

import org.example.entity.Appointment;
import org.example.security.JwtUtil;
import org.example.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "http://localhost:5173")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private JwtUtil jwtUtil;

    // Записаться на приём
    @PostMapping
    public ResponseEntity<?> createAppointment(@RequestHeader("Authorization") String authHeader,
                                               @RequestBody Appointment appointment) {
        try {
            String token = authHeader.replace("Bearer ", "");
            if (!jwtUtil.validateToken(token)) {
                return ResponseEntity.status(401).body(Map.of("error", "Требуется авторизация"));
            }
            Long userId = jwtUtil.getUserIdFromToken(token);
            appointment.setUserId(userId);
            appointment.setId(null);
            return ResponseEntity.ok(appointmentService.createAppointment(appointment));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }

    // Мои записи
    @GetMapping
    public ResponseEntity<?> getUserAppointments(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            if (!jwtUtil.validateToken(token)) {
                return ResponseEntity.status(401).body(Map.of("error", "Требуется авторизация"));
            }
            Long userId = jwtUtil.getUserIdFromToken(token);
            return ResponseEntity.ok(appointmentService.getUserAppointments(userId));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Ошибка авторизации"));
        }
    }

    // Занятые слоты врача на дату
    @GetMapping("/slots")
    public ResponseEntity<?> getVrachSlots(@RequestParam Long vrachId,
                                           @RequestParam String date) {
        try {
            java.time.LocalDate localDate = java.time.LocalDate.parse(date);
            return ResponseEntity.ok(appointmentService.getVrachAppointments(vrachId, localDate));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }

    // Удалить запись
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAppointment(@RequestHeader("Authorization") String authHeader,
                                               @PathVariable("id") Long id) {
        try {
            String token = authHeader.replace("Bearer ", "");
            if (!jwtUtil.validateToken(token)) {
                return ResponseEntity.status(401).body(Map.of("error", "Требуется авторизация"));
            }
            appointmentService.deleteAppointment(id);
            return ResponseEntity.ok(Map.of("message", "Запись удалена"));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }
}