package bd.edu.seu.softwaredevelopment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration

public class WebClientConfig {
    @Bean
    public WebClient mlWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8000") // Pointing to your Python ML Service
                .build();
    }
}
