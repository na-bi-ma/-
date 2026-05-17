package org.example.service;

import org.example.dto.LoginRequest;
import org.example.dto.RegisterRequest;
import org.example.entity.User;
import org.example.repository.UserRepository;
import org.example.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // Регистрация
    public User register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email уже используется");
        }
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new RuntimeException("Телефон уже используется");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());

        return userRepository.save(user);
    }

    // Вход — возвращает JWT токен
    public String login(LoginRequest request) {
        User user = userRepository.findByEmailOrPhone(request.getLogin(), request.getLogin())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Неверный пароль");
        }

        // Передаём роль в токен
        return jwtUtil.generateToken(user.getId(), user.getEmail(), user.getName(), user.getRole());
    }

    // Получить пользователя по ID
    public User getCurrentUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
    }
}