package com.example.currencyreport;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(QuoteController.class)
public class QuoteControllerTests {

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
    void quoteEndpointReturnsExpectedFields() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/quote")
                .param("from", "USD")
                .param("to", "BRL"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.from", is("USD")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.to", is("BRL")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price", notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp", notNullValue()));
    }

    @Test
    void historyFromReportEndpointCallsRestTemplate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/history-from-report"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.ok", is(true)));
    }
}
