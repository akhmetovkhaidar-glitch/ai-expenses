package com.haise;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    public record ExpenseView(Long id, Category category, BigDecimal amount) {}

    private final ExpenseRepository repository;

    public ExpenseController(ExpenseRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ExpenseView add(@RequestBody Expense expense) {
        ExpenseEntity saved = repository.save(
                new ExpenseEntity(expense.category(), expense.amount()));
        return toView(saved);
    }

    @GetMapping
    public List<ExpenseView> list() {
        return repository.findAll().stream().map(this::toView).toList();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private ExpenseView toView(ExpenseEntity e) {
        return new ExpenseView(e.getId(), e.getCategory(), e.getAmount());
    }
}