package com.example.currencyhistory;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(HistoryController.class)
public class HistoryControllerTests {

    @Autowired
    private MockMvc mockMvc;

    // Provide a simple RestTemplate bean for tests to avoid Mockito inline mock-maker issues.
    @TestConfiguration
    static class TestRestTemplateConfig {
        @Bean
        public RestTemplate restTemplate() {
            return new RestTemplate() {
                @Override
                public <T> T getForObject(String url, Class<T> responseType, Object... uriVariables) {
                    // return a JSON-serializable map for tests
                    return (T) Map.of("ok", true);
                }
            };
        }
    }

    @Test
    void healthEndpointReturnsUp() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/health"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", is("UP")));
    }

    @Test
    void historyEndpointReturnsExpectedFields() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/history")
                .param("from", "USD")
                .param("to", "BRL"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.from", is("USD")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.to", is("BRL")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.values", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.values[0].price", notNullValue()));
    }

    @Test
    void quoteFromHistoryEndpointCallsRestTemplate() throws Exception {
        // The TestRestTemplateConfig returns a Map {"ok": true} so controller should return JSON with ok=true
        mockMvc.perform(MockMvcRequestBuilders.get("/quote-from-history"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.ok", is(true)));
    }
}
