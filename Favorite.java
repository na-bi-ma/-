package org.example.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "favorite", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"USER_ID", "USLUGA_ID"})
})
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USER_ID", nullable = false)
    private Long userId;

    @Column(name = "USLUGA_ID", nullable = false)
    private Long uslugaId;

    public Favorite() {}

    public Favorite(Long userId, Long uslugaId) {
        this.userId = userId;
        this.uslugaId = uslugaId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getUslugaId() { return uslugaId; }
    public void setUslugaId(Long uslugaId) { this.uslugaId = uslugaId; }
}