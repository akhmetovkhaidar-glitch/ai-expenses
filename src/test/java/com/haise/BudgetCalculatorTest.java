package com.haise;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BudgetCalculatorTest {

    private final BudgetCalculator calculator = new BudgetCalculator();

    @Test
    void calculateTotalAddsAllExpenses() {
        List<Expense> expenses = List.of(
                new Expense(Category.RENT, new BigDecimal("600.00")),
                new Expense(Category.CAFE, new BigDecimal("15.50"))
        );
        assertEquals(new BigDecimal("615.50"), calculator.calculateTotal(expenses));
    }

    @Test
    void calculateTotalByCategoryGroupsSameCategory() {
        List<Expense> expenses = List.of(
                new Expense(Category.GROCERIES, new BigDecimal("300.00")),
                new Expense(Category.GROCERIES, new BigDecimal("25.99"))
        );
        Map<Category, BigDecimal> result = calculator.calculateTotalByCategory(expenses);
        assertEquals(new BigDecimal("325.99"), result.get(Category.GROCERIES));
    }

    @Test
    void calculatePercentageOfIncomeRoundsToTwoDecimals() {
        List<Expense> expenses = List.of(new Expense(Category.GROCERIES, new BigDecimal("325.99")));
        Map<Category, BigDecimal> result =
                calculator.calculatePercentageOfIncome(new BigDecimal("1500.00"), expenses);
        assertEquals(new BigDecimal("21.73"), result.get(Category.GROCERIES));
    }

    @Test
    void calculatePercentageOfIncomeRejectsZeroIncome() {
        List<Expense> expenses = List.of(new Expense(Category.CAFE, new BigDecimal("10.00")));
        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculatePercentageOfIncome(BigDecimal.ZERO, expenses));
    }

    @Test
    void expenseRejectsNegativeAmount() {
        assertThrows(IllegalArgumentException.class,
                () -> new Expense(Category.CAFE, new BigDecimal("-5.00")));
    }
}