#!/bin/bash

# Script para parar a infraestrutura do Sistema Hospitalar
# Uso: ./stop-infrastructure.sh [--clean]

set -e

# Cores
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

echo "🏥 Sistema de Gerenciamento Hospitalar"
echo "========================================"
echo ""

# Verificar flag --clean
CLEAN_VOLUMES=false
if [ "$1" == "--clean" ]; then
    CLEAN_VOLUMES=true
    echo -e "${YELLOW}⚠️  MODO CLEAN: Todos os dados serão APAGADOS!${NC}"
    echo ""
    read -p "Tem certeza? (digite 'sim' para confirmar): " confirmacao
    if [ "$confirmacao" != "sim" ]; then
        echo "Operação cancelada."
        exit 0
    fi
    echo ""
fi

echo -e "${BLUE}Parando containers...${NC}"

if [ "$CLEAN_VOLUMES" = true ]; then
    docker-compose down -v
    echo ""
    echo -e "${GREEN}✓ Containers parados e volumes removidos${NC}"
else
    docker-compose down
    echo ""
    echo -e "${GREEN}✓ Containers parados (dados preservados)${NC}"
fi

echo ""
echo -e "${BLUE}Status final:${NC}"
docker-compose ps
echo ""

if [ "$CLEAN_VOLUMES" = true ]; then
    echo -e "${YELLOW}========================================${NC}"
    echo -e "${YELLOW}⚠️  ATENÇÃO:${NC}"
    echo -e "${YELLOW}========================================${NC}"
    echo ""
    echo "Todos os dados foram APAGADOS!"
    echo "Para reiniciar com dados limpos:"
    echo "  ./start-infrastructure.sh"
    echo ""
else
    echo -e "${GREEN}========================================${NC}"
    echo -e "${GREEN}Infraestrutura parada com sucesso!${NC}"
    echo -e "${GREEN}========================================${NC}"
    echo ""
    echo "Os dados foram preservados."
    echo "Para reiniciar:"
    echo "  ./start-infrastructure.sh"
    echo ""
    echo "Para remover todos os dados:"
    echo "  ./stop-infrastructure.sh --clean"
    echo ""
fi
