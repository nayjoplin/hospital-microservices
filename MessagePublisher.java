package com.hospital.agendamento.messaging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Serviço para publicar mensagens no RabbitMQ
 */
@Service
@Slf4j
public class MessagePublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.routingkey.consulta}")
    private String consultaRoutingKey;

    @Value("${rabbitmq.routingkey.exame}")
    private String exameRoutingKey;

    public MessagePublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Publica mensagem de nova consulta
     */
    public void publicarConsulta(ConsultaMessage message) {
        log.info("Publicando mensagem de consulta: {}", message);
        rabbitTemplate.convertAndSend(exchange, consultaRoutingKey, message);
        log.info("Mensagem de consulta publicada com sucesso");
    }

    /**
     * Publica mensagem de novo exame
     */
    public void publicarExame(ExameMessage message) {
        log.info("Publicando mensagem de exame: {}", message);
        rabbitTemplate.convertAndSend(exchange, exameRoutingKey, message);
        log.info("Mensagem de exame publicada com sucesso");
    }

    /**
     * DTO para mensagem de consulta
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConsultaMessage {
        private Long consultaId;
        private String cpfPaciente;
        private String nomePaciente;
        private LocalDateTime horario;
        private String especialidadeMedico;
        private String acao; // CRIAR, ATUALIZAR, CANCELAR
    }

    /**
     * DTO para mensagem de exame
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExameMessage {
        private Long exameId;
        private String cpfPaciente;
        private String nomePaciente;
        private LocalDateTime horario;
        private String tipoExame;
        private String acao; // CRIAR, ATUALIZAR, CANCELAR
    }
}
