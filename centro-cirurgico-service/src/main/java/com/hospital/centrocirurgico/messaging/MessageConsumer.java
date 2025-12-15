package com.hospital.centrocirurgico.messaging;

import com.hospital.centrocirurgico.dto.CriarExameDTO;
import com.hospital.centrocirurgico.service.CentroCirurgicoService;
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

    private final CentroCirurgicoService centroCirurgicoService;

    /**
     * Consome mensagens da fila de exames
     */
    @RabbitListener(queues = "${rabbitmq.queue.exame}")
    public void receberExame(CriarExameDTO message) {
        log.info("Mensagem recebida da fila de exames: {}", message);
        
        try {
            Long procedimentoId = centroCirurgicoService.criarProcedimento(message);
            log.info("Procedimento criado com sucesso. ID: {}", procedimentoId);
        } catch (Exception e) {
            log.error("Erro ao processar mensagem de exame", e);
        }
    }
}
