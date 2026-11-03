package com.haise;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class BudgetController {

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
    private final BudgetSummaryFormatter formatter;
    private final GeminiClient geminiClient;
    public BudgetController(BudgetSummaryFormatter formatter, GeminiClient geminiClient) {
        this.formatter = formatter;
        this.geminiClient = geminiClient;
    }
    @PostMapping("/summary")
    public Map<String, String> summary(@RequestBody BudgetRequest request) {
        String summary = formatter.createSummary(request.income(), request.expenses());
        return Map.of("summary", summary);
    }
    @PostMapping("/analysis")
    public ResponseEntity<Map<String, String>> analysis(@RequestBody BudgetRequest request) {
        String summary = formatter.createSummary(request.income(), request.expenses());
        try {
            String analysis = geminiClient.ask(AI_INSTRUCTION, summary);
            return ResponseEntity.ok(Map.of("summary", summary, "analysis", analysis));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body(Map.of("error", "AI request failed: " + e.getMessage()));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Request was interrupted"));
        }
    }
    @ExceptionHandler({IllegalArgumentException.class, NullPointerException.class})
    public ResponseEntity<Map<String, String>> handleBadInput(RuntimeException e) {
        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }
}
