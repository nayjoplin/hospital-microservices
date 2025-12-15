#!/bin/bash

# Script para iniciar a infraestrutura do Sistema Hospitalar
# Uso: ./start-infrastructure.sh

set -e

echo "🏥 Sistema de Gerenciamento Hospitalar"
echo "========================================"
echo ""

# Cores
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Verificar se Docker está rodando
echo -e "${BLUE}Verificando Docker...${NC}"
if ! docker info > /dev/null 2>&1; then
    echo -e "${RED}❌ Docker não está rodando!${NC}"
    echo "Por favor, inicie o Docker Desktop e tente novamente."
    exit 1
fi
echo -e "${GREEN}✓ Docker está rodando${NC}"
echo ""

# Verificar se docker-compose existe
if ! command -v docker-compose &> /dev/null; then
    echo -e "${RED}❌ docker-compose não encontrado!${NC}"
    echo "Por favor, instale o Docker Compose."
    exit 1
fi

# Parar containers existentes (se houver)
echo -e "${YELLOW}Parando containers existentes (se houver)...${NC}"
docker-compose down 2>/dev/null || true
echo ""

# Iniciar infraestrutura
echo -e "${BLUE}Iniciando infraestrutura...${NC}"
echo "Isso pode levar alguns minutos na primeira vez..."
echo ""

docker-compose up -d

echo ""
echo -e "${BLUE}Aguardando serviços ficarem prontos...${NC}"
echo "Isso pode levar até 2 minutos..."
echo ""

# Aguardar MySQL
echo -n "MySQL Agendamento... "
timeout 60 bash -c 'until docker exec mysql-agendamento mysqladmin ping -h localhost -u root -proot --silent 2>/dev/null; do sleep 1; done' && echo -e "${GREEN}✓${NC}" || echo -e "${RED}✗${NC}"

echo -n "MySQL Clínica... "
timeout 60 bash -c 'until docker exec mysql-clinica mysqladmin ping -h localhost -u root -proot --silent 2>/dev/null; do sleep 1; done' && echo -e "${GREEN}✓${NC}" || echo -e "${RED}✗${NC}"

echo -n "MySQL Centro Cirúrgico... "
timeout 60 bash -c 'until docker exec mysql-centro-cirurgico mysqladmin ping -h localhost -u root -proot --silent 2>/dev/null; do sleep 1; done' && echo -e "${GREEN}✓${NC}" || echo -e "${RED}✗${NC}"

echo -n "MySQL Keycloak... "
timeout 60 bash -c 'until docker exec mysql-keycloak mysqladmin ping -h localhost -u root -proot --silent 2>/dev/null; do sleep 1; done' && echo -e "${GREEN}✓${NC}" || echo -e "${RED}✗${NC}"

# Aguardar RabbitMQ
echo -n "RabbitMQ... "
timeout 60 bash -c 'until docker exec rabbitmq rabbitmq-diagnostics ping --silent 2>/dev/null; do sleep 1; done' && echo -e "${GREEN}✓${NC}" || echo -e "${RED}✗${NC}"

# Aguardar Keycloak
echo -n "Keycloak... "
timeout 90 bash -c 'until curl -s http://localhost:8090/health/ready | grep -q "UP"; do sleep 2; done' && echo -e "${GREEN}✓${NC}" || echo -e "${RED}✗${NC}"

echo ""
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}✓ Infraestrutura iniciada com sucesso!${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""

# Mostrar status
echo -e "${BLUE}Status dos containers:${NC}"
docker-compose ps
echo ""

# Mostrar URLs
echo -e "${BLUE}Serviços disponíveis:${NC}"
echo ""
echo "  🐰 RabbitMQ Management:"
echo "     http://localhost:15672"
echo "     Login: guest / guest"
echo ""
echo "  🔐 Keycloak Admin:"
echo "     http://localhost:8090"
echo "     Login: admin / admin"
echo ""
echo "  🗄️  MySQL Bancos:"
echo "     Agendamento:      localhost:3307"
echo "     Clínica:          localhost:3308"
echo "     Centro Cirúrgico: localhost:3309"
echo "     Keycloak:         localhost:3310"
echo "     Credenciais: root / root"
echo ""

echo -e "${YELLOW}========================================${NC}"
echo -e "${YELLOW}Próximos passos:${NC}"
echo -e "${YELLOW}========================================${NC}"
echo ""
echo "1. Configure o Keycloak:"
echo "   - Criar realm: hospital"
echo "   - Criar roles: USUARIO, MEDICO, ADMIN"
echo "   - Criar clients para cada serviço"
echo "   - Criar usuários de teste"
echo ""
echo "2. Execute os serviços:"
echo "   Terminal 1: cd agendamento-service && mvn spring-boot:run"
echo "   Terminal 2: cd clinica-service && mvn spring-boot:run"
echo "   Terminal 3: cd centro-cirurgico-service && mvn spring-boot:run"
echo "   Terminal 4: cd gateway-service && mvn spring-boot:run"
echo ""
echo "Para mais detalhes, consulte: GUIA_6_HORAS.md"
echo ""
