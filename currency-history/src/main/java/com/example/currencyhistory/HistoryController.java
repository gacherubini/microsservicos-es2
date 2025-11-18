package com.example.currencyhistory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
public class HistoryController {

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "UP");
    }

    @GetMapping("/history")
    public Map<String, Object> history(
            @RequestParam String from,
            @RequestParam String to
    ) {
        return Map.of(
                "from", from,
                "to", to,
                "values", List.of(
                        Map.of("timestamp", Instant.now().minusSeconds(300).toString(), "price", 5.42),
                        Map.of("timestamp", Instant.now().minusSeconds(180).toString(), "price", 5.47),
                        Map.of("timestamp", Instant.now().minusSeconds(60).toString(), "price", 5.45)
                )
        );
    }

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/quote-from-history")
    public Object getQuoteFromHistory() {
        String url = "http://currency-report:8080/quote?from=USD&to=BRL";
        return restTemplate.getForObject(url, Object.class);
    }
}
