package com.haise;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component

public class BudgetSummaryFormatter {
    private final BudgetCalculator calculator;
    public BudgetSummaryFormatter(BudgetCalculator calculator) {
        this.calculator = calculator;
    }
    public String createSummary(BigDecimal income, List<Expense> expenses) {
        BigDecimal total = calculator.calculateTotal(expenses);
        BigDecimal balance = calculator.calculateBalance(income, expenses);
        Map<Category, BigDecimal> totalByCategory = calculator.calculateTotalByCategory(expenses);
        Map<Category, BigDecimal> percentages = calculator.calculatePercentageOfIncome(income, expenses);
        StringBuilder summary = new StringBuilder();
        summary.append("Income: ").append(income).append(" EUR\n");
        summary.append("Expenses by category:\n");
        for (Category category : totalByCategory.keySet()) {
            summary.append(" ").append(category)
                    .append(": ")
                    .append(totalByCategory.get(category))
                    .append(" EUR (")
                    .append(percentages.get(category))
                    .append("% of income)\n");
        }
        summary.append("Total expenses: ").append(total).append(" EUR\n");
        summary.append("Balance: ").append(balance).append(" EUR\n");
        return summary.toString();
    }
}
