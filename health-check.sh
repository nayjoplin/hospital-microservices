#!/bin/bash

# Script para verificar a saúde de todos os serviços
# Uso: ./health-check.sh

# Cores
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

echo "🏥 Health Check - Sistema de Gerenciamento Hospitalar"
echo "====================================================="
echo ""

check_service() {
    local name=$1
    local url=$2
    local expected_code=${3:-200}
    
    echo -n "Verificando $name... "
    
    response=$(curl -s -o /dev/null -w "%{http_code}" "$url" 2>/dev/null)
    
    if [ "$response" == "$expected_code" ] || [ "$response" == "200" ] || [ "$response" == "302" ]; then
        echo -e "${GREEN}✓ OK${NC} (HTTP $response)"
        return 0
    else
        echo -e "${RED}✗ FALHOU${NC} (HTTP $response)"
        return 1
    fi
}

check_docker_container() {
    local container=$1
    local name=$2
    
    echo -n "Container $name... "
    
    if docker ps --format '{{.Names}}' | grep -q "^${container}$"; then
        status=$(docker inspect --format='{{.State.Health.Status}}' "$container" 2>/dev/null || echo "running")
        if [ "$status" == "healthy" ] || [ "$status" == "running" ]; then
            echo -e "${GREEN}✓ Running${NC}"
            return 0
        else
            echo -e "${YELLOW}⚠ Unhealthy${NC}"
            return 1
        fi
    else
        echo -e "${RED}✗ Not Running${NC}"
        return 1
    fi
}

# Contador
total=0
passed=0

echo -e "${BLUE}== INFRAESTRUTURA ==${NC}"
echo ""

# Verificar containers Docker
check_docker_container "mysql-agendamento" "MySQL Agendamento" && ((passed++))
((total++))

check_docker_container "mysql-clinica" "MySQL Clínica" && ((passed++))
((total++))

check_docker_container "mysql-centro-cirurgico" "MySQL Centro Cirúrgico" && ((passed++))
((total++))

check_docker_container "mysql-keycloak" "MySQL Keycloak" && ((passed++))
((total++))

check_docker_container "rabbitmq" "RabbitMQ" && ((passed++))
((total++))

check_docker_container "keycloak" "Keycloak" && ((passed++))
((total++))

echo ""
echo -e "${BLUE}== SERVIÇOS WEB ==${NC}"
echo ""

# Verificar RabbitMQ Management
check_service "RabbitMQ Management" "http://localhost:15672" && ((passed++))
((total++))

# Verificar Keycloak
check_service "Keycloak" "http://localhost:8090" && ((passed++))
((total++))

# Verificar microsserviços
check_service "Agendamento Service" "http://localhost:8081/swagger-ui.html" && ((passed++))
((total++))

check_service "Clínica Service" "http://localhost:8082/swagger-ui.html" && ((passed++))
((total++))

check_service "Centro Cirúrgico Service" "http://localhost:8083/swagger-ui.html" && ((passed++))
((total++))

check_service "API Gateway" "http://localhost:8080" && ((passed++))
((total++))

echo ""
echo "====================================================="
echo -e "Resultado: ${GREEN}$passed${NC}/${total} serviços funcionando"
echo "====================================================="
echo ""

if [ $passed -eq $total ]; then
    echo -e "${GREEN}✓ Todos os serviços estão funcionando!${NC}"
    echo ""
    echo "URLs disponíveis:"
    echo "  🐰 RabbitMQ:  http://localhost:15672"
    echo "  🔐 Keycloak:  http://localhost:8090"
    echo "  📄 Swagger Agendamento:     http://localhost:8081/swagger-ui.html"
    echo "  📄 Swagger Clínica:         http://localhost:8082/swagger-ui.html"
    echo "  📄 Swagger Centro Cirúrgico: http://localhost:8083/swagger-ui.html"
    echo "  🌐 API Gateway:              http://localhost:8080"
    echo ""
    exit 0
elif [ $passed -gt 0 ]; then
    echo -e "${YELLOW}⚠ Alguns serviços não estão funcionando${NC}"
    echo ""
    echo "Verifique os logs:"
    echo "  docker-compose logs [nome-do-servico]"
    echo ""
    exit 1
else
    echo -e "${RED}✗ Nenhum serviço está funcionando${NC}"
    echo ""
    echo "Inicie a infraestrutura:"
    echo "  ./start-infrastructure.sh"
    echo ""
    echo "Inicie os microsserviços:"
    echo "  make run-agendamento (em um terminal)"
    echo "  make run-clinica (em outro terminal)"
    echo "  make run-centro (em outro terminal)"
    echo "  make run-gateway (em outro terminal)"
    echo ""
    exit 1
fi
