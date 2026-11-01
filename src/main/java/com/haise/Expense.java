package com.haise;

import javax.naming.spi.ObjectFactory;
import java.math.BigDecimal;
import java.util.Objects;

    public record Expense(Category category, BigDecimal amount) {
        public Expense {
            Objects.requireNonNull(category, "There should be a category!");
            Objects.requireNonNull(amount, "There should be an amount!");
            if (amount.signum() <= 0) {
                throw new IllegalArgumentException("Amount must be greater than zero!");
            }
        }
    }

