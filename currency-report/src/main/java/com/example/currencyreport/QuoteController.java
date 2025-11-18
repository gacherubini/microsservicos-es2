package com.example.currencyreport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Map;

@RestController
public class QuoteController {

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "UP");
    }

    @GetMapping("/quote")
    public Map<String, Object> quote(
            @RequestParam String from,
            @RequestParam String to
    ) {
        return Map.of(
                "from", from,
                "to", to,
                "price", 5.42,
                "timestamp", Instant.now().toString()
        );
    }

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/history-from-report")
    public Object getHistoryFromReport() {
        String url = "http://currency-history:8080/history?from=USD&to=BRL";
        return restTemplate.getForObject(url, Object.class);
    }
}
