package com.hospital.clinica.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração do RabbitMQ para mensageria entre microsserviços
 */
@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.queue.consulta}")
    private String consultaQueue;

    @Value("${rabbitmq.queue.exame}")
    private String exameQueue;

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.routingkey.consulta}")
    private String consultaRoutingKey;

    @Value("${rabbitmq.routingkey.exame}")
    private String exameRoutingKey;

    /**
     * Cria a fila de consultas
     */
    @Bean
    public Queue consultaQueue() {
        return new Queue(consultaQueue, true); // durável
    }

    /**
     * Cria a fila de exames
     */
    @Bean
    public Queue exameQueue() {
        return new Queue(exameQueue, true); // durável
    }

    /**
     * Cria o exchange tipo Topic
     */
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchange);
    }

    /**
     * Faz o binding da fila de consultas com o exchange
     */
    @Bean
    public Binding consultaBinding() {
        return BindingBuilder
                .bind(consultaQueue())
                .to(exchange())
                .with(consultaRoutingKey);
    }

    /**
     * Faz o binding da fila de exames com o exchange
     */
    @Bean
    public Binding exameBinding() {
        return BindingBuilder
                .bind(exameQueue())
                .to(exchange())
                .with(exameRoutingKey);
    }

    /**
     * Conversor de mensagens para JSON
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Configura o RabbitTemplate com o conversor JSON
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
