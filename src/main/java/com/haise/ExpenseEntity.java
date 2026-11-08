package com.haise;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "expenses")
public class ExpenseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    protected ExpenseEntity() {}

    public ExpenseEntity(Category category, BigDecimal amount) {
        this.category = category;
        this.amount = amount;
    }

    public Long getId() { return id; }
    public Category getCategory() { return category; }
    public BigDecimal getAmount() { return amount; }

    public Expense toExpense() {
        return new Expense(category, amount);
    }
}
