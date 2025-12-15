#!/bin/bash

# Script para compilar todos os microsserviços
# Uso: ./build-all.sh [--skip-tests]

set -e

# Cores
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

echo "🏥 Sistema de Gerenciamento Hospitalar"
echo "========================================"
echo "Compilando todos os microsserviços..."
echo ""

# Verificar se Maven está instalado
if ! command -v mvn &> /dev/null; then
    echo -e "${RED}❌ Maven não encontrado!${NC}"
    echo "Por favor, instale o Maven."
    exit 1
fi

# Verificar flag --skip-tests
MAVEN_ARGS="clean install"
if [ "$1" == "--skip-tests" ]; then
    MAVEN_ARGS="clean install -DskipTests"
    echo -e "${YELLOW}Pulando testes...${NC}"
    echo ""
fi

# Array de serviços
services=("agendamento-service" "clinica-service" "centro-cirurgico-service" "gateway-service")

# Contador
total=${#services[@]}
current=0
failed=0

# Compilar cada serviço
for service in "${services[@]}"; do
    current=$((current + 1))
    echo -e "${BLUE}[$current/$total] Compilando $service...${NC}"
    
    if [ -d "$service" ]; then
        cd "$service"
        
        if mvn $MAVEN_ARGS; then
            echo -e "${GREEN}✓ $service compilado com sucesso${NC}"
        else
            echo -e "${RED}✗ Erro ao compilar $service${NC}"
            failed=$((failed + 1))
        fi
        
        cd ..
    else
        echo -e "${RED}✗ Diretório $service não encontrado${NC}"
        failed=$((failed + 1))
    fi
    
    echo ""
done

# Resultado final
echo "========================================"
if [ $failed -eq 0 ]; then
    echo -e "${GREEN}✓ Todos os serviços compilados com sucesso!${NC}"
    echo "========================================"
    echo ""
    echo "Para executar os serviços:"
    echo "  Terminal 1: cd agendamento-service && mvn spring-boot:run"
    echo "  Terminal 2: cd clinica-service && mvn spring-boot:run"
    echo "  Terminal 3: cd centro-cirurgico-service && mvn spring-boot:run"
    echo "  Terminal 4: cd gateway-service && mvn spring-boot:run"
    echo ""
    exit 0
else
    echo -e "${RED}✗ $failed serviço(s) falharam na compilação${NC}"
    echo "========================================"
    exit 1
fi
