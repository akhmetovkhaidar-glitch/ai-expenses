package com.haise;

import java.math.RoundingMode;
import java.math.BigDecimal;
import java.util.Objects;

    public record Expense(Category category, BigDecimal amount) {
        public Expense {
            Objects.requireNonNull(category, "There should be a category!");
            Objects.requireNonNull(amount, "There should be an amount!");
            if (amount.signum() <= 0) {
                throw new IllegalArgumentException("Amount must be greater than zero!");
            }
            if (amount.stripTrailingZeros().scale() > 2) {
                throw new IllegalArgumentException("Amount can have at most 2 decimal places!");
            }
            amount = amount.setScale(2, RoundingMode.HALF_UP);
        }
    }

