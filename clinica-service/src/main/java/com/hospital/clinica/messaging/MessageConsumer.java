package com.hospital.clinica.messaging;

import com.hospital.clinica.dto.CriarConsultaDTO;
import com.hospital.clinica.service.ClinicaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Consumer de mensagens do RabbitMQ
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class MessageConsumer {

    private final ClinicaService clinicaService;

    /**
     * Consome mensagens da fila de consultas
     */
    @RabbitListener(queues = "${rabbitmq.queue.consulta}")
    public void receberConsulta(CriarConsultaDTO message) {
        log.info("Mensagem recebida da fila de consultas: {}", message);
        
        try {
            Long consultaId = clinicaService.criarConsulta(message);
            log.info("Consulta criada com sucesso. ID: {}", consultaId);
            
            // Aqui poderia enviar mensagem de volta confirmando a criação
        } catch (Exception e) {
            log.error("Erro ao processar mensagem de consulta", e);
        }
    }
}
