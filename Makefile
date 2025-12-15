# Makefile para Sistema de Gerenciamento Hospitalar
# Uso: make [comando]

.PHONY: help infra-up infra-down infra-clean build-all run-agendamento run-clinica run-centro run-gateway logs clean test

# Cores para output
BLUE := \033[0;34m
GREEN := \033[0;32m
YELLOW := \033[1;33m
NC := \033[0m

# Comando padrão
.DEFAULT_GOAL := help

## help: Mostra esta ajuda
help:
	@echo "$(BLUE)Sistema de Gerenciamento Hospitalar$(NC)"
	@echo "===================================="
	@echo ""
	@echo "Comandos disponíveis:"
	@echo ""
	@grep -E '^## [a-zA-Z_-]+:.*$$' $(MAKEFILE_LIST) | awk 'BEGIN {FS = "## |:"}; {printf "  $(GREEN)%-20s$(NC) %s\n", $$2, $$3}'
	@echo ""

## infra-up: Inicia a infraestrutura (MySQL, RabbitMQ, Keycloak)
infra-up:
	@echo "$(BLUE)Iniciando infraestrutura...$(NC)"
	@./start-infrastructure.sh

## infra-down: Para a infraestrutura (preserva dados)
infra-down:
	@echo "$(YELLOW)Parando infraestrutura...$(NC)"
	@docker-compose down
	@echo "$(GREEN)Infraestrutura parada!$(NC)"

## infra-clean: Para a infraestrutura e remove TODOS os dados
infra-clean:
	@echo "$(YELLOW)⚠️  ATENÇÃO: Isso vai APAGAR todos os dados!$(NC)"
	@./stop-infrastructure.sh --clean

## build-all: Compila todos os microsserviços
build-all:
	@echo "$(BLUE)Compilando todos os serviços...$(NC)"
	@./build-all.sh --skip-tests

## build-agendamento: Compila apenas serviço de agendamento
build-agendamento:
	@echo "$(BLUE)Compilando agendamento-service...$(NC)"
	@cd agendamento-service && mvn clean install -DskipTests

## build-clinica: Compila apenas serviço de clínica
build-clinica:
	@echo "$(BLUE)Compilando clinica-service...$(NC)"
	@cd clinica-service && mvn clean install -DskipTests

## build-centro: Compila apenas serviço de centro cirúrgico
build-centro:
	@echo "$(BLUE)Compilando centro-cirurgico-service...$(NC)"
	@cd centro-cirurgico-service && mvn clean install -DskipTests

## build-gateway: Compila apenas API Gateway
build-gateway:
	@echo "$(BLUE)Compilando gateway-service...$(NC)"
	@cd gateway-service && mvn clean install -DskipTests

## run-agendamento: Executa serviço de agendamento (porta 8081)
run-agendamento:
	@echo "$(BLUE)Executando agendamento-service na porta 8081...$(NC)"
	@cd agendamento-service && mvn spring-boot:run

## run-clinica: Executa serviço de clínica (porta 8082)
run-clinica:
	@echo "$(BLUE)Executando clinica-service na porta 8082...$(NC)"
	@cd clinica-service && mvn spring-boot:run

## run-centro: Executa serviço de centro cirúrgico (porta 8083)
run-centro:
	@echo "$(BLUE)Executando centro-cirurgico-service na porta 8083...$(NC)"
	@cd centro-cirurgico-service && mvn spring-boot:run

## run-gateway: Executa API Gateway (porta 8080)
run-gateway:
	@echo "$(BLUE)Executando gateway-service na porta 8080...$(NC)"
	@cd gateway-service && mvn spring-boot:run

## logs: Mostra logs da infraestrutura
logs:
	@docker-compose logs -f

## logs-rabbitmq: Mostra logs do RabbitMQ
logs-rabbitmq:
	@docker-compose logs -f rabbitmq

## logs-keycloak: Mostra logs do Keycloak
logs-keycloak:
	@docker-compose logs -f keycloak

## status: Mostra status dos containers
status:
	@echo "$(BLUE)Status da infraestrutura:$(NC)"
	@docker-compose ps
	@echo ""
	@echo "$(BLUE)Serviços disponíveis:$(NC)"
	@echo "  RabbitMQ:  http://localhost:15672 (guest/guest)"
	@echo "  Keycloak:  http://localhost:8090 (admin/admin)"
	@echo "  Swagger Agendamento:     http://localhost:8081/swagger-ui.html"
	@echo "  Swagger Clínica:         http://localhost:8082/swagger-ui.html"
	@echo "  Swagger Centro Cirúrgico: http://localhost:8083/swagger-ui.html"

## clean: Remove arquivos compilados de todos os serviços
clean:
	@echo "$(YELLOW)Limpando arquivos compilados...$(NC)"
	@cd agendamento-service && mvn clean
	@cd clinica-service && mvn clean
	@cd centro-cirurgico-service && mvn clean
	@cd gateway-service && mvn clean
	@echo "$(GREEN)Limpeza concluída!$(NC)"

## test: Executa testes de todos os serviços
test:
	@echo "$(BLUE)Executando testes...$(NC)"
	@cd agendamento-service && mvn test
	@cd clinica-service && mvn test
	@cd centro-cirurgico-service && mvn test
	@cd gateway-service && mvn test
	@echo "$(GREEN)Testes concluídos!$(NC)"

## docker-build: Constrói imagens Docker de todos os serviços
docker-build:
	@echo "$(BLUE)Construindo imagens Docker...$(NC)"
	@docker build -t hospital/agendamento-service:latest ./agendamento-service
	@docker build -t hospital/clinica-service:latest ./clinica-service
	@docker build -t hospital/centro-cirurgico-service:latest ./centro-cirurgico-service
	@docker build -t hospital/gateway-service:latest ./gateway-service
	@echo "$(GREEN)Imagens construídas!$(NC)"

## docker-up: Inicia TUDO via Docker (infra + serviços)
docker-up:
	@echo "$(BLUE)Iniciando tudo via Docker...$(NC)"
	@docker-compose -f docker-compose-full.yml up -d
	@echo "$(GREEN)Sistema iniciado!$(NC)"

## docker-down: Para tudo (infra + serviços)
docker-down:
	@echo "$(YELLOW)Parando tudo...$(NC)"
	@docker-compose -f docker-compose-full.yml down
	@echo "$(GREEN)Sistema parado!$(NC)"

## setup: Setup completo (infra + build)
setup: infra-up build-all
	@echo "$(GREEN)Setup completo!$(NC)"
	@echo ""
	@echo "Próximo passo: Configurar Keycloak"
	@echo "Consulte: GUIA_6_HORAS.md"
