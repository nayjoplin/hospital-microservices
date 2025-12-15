package com.hospital.agendamento.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuração do WebClient para comunicação entre microsserviços
 */
@Configuration
public class WebClientConfig {

    /**
     * Bean do WebClient para chamadas HTTP assíncronas
     */
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
