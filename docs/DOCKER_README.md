# 🐳 Guia Docker - Sistema de Gerenciamento Hospitalar

Este guia explica como usar o Docker com o sistema.

## 📋 Pré-requisitos

- Docker Desktop instalado e rodando
- Docker Compose instalado (geralmente vem com Docker Desktop)
- 4GB RAM livre mínimo
- 10GB espaço em disco livre

## 🚀 Modo 1: Apenas Infraestrutura (Recomendado para Desenvolvimento)

Use este modo quando quiser desenvolver os serviços localmente com Maven.

### Iniciar

```bash
# Usando o script
./start-infrastructure.sh

# OU usando docker-compose diretamente
docker-compose up -d

# OU usando Makefile
make infra-up
```

Isso vai iniciar:
- ✅ 4 instâncias MySQL (Agendamento, Clínica, Centro Cirúrgico, Keycloak)
- ✅ RabbitMQ
- ✅ Keycloak

### Parar

```bash
# Preservando dados
./stop-infrastructure.sh
# OU
docker-compose down
# OU
make infra-down

# Removendo TODOS os dados
./stop-infrastructure.sh --clean
# OU
docker-compose down -v
# OU
make infra-clean
```

### Verificar Status

```bash
docker-compose ps

# OU verificar logs
docker-compose logs -f

# OU logs de um serviço específico
docker-compose logs -f rabbitmq
docker-compose logs -f keycloak
```

---

## 🐋 Modo 2: Tudo via Docker (Produção)

Use este modo quando quiser rodar TUDO em containers.

### Build das Imagens

```bash
# Build de todos os serviços
make docker-build

# OU manualmente
docker build -t hospital/agendamento-service:latest ./agendamento-service
docker build -t hospital/clinica-service:latest ./clinica-service
docker build -t hospital/centro-cirurgico-service:latest ./centro-cirurgico-service
docker build -t hospital/gateway-service:latest ./gateway-service
```

### Iniciar Tudo

```bash
# Editar docker-compose-full.yml e descomentar os serviços
# Depois:

docker-compose -f docker-compose-full.yml up -d

# OU usando Makefile
make docker-up
```

### Parar Tudo

```bash
docker-compose -f docker-compose-full.yml down

# OU
make docker-down
```

---

## 🔍 Comandos Úteis

### Ver Containers Rodando
```bash
docker ps

# OU ver todos (incluindo parados)
docker ps -a
```

### Ver Logs
```bash
# Logs de todos
docker-compose logs

# Seguir logs em tempo real
docker-compose logs -f

# Logs de um serviço específico
docker-compose logs -f mysql-agendamento
docker-compose logs -f rabbitmq

# Últimas 100 linhas
docker-compose logs --tail=100
```

### Reiniciar um Serviço
```bash
docker-compose restart mysql-agendamento
docker-compose restart rabbitmq
```

### Entrar em um Container
```bash
# MySQL
docker exec -it mysql-agendamento bash
docker exec -it mysql-agendamento mysql -uroot -proot agendamento_db

# RabbitMQ
docker exec -it rabbitmq bash
```

### Ver Uso de Recursos
```bash
docker stats
```

### Limpar Tudo
```bash
# Parar e remover containers, networks, volumes e imagens
docker-compose down -v --rmi all

# Limpar sistema (cuidado!)
docker system prune -a --volumes
```

---

## 🗄️ Bancos de Dados

### Portas dos Bancos MySQL:

| Banco | Porta Host | Porta Container | Credenciais |
|-------|------------|-----------------|-------------|
| Agendamento | 3307 | 3306 | root/root |
| Clínica | 3308 | 3306 | root/root |
| Centro Cirúrgico | 3309 | 3306 | root/root |
| Keycloak | 3310 | 3306 | root/root |

### Conectar ao MySQL:

```bash
# Via Docker
docker exec -it mysql-agendamento mysql -uroot -proot agendamento_db

# Via cliente MySQL local
mysql -h 127.0.0.1 -P 3307 -uroot -proot agendamento_db
```

### Backup do Banco:

```bash
# Backup
docker exec mysql-agendamento mysqldump -uroot -proot agendamento_db > backup.sql

# Restore
docker exec -i mysql-agendamento mysql -uroot -proot agendamento_db < backup.sql
```

---

## 🐰 RabbitMQ

### Acessar Management UI:
- URL: http://localhost:15672
- Login: `guest`
- Senha: `guest`

### Ver Filas via CLI:
```bash
docker exec rabbitmq rabbitmqctl list_queues
docker exec rabbitmq rabbitmqctl list_exchanges
```

---

## 🔐 Keycloak

### Acessar Admin Console:
- URL: http://localhost:8090
- Login: `admin`
- Senha: `admin`

### Exportar Configuração:
```bash
docker exec keycloak /opt/keycloak/bin/kc.sh export --dir /tmp/export
docker cp keycloak:/tmp/export ./keycloak-export
```

---

## 🏥 Microsserviços (Quando rodando em Docker)

### Portas:

| Serviço | Porta | Swagger |
|---------|-------|---------|
| Agendamento | 8081 | http://localhost:8081/swagger-ui.html |
| Clínica | 8082 | http://localhost:8082/swagger-ui.html |
| Centro Cirúrgico | 8083 | http://localhost:8083/swagger-ui.html |
| Gateway | 8080 | - |

### Ver Logs de um Serviço:
```bash
docker logs -f agendamento-service
docker logs -f clinica-service
docker logs -f centro-cirurgico-service
docker logs -f gateway-service
```

---

## 🔧 Troubleshooting

### Problema: Porta já está em uso
```bash
# Ver o que está usando a porta
lsof -i :8081  # Mac/Linux
netstat -ano | findstr :8081  # Windows

# Matar processo
kill -9 PID  # Mac/Linux
taskkill /PID PID /F  # Windows

# OU mudar porta no docker-compose.yml
```

### Problema: Container não inicia
```bash
# Ver logs
docker-compose logs [nome-do-servico]

# Ver status detalhado
docker inspect [nome-do-container]

# Reiniciar
docker-compose restart [nome-do-servico]
```

### Problema: Banco de dados não conecta
```bash
# Verificar se container está rodando
docker ps | grep mysql

# Verificar health check
docker inspect --format='{{.State.Health.Status}}' mysql-agendamento

# Entrar no container e testar
docker exec -it mysql-agendamento mysql -uroot -proot -e "SELECT 1"
```

### Problema: Sem espaço em disco
```bash
# Limpar imagens não utilizadas
docker image prune -a

# Limpar volumes não utilizados
docker volume prune

# Limpar tudo
docker system prune -a --volumes
```

### Problema: RabbitMQ não consome mensagens
```bash
# Ver filas
docker exec rabbitmq rabbitmqctl list_queues

# Reiniciar RabbitMQ
docker-compose restart rabbitmq

# Ver logs
docker-compose logs -f rabbitmq
```

---

## 📊 Monitoramento

### Health Check Manual:
```bash
./health-check.sh
```

### Verificar Memória e CPU:
```bash
docker stats

# Apenas containers do hospital
docker stats $(docker ps --filter name=hospital -q)
```

### Limites de Recursos (Adicionar no docker-compose.yml):
```yaml
services:
  agendamento-service:
    # ...
    deploy:
      resources:
        limits:
          cpus: '0.5'
          memory: 512M
        reservations:
          cpus: '0.25'
          memory: 256M
```

---

## 🔒 Segurança

### Não usar em Produção:
- ❌ Senhas padrão (root/root, guest/guest, admin/admin)
- ❌ Portas expostas diretamente
- ❌ Containers rodando como root

### Recomendações para Produção:
- ✅ Usar secrets do Docker
- ✅ Usar variáveis de ambiente
- ✅ Usar network overlay
- ✅ Usar usuários não-root nos containers
- ✅ Habilitar TLS
- ✅ Usar Docker Swarm ou Kubernetes

---

## 📦 Docker Compose Profiles (Opcional)

Você pode criar profiles para diferentes ambientes:

```yaml
# docker-compose.yml
services:
  agendamento-service:
    profiles: ["dev", "prod"]
    # ...

  monitoring:
    profiles: ["dev"]
    image: prom/prometheus
    # ...
```

Usar:
```bash
# Apenas perfil dev
docker-compose --profile dev up

# Apenas perfil prod
docker-compose --profile prod up
```

---

## 🎯 Resumo de Comandos Rápidos

```bash
# Iniciar tudo (apenas infra)
make infra-up

# Parar tudo (preservar dados)
make infra-down

# Limpar tudo (APAGA DADOS!)
make infra-clean

# Ver status
make status

# Ver logs
make logs

# Health check
./health-check.sh

# Build completo
make docker-build

# Iniciar tudo em Docker
make docker-up
```

---

**Para mais informações, consulte:**
- README.md (documentação principal)
- GUIA_6_HORAS.md (passo a passo completo)
- docker-compose.yml (configuração da infraestrutura)
- docker-compose-full.yml (configuração completa)
