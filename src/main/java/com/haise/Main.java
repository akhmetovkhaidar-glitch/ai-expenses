package com.haise;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class Main {
    private static final String AI_INSTRUCTION = """
            You are a personal budget assistant.
            You receive a monthly budget summary that was already calculated in a java program.
            Rules:
            - Use only the numbers from the summary. Do not recalculate or change them.
            - Do not invent any data that is not in the summary.
            - Name the 2-3 categories with the most potential for savings and give short, practical tips.
            - Do not give investment, credit or tax advice.
            - Answer in English, maximum 120 words.
            - Use plain text only, no Markdown formatting.
            """;
    public static void main(String[] args) {
        BigDecimal income = new BigDecimal("1500.00");

        List<Expense> expenses = List.of(
                new Expense(Category.RENT, new BigDecimal("600.00")),
                new Expense(Category.GROCERIES, new BigDecimal("300.00")),
                new Expense(Category.GROCERIES, new BigDecimal("25.99")),
                new Expense(Category.CAFE, new BigDecimal("215.00")),
                new Expense(Category.TRANSPORT, new BigDecimal("167.50"))
        );
        BudgetCalculator calculator = new BudgetCalculator();
        BudgetSummaryFormatter formatter = new BudgetSummaryFormatter(calculator);
        String summary = formatter.createSummary(income, expenses);
        System.out.println(summary);
        String apiKey = System.getenv("GEMINI_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            System.out.println("GEMINI_API_KEY is not set!");
            return;
        }
        GeminiClient geminiClient = new GeminiClient(apiKey);
        try {
            String analysis = geminiClient.ask(AI_INSTRUCTION, summary);
            System.out.println();
            System.out.println("AI analysis and instructions: ");
            System.out.println(analysis);
        } catch (IOException | InterruptedException e) {
            System.out.println("AI analysis and instructions failed: " + e.getMessage());
        }
    }
}
