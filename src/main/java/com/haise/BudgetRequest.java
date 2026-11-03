package com.haise;

import java.math.BigDecimal;
import java.util.List;

public record BudgetRequest(BigDecimal income, List<Expense> expenses) {}

