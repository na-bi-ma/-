package org.example.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "EMAIL", unique = true, nullable = false, length = 100)
    private String email;

    @Column(name = "PHONE", unique = true, nullable = false, length = 15)
    private String phone;

    @Column(name = "PASSWORD", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "MOTHER_MAIDEN_NAME", length = 100)
    private String motherMaidenName;

    @Column(name = "FULL_NAME", nullable = false, length = 150)
    private String name;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "ROLE", length = 20)
    private String role = "USER";

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Конструкторы
    public User() {}

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getMotherMaidenName() { return motherMaidenName; }
    public void setMotherMaidenName(String motherMaidenName) { this.motherMaidenName = motherMaidenName; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}