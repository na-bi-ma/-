package org.example.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "vrach")
public class Vrach {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "FULL_NAME", nullable = false, length = 150)
    private String fullName;

    @Column(name = "PHONE", length = 15)
    private String phone;

    @Column(name = "CATEGORY_ID")
    private Long categoryId;

    public Vrach() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}