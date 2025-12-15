package com.hospital.agendamento.exception;

/**
 * Exceção para conflitos de agendamento
 */
public class AgendamentoConflictException extends RuntimeException {
    public AgendamentoConflictException(String message) {
        super(message);
    }
}

/**
 * Exceção para recursos não encontrados
 */
class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

/**
 * Exceção para validações de negócio
 */
class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}

/**
 * Exceção para erros de comunicação entre serviços
 */
class ServiceCommunicationException extends RuntimeException {
    public ServiceCommunicationException(String message) {
        super(message);
    }
    
    public ServiceCommunicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
