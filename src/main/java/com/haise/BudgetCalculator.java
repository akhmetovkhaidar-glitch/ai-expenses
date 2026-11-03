package com.haise;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.EnumMap;
import java.util.Map;
import java.util.List;

import org.springframework.stereotype.Component;

@Component

public class BudgetCalculator {
    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");
    public BigDecimal calculateTotal(List<Expense> expenses) {
        BigDecimal total = BigDecimal.ZERO;
        for (Expense expense : expenses) {
            total = total.add(expense.amount());
        }
        return total;
    }
    public Map<Category, BigDecimal> calculateTotalByCategory(List<Expense> expenses) {
        Map<Category, BigDecimal> totalByCategory = new EnumMap<>(Category.class);
        for (Expense expense : expenses) {
            BigDecimal current = totalByCategory.getOrDefault(expense.category(), BigDecimal.ZERO);
            totalByCategory.put(expense.category(), current.add(expense.amount()));
        }
        return totalByCategory;
    }
    public BigDecimal calculateBalance(BigDecimal income, List<Expense> expenses) {
        return income.subtract(calculateTotal(expenses));
    }
    public Map<Category, BigDecimal> calculatePercentageOfIncome(BigDecimal income, List<Expense> expenses) {
        if (income.signum() <= 0) {
            throw new IllegalArgumentException("Income must be greater than zero!");
        }
        Map<Category, BigDecimal> totalsByCategory = calculateTotalByCategory(expenses);
        Map<Category, BigDecimal> percentages = new EnumMap<>(Category.class);
        for (Category category : totalsByCategory.keySet()) {
            BigDecimal amount = totalsByCategory.get(category);
            BigDecimal percentage = amount.multiply(ONE_HUNDRED).divide(income, 2, RoundingMode.HALF_UP);
            percentages.put(category, percentage);
        }
        return percentages;
    }
}
