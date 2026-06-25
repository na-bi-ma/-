package org.example.entity;
import jakarta.persistence.*;

@Entity
@Table(name = "usluga")
public class Usluga {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME", nullable = false, length = 150)
    private String name;

    @Column(name = "PRICE")
    private java.math.BigDecimal price;

    @Column(name = "DATE")
    private java.time.LocalDate date;

    @Column(name = "TIME")
    private java.time.LocalTime time;

    @Column(name = "ID_VRACH")
    private Long vrachId;

    @Column(name = "CATEGORY_ID")
    private Long categoryId;


    // Конструкторы
    public Usluga() {}

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public java.math.BigDecimal getPrice() { return price; }
    public void setPrice(java.math.BigDecimal price) { this.price = price; }

    public java.time.LocalDate getDate() { return date; }
    public void setDate(java.time.LocalDate date) { this.date = date; }

    public java.time.LocalTime getTime() { return time; }
    public void setTime(java.time.LocalTime time) { this.time = time; }

    public Long getVrachId() { return vrachId; }
    public void setVrachId(Long vrachId) { this.vrachId = vrachId; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
}