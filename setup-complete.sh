#!/bin/bash

# ============================================================
# Script de Setup Completo do Sistema Hospitalar
# ============================================================
# Este script automatiza TODA a configuração do sistema:
#   1. Verifica pré-requisitos
#   2. Sobe a infraestrutura (MySQL, RabbitMQ, Keycloak)
#   3. Aguarda serviços ficarem prontos
#   4. Importa configuração do Keycloak automaticamente
#   5. Faz build de todos os microsserviços
#   6. Sobe todos os microsserviços
#
# Uso: ./setup-complete.sh
# ============================================================

set -e

# Cores para output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
CYAN='\033[0;36m'
BOLD='\033[1m'
NC='\033[0m' # No Color

# Banner
echo -e "${CYAN}${BOLD}"
echo "============================================================"
echo "    🏥 SISTEMA DE GERENCIAMENTO HOSPITALAR"
echo "    Setup Completo Automatizado"
echo "============================================================"
echo -e "${NC}"

# ============================================
# PASSO 1: Verificar Pré-requisitos
# ============================================

echo -e "${BLUE}${BOLD}[1/7] Verificando pré-requisitos...${NC}"
echo ""

# Verificar Docker
echo -n "  Verificando Docker... "
if ! command -v docker &> /dev/null; then
    echo -e "${RED}✗ NÃO ENCONTRADO${NC}"
    echo -e "${RED}Por favor, instale o Docker: https://www.docker.com/get-started${NC}"
    exit 1
fi
if ! docker info > /dev/null 2>&1; then
    echo -e "${RED}✗ NÃO ESTÁ RODANDO${NC}"
    echo -e "${RED}Por favor, inicie o Docker Desktop e tente novamente.${NC}"
    exit 1
fi
echo -e "${GREEN}✓${NC}"

# Verificar Docker Compose
echo -n "  Verificando Docker Compose... "
if ! command -v docker-compose &> /dev/null; then
    echo -e "${RED}✗ NÃO ENCONTRADO${NC}"
    echo -e "${RED}Por favor, instale o Docker Compose: https://docs.docker.com/compose/install/${NC}"
    exit 1
fi
echo -e "${GREEN}✓${NC}"

# Verificar Java
echo -n "  Verificando Java 21... "
if ! command -v java &> /dev/null; then
    echo -e "${YELLOW}⚠ NÃO ENCONTRADO${NC}"
    echo -e "${YELLOW}  Java não é necessário para rodar via Docker, mas é recomendado para desenvolvimento.${NC}"
else
    JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d'.' -f1)
    if [ "$JAVA_VERSION" -ge 21 ]; then
        echo -e "${GREEN}✓ (versão $JAVA_VERSION)${NC}"
    else
        echo -e "${YELLOW}⚠ versão $JAVA_VERSION (recomendado 21+)${NC}"
    fi
fi

# Verificar Maven
echo -n "  Verificando Maven... "
if ! command -v mvn &> /dev/null; then
    echo -e "${YELLOW}⚠ NÃO ENCONTRADO${NC}"
    echo -e "${YELLOW}  Maven não é necessário para rodar via Docker.${NC}"
else
    echo -e "${GREEN}✓${NC}"
fi

# Verificar curl
echo -n "  Verificando curl... "
if ! command -v curl &> /dev/null; then
    echo -e "${RED}✗ NÃO ENCONTRADO${NC}"
    echo -e "${RED}  curl é necessário para configurar o Keycloak.${NC}"
    exit 1
fi
echo -e "${GREEN}✓${NC}"

echo ""
echo -e "${GREEN}${BOLD}Todos os pré-requisitos verificados!${NC}"
echo ""

# ============================================
# PASSO 2: Limpar Ambiente Anterior (se existir)
# ============================================

echo -e "${BLUE}${BOLD}[2/7] Limpando ambiente anterior (se existir)...${NC}"
echo ""

docker-compose -f docker-compose-complete.yml down 2>/dev/null || true
docker-compose down 2>/dev/null || true

echo -e "${GREEN}✓ Ambiente limpo${NC}"
echo ""

# ============================================
# PASSO 3: Subir Infraestrutura
# ============================================

echo -e "${BLUE}${BOLD}[3/7] Iniciando infraestrutura (MySQL, RabbitMQ, Keycloak)...${NC}"
echo -e "${YELLOW}Isso pode levar alguns minutos na primeira execução...${NC}"
echo ""

# Subir apenas infraestrutura primeiro
docker-compose up -d mysql-agendamento mysql-clinica mysql-centro-cirurgico mysql-keycloak rabbitmq keycloak

echo ""
echo -e "${BLUE}Aguardando serviços ficarem prontos...${NC}"
echo ""

# Aguardar MySQL
echo -n "  MySQL Agendamento... "
timeout 90 bash -c 'until docker exec mysql-agendamento mysqladmin ping -h localhost -u root -proot --silent 2>/dev/null; do sleep 2; done' && echo -e "${GREEN}✓${NC}" || { echo -e "${RED}✗ TIMEOUT${NC}"; exit 1; }

echo -n "  MySQL Clínica... "
timeout 90 bash -c 'until docker exec mysql-clinica mysqladmin ping -h localhost -u root -proot --silent 2>/dev/null; do sleep 2; done' && echo -e "${GREEN}✓${NC}" || { echo -e "${RED}✗ TIMEOUT${NC}"; exit 1; }

echo -n "  MySQL Centro Cirúrgico... "
timeout 90 bash -c 'until docker exec mysql-centro-cirurgico mysqladmin ping -h localhost -u root -proot --silent 2>/dev/null; do sleep 2; done' && echo -e "${GREEN}✓${NC}" || { echo -e "${RED}✗ TIMEOUT${NC}"; exit 1; }

echo -n "  MySQL Keycloak... "
timeout 90 bash -c 'until docker exec mysql-keycloak mysqladmin ping -h localhost -u root -proot --silent 2>/dev/null; do sleep 2; done' && echo -e "${GREEN}✓${NC}" || { echo -e "${RED}✗ TIMEOUT${NC}"; exit 1; }

echo -n "  RabbitMQ... "
timeout 90 bash -c 'until docker exec rabbitmq rabbitmq-diagnostics ping --silent 2>/dev/null; do sleep 2; done' && echo -e "${GREEN}✓${NC}" || { echo -e "${RED}✗ TIMEOUT${NC}"; exit 1; }

echo -n "  Keycloak... "
timeout 120 bash -c 'until curl -s http://localhost:8090/health/ready 2>/dev/null | grep -q "UP"; do sleep 3; done' && echo -e "${GREEN}✓${NC}" || { echo -e "${RED}✗ TIMEOUT${NC}"; exit 1; }

echo ""
echo -e "${GREEN}${BOLD}Infraestrutura iniciada com sucesso!${NC}"
echo ""

# ============================================
# PASSO 4: Configurar Keycloak
# ============================================

echo -e "${BLUE}${BOLD}[4/7] Configurando Keycloak...${NC}"
echo ""

# Fazer login no Keycloak Admin CLI
echo "  Obtendo token de administrador..."
ADMIN_TOKEN=$(curl -s -X POST http://localhost:8090/realms/master/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "username=admin" \
  -d "password=admin" \
  -d "grant_type=password" \
  -d "client_id=admin-cli" | grep -o '"access_token":"[^"]*' | cut -d'"' -f4)

if [ -z "$ADMIN_TOKEN" ]; then
    echo -e "${RED}✗ Falha ao obter token de admin${NC}"
    exit 1
fi
echo -e "${GREEN}  ✓ Token obtido${NC}"

# Verificar se realm já existe
echo "  Verificando se realm 'hospital' já existe..."
REALM_EXISTS=$(curl -s -o /dev/null -w "%{http_code}" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  http://localhost:8090/admin/realms/hospital)

if [ "$REALM_EXISTS" = "200" ]; then
    echo -e "${YELLOW}  ⚠ Realm 'hospital' já existe. Pulando importação.${NC}"
else
    # Importar realm do arquivo JSON
    echo "  Importando realm 'hospital' do arquivo keycloak/realm-hospital.json..."

    if [ ! -f "keycloak/realm-hospital.json" ]; then
        echo -e "${RED}✗ Arquivo keycloak/realm-hospital.json não encontrado!${NC}"
        exit 1
    fi

    IMPORT_RESULT=$(curl -s -o /dev/null -w "%{http_code}" \
      -X POST http://localhost:8090/admin/realms \
      -H "Authorization: Bearer $ADMIN_TOKEN" \
      -H "Content-Type: application/json" \
      -d @keycloak/realm-hospital.json)

    if [ "$IMPORT_RESULT" = "201" ]; then
        echo -e "${GREEN}  ✓ Realm 'hospital' importado com sucesso!${NC}"
    else
        echo -e "${RED}✗ Falha ao importar realm (HTTP $IMPORT_RESULT)${NC}"
        echo -e "${YELLOW}  Por favor, importe manualmente: keycloak/realm-hospital.json${NC}"
        echo -e "${YELLOW}  Ou consulte: docs/KEYCLOAK_SETUP.md${NC}"
    fi
fi

echo ""
echo -e "${GREEN}${BOLD}Keycloak configurado!${NC}"
echo ""

# ============================================
# PASSO 5: Build dos Microsserviços
# ============================================

echo -e "${BLUE}${BOLD}[5/7] Fazendo build dos microsserviços...${NC}"
echo -e "${YELLOW}Isso pode levar vários minutos...${NC}"
echo ""

docker-compose -f docker-compose-complete.yml build

echo ""
echo -e "${GREEN}${BOLD}Build concluído!${NC}"
echo ""

# ============================================
# PASSO 6: Subir Microsserviços
# ============================================

echo -e "${BLUE}${BOLD}[6/7] Iniciando microsserviços...${NC}"
echo -e "${YELLOW}Aguarde enquanto os serviços são iniciados...${NC}"
echo ""

docker-compose -f docker-compose-complete.yml up -d agendamento-service clinica-service centro-cirurgico-service gateway-service

echo ""
echo "  Aguardando microsserviços ficarem prontos..."
echo ""

sleep 10

echo -n "  Agendamento Service (8081)... "
timeout 120 bash -c 'until curl -s http://localhost:8081/actuator/health 2>/dev/null | grep -q "UP"; do sleep 3; done' && echo -e "${GREEN}✓${NC}" || echo -e "${YELLOW}⚠ (pode estar iniciando)${NC}"

echo -n "  Clínica Service (8082)... "
timeout 120 bash -c 'until curl -s http://localhost:8082/actuator/health 2>/dev/null | grep -q "UP"; do sleep 3; done' && echo -e "${GREEN}✓${NC}" || echo -e "${YELLOW}⚠ (pode estar iniciando)${NC}"

echo -n "  Centro Cirúrgico Service (8083)... "
timeout 120 bash -c 'until curl -s http://localhost:8083/actuator/health 2>/dev/null | grep -q "UP"; do sleep 3; done' && echo -e "${GREEN}✓${NC}" || echo -e "${YELLOW}⚠ (pode estar iniciando)${NC}"

echo -n "  Gateway Service (8080)... "
timeout 120 bash -c 'until curl -s http://localhost:8080/actuator/health 2>/dev/null | grep -q "UP"; do sleep 3; done' && echo -e "${GREEN}✓${NC}" || echo -e "${YELLOW}⚠ (pode estar iniciando)${NC}"

echo ""
echo -e "${GREEN}${BOLD}Microsserviços iniciados!${NC}"
echo ""

# ============================================
# PASSO 7: Verificação Final
# ============================================

echo -e "${BLUE}${BOLD}[7/7] Verificando status final...${NC}"
echo ""

docker-compose -f docker-compose-complete.yml ps

echo ""
echo -e "${GREEN}${BOLD}============================================================${NC}"
echo -e "${GREEN}${BOLD}   ✓✓✓ SISTEMA INICIADO COM SUCESSO! ✓✓✓${NC}"
echo -e "${GREEN}${BOLD}============================================================${NC}"
echo ""

# ============================================
# Informações Importantes
# ============================================

echo -e "${CYAN}${BOLD}📋 INFORMAÇÕES IMPORTANTES:${NC}"
echo ""

echo -e "${BOLD}🌐 URLs dos Serviços:${NC}"
echo ""
echo "  API Gateway (Ponto de Entrada):  http://localhost:8080"
echo "  Agendamento Service:             http://localhost:8081"
echo "  Clínica Service:                 http://localhost:8082"
echo "  Centro Cirúrgico Service:        http://localhost:8083"
echo ""

echo -e "${BOLD}📚 Documentação Swagger:${NC}"
echo ""
echo "  Agendamento:      http://localhost:8081/swagger-ui.html"
echo "  Clínica:          http://localhost:8082/swagger-ui.html"
echo "  Centro Cirúrgico: http://localhost:8083/swagger-ui.html"
echo ""

echo -e "${BOLD}🔐 Keycloak Admin:${NC}"
echo ""
echo "  URL:    http://localhost:8090"
echo "  Login:  admin / admin"
echo "  Realm:  hospital"
echo ""

echo -e "${BOLD}🐰 RabbitMQ Management:${NC}"
echo ""
echo "  URL:    http://localhost:15672"
echo "  Login:  guest / guest"
echo ""

echo -e "${BOLD}🗄️ MySQL Bancos de Dados:${NC}"
echo ""
echo "  Agendamento:      localhost:3307 (root/root)"
echo "  Clínica:          localhost:3308 (root/root)"
echo "  Centro Cirúrgico: localhost:3309 (root/root)"
echo "  Keycloak:         localhost:3310 (root/root)"
echo ""

echo -e "${BOLD}👥 Usuários de Teste (Keycloak):${NC}"
echo ""
echo "  paciente1  / senha123  [USUARIO]"
echo "  paciente2  / senha123  [USUARIO]"
echo "  medico1    / senha123  [MEDICO, USUARIO]"
echo "  medico2    / senha123  [MEDICO, USUARIO]"
echo "  admin1     / admin123  [ADMIN, MEDICO, USUARIO]"
echo ""

echo -e "${YELLOW}${BOLD}📖 Documentação Adicional:${NC}"
echo ""
echo "  README.md              - Visão geral do projeto"
echo "  INICIO_RAPIDO.md       - Guia rápido de início"
echo "  docs/COMO_EXECUTAR.md  - Guia completo de execução"
echo "  docs/KEYCLOAK_SETUP.md - Configuração do Keycloak"
echo "  SITEMAP.md             - Mapa completo do projeto"
echo ""

echo -e "${CYAN}${BOLD}🧪 Testando o Sistema:${NC}"
echo ""
echo "  1. Obter token de acesso:"
echo '     curl -X POST http://localhost:8090/realms/hospital/protocol/openid-connect/token \'
echo '       -d "client_id=agendamento-service" \'
echo '       -d "client_secret=agendamento-secret-key-2024" \'
echo '       -d "grant_type=password" \'
echo '       -d "username=paciente1" \'
echo '       -d "password=senha123"'
echo ""
echo "  2. Usar o access_token nos headers das requisições:"
echo '     curl -H "Authorization: Bearer SEU_TOKEN_AQUI" \'
echo '       http://localhost:8080/api/cadastro/consulta'
echo ""

echo -e "${YELLOW}${BOLD}⚙️  Comandos Úteis:${NC}"
echo ""
echo "  Parar tudo:        docker-compose -f docker-compose-complete.yml down"
echo "  Ver logs:          docker-compose -f docker-compose-complete.yml logs -f"
echo "  Ver logs de um:    docker-compose -f docker-compose-complete.yml logs -f agendamento-service"
echo "  Reiniciar tudo:    docker-compose -f docker-compose-complete.yml restart"
echo "  Status:            docker-compose -f docker-compose-complete.yml ps"
echo ""

echo -e "${GREEN}${BOLD}Bom trabalho! 🚀${NC}"
echo ""
