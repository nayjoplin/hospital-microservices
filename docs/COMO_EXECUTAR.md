# 🚀 Como Executar o Sistema Hospitalar

Guia completo para executar o Sistema de Gerenciamento Hospitalar em diferentes ambientes.

## 📋 Índice

- [Opção 1: Setup Automático (MAIS FÁCIL)](#opção-1-setup-automático-mais-fácil)
- [Opção 2: Docker Compose Completo](#opção-2-docker-compose-completo)
- [Opção 3: Infraestrutura Docker + Serviços Locais](#opção-3-infraestrutura-docker--serviços-locais)
- [Opção 4: Tudo Local (Sem Docker)](#opção-4-tudo-local-sem-docker)
- [Testando o Sistema](#testando-o-sistema)
- [Parando o Sistema](#parando-o-sistema)
- [Troubleshooting](#troubleshooting)

---

## ✅ Pré-requisitos

### Obrigatórios (para qualquer opção):

- ✅ **Docker** 20.10+ e **Docker Compose** 2.0+
- ✅ **Git** (para clonar o repositório)

### Opcionais (apenas para desenvolvimento local):

- ⚙️ **Java 21** (JDK)
- ⚙️ **Maven 3.8+**

### Verificar instalação:

```bash
# Docker
docker --version
docker-compose --version

# Java (opcional)
java -version

# Maven (opcional)
mvn -version
```

---

## 🎯 Opção 1: Setup Automático (MAIS FÁCIL)

**👍 RECOMENDADO para primeira execução!**

Este script automatiza TUDO:
- ✅ Verifica pré-requisitos
- ✅ Sobe infraestrutura (MySQL, RabbitMQ, Keycloak)
- ✅ Configura Keycloak automaticamente
- ✅ Faz build de todos os microsserviços
- ✅ Sobe todos os microsserviços

### Passo a Passo:

```bash
# 1. Clone o repositório
git clone https://github.com/seu-usuario/hospital-microservices.git
cd hospital-microservices

# 2. Execute o script de setup
chmod +x setup-complete.sh
./setup-complete.sh
```

**Aguarde 5-10 minutos** (primeira execução faz download de todas as imagens)

### O que o script faz:

1. ✅ Verifica Docker, curl e outros pré-requisitos
2. ✅ Limpa ambiente anterior (se existir)
3. ✅ Sobe MySQL (4 instâncias), RabbitMQ e Keycloak
4. ✅ Aguarda todos ficarem prontos
5. ✅ Importa configuração do Keycloak (realm, users, clients)
6. ✅ Faz build dos 4 microsserviços
7. ✅ Sobe todos os microsserviços
8. ✅ Mostra URLs e credenciais

### Resultado:

Ao final, você terá:

- 🌐 **API Gateway**: http://localhost:8080
- 📅 **Agendamento**: http://localhost:8081
- 🏥 **Clínica**: http://localhost:8082
- ⚕️ **Centro Cirúrgico**: http://localhost:8083
- 🔐 **Keycloak**: http://localhost:8090
- 🐰 **RabbitMQ**: http://localhost:15672

---

## 🐳 Opção 2: Docker Compose Completo

**👍 Recomendado para produção local ou testes completos**

### Passo a Passo:

```bash
# 1. Clone o repositório
git clone https://github.com/seu-usuario/hospital-microservices.git
cd hospital-microservices

# 2. Suba TUDO com um comando
docker-compose -f docker-compose-complete.yml up -d --build

# 3. Acompanhe os logs (opcional)
docker-compose -f docker-compose-complete.yml logs -f
```

### Aguardar serviços ficarem prontos:

```bash
# Verificar status
docker-compose -f docker-compose-complete.yml ps

# Ver logs de um serviço específico
docker-compose -f docker-compose-complete.yml logs -f agendamento-service
```

### Configurar Keycloak:

**IMPORTANTE**: Você precisa configurar o Keycloak manualmente ou importar o realm:

#### Opção A: Importação Automática

```bash
# Acesse o Keycloak
http://localhost:8090

# Login: admin / admin

# Importe o arquivo: keycloak/realm-hospital.json
# (veja guia completo em docs/KEYCLOAK_SETUP.md)
```

#### Opção B: Script de Importação

```bash
# Aguarde o Keycloak estar pronto
sleep 60

# Execute o script de importação
./scripts/import-keycloak-realm.sh
```

---

## 🔧 Opção 3: Infraestrutura Docker + Serviços Locais

**👍 Recomendado para desenvolvimento**

Sobe apenas a infraestrutura no Docker e executa os microsserviços localmente (com hot reload).

### Passo 1: Subir Infraestrutura

```bash
# Apenas MySQL, RabbitMQ e Keycloak
docker-compose up -d

# Ou use o script auxiliar
./start-infrastructure.sh
```

### Passo 2: Configurar Keycloak

Siga o guia: [docs/KEYCLOAK_SETUP.md](KEYCLOAK_SETUP.md)

### Passo 3: Executar Microsserviços Localmente

Abra **4 terminais** (um para cada serviço):

#### Terminal 1 - Agendamento Service:

```bash
cd agendamento-service
mvn spring-boot:run
```

#### Terminal 2 - Clínica Service:

```bash
cd clinica-service
mvn spring-boot:run
```

#### Terminal 3 - Centro Cirúrgico Service:

```bash
cd centro-cirurgico-service
mvn spring-boot:run
```

#### Terminal 4 - Gateway Service:

```bash
cd gateway-service
mvn spring-boot:run
```

### Vantagens desta opção:

- ✅ Hot reload automático (mudanças no código refletem instantaneamente)
- ✅ Debug mais fácil
- ✅ Logs diretos no terminal
- ✅ Usa menos recursos que Docker

---

## 💻 Opção 4: Tudo Local (Sem Docker)

**⚠️ Não recomendado** - Requer instalar MySQL, RabbitMQ e Keycloak manualmente.

<details>
<summary>Clique para expandir</summary>

### Pré-requisitos:

- MySQL 8.0 instalado e rodando
- RabbitMQ instalado e rodando
- Keycloak 23.0 instalado e rodando
- Java 21
- Maven 3.8+

### Passo 1: Configurar Bancos de Dados

```sql
-- MySQL - Criar bancos
CREATE DATABASE agendamento_db;
CREATE DATABASE clinica_db;
CREATE DATABASE centro_cirurgico_db;
CREATE DATABASE keycloak;
```

### Passo 2: Configurar RabbitMQ

```bash
# Acessar: http://localhost:15672
# Login: guest / guest
```

### Passo 3: Configurar Keycloak

```bash
# Iniciar Keycloak
cd keycloak-23.0
./bin/kc.sh start-dev --http-port=8090
```

Importar realm: `keycloak/realm-hospital.json`

### Passo 4: Ajustar Configurações

Edite os arquivos `application.properties` de cada serviço para apontar para `localhost` ao invés de hostnames Docker.

### Passo 5: Executar Microsserviços

```bash
# Terminal 1
cd agendamento-service && mvn spring-boot:run

# Terminal 2
cd clinica-service && mvn spring-boot:run

# Terminal 3
cd centro-cirurgico-service && mvn spring-boot:run

# Terminal 4
cd gateway-service && mvn spring-boot:run
```

</details>

---

## 🧪 Testando o Sistema

### 1. Verificar Saúde dos Serviços

```bash
# Gateway
curl http://localhost:8080/actuator/health

# Agendamento
curl http://localhost:8081/actuator/health

# Clínica
curl http://localhost:8082/actuator/health

# Centro Cirúrgico
curl http://localhost:8083/actuator/health
```

**Resposta esperada**: `{"status":"UP"}`

### 2. Obter Token de Autenticação

```bash
# Paciente (USUARIO)
curl -X POST http://localhost:8090/realms/hospital/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=agendamento-service" \
  -d "client_secret=agendamento-secret-key-2024" \
  -d "grant_type=password" \
  -d "username=paciente1" \
  -d "password=senha123"

# Médico (MEDICO)
curl -X POST http://localhost:8090/realms/hospital/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=agendamento-service" \
  -d "client_secret=agendamento-secret-key-2024" \
  -d "grant_type=password" \
  -d "username=medico1" \
  -d "password=senha123"

# Admin (ADMIN)
curl -X POST http://localhost:8090/realms/hospital/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=agendamento-service" \
  -d "client_secret=agendamento-secret-key-2024" \
  -d "grant_type=password" \
  -d "username=admin1" \
  -d "password=admin123"
```

**Copie o `access_token` da resposta** para usar nas próximas requisições.

### 3. Testar Endpoints (via Gateway)

```bash
# Substitua SEU_TOKEN pelo access_token obtido acima

# Cadastrar Consulta (via Gateway)
curl -X POST http://localhost:8080/api/cadastro/consulta \
  -H "Authorization: Bearer SEU_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "cpfPaciente": "123.456.789-00",
    "nomePaciente": "Maria Silva",
    "horario": "2024-12-20T10:00:00",
    "especialidadeMedico": "Cardiologia"
  }'

# Buscar Consultas por CPF
curl -X GET http://localhost:8080/api/pesquisa/consultas/cpf/123.456.789-00 \
  -H "Authorization: Bearer SEU_TOKEN"
```

### 4. Acessar Swagger (Documentação Interativa)

- **Agendamento**: http://localhost:8081/swagger-ui.html
- **Clínica**: http://localhost:8082/swagger-ui.html
- **Centro Cirúrgico**: http://localhost:8083/swagger-ui.html

**Clique em "Authorize"** e cole seu token JWT.

---

## 🛑 Parando o Sistema

### Opção Completa (Docker Compose):

```bash
# Parar e remover containers
docker-compose -f docker-compose-complete.yml down

# Parar, remover containers E volumes (CUIDADO: apaga dados)
docker-compose -f docker-compose-complete.yml down -v
```

### Apenas Infraestrutura:

```bash
docker-compose down

# Ou use o script
./stop-infrastructure.sh
```

### Serviços Locais:

Pressione `Ctrl+C` em cada terminal onde os serviços estão rodando.

---

## 🔍 Troubleshooting

### Problema: "Port already in use"

**Causa**: Porta já está sendo usada por outro processo.

**Solução**:

```bash
# Descobrir qual processo está usando a porta (exemplo: 8080)
lsof -i :8080

# Matar o processo
kill -9 PID_DO_PROCESSO
```

### Problema: "Connection refused" ao acessar serviços

**Causa**: Serviços ainda estão inicializando.

**Solução**: Aguarde 1-2 minutos e tente novamente.

```bash
# Ver logs de um serviço
docker-compose -f docker-compose-complete.yml logs -f agendamento-service
```

### Problema: "Unauthorized" (401)

**Causa**: Token inválido, expirado ou não fornecido.

**Solução**:
1. Obtenha um novo token
2. Verifique se está usando o header correto: `Authorization: Bearer SEU_TOKEN`
3. Verifique se o Keycloak está configurado corretamente

### Problema: "Forbidden" (403)

**Causa**: Usuário não tem a role necessária.

**Solução**:
1. Verifique as roles do usuário no Keycloak
2. Use um usuário com a role apropriada:
   - `USUARIO`: endpoints básicos
   - `MEDICO`: atendimento clínico
   - `ADMIN`: endpoints administrativos

### Problema: Keycloak não inicia

**Causa**: MySQL do Keycloak não está pronto.

**Solução**:

```bash
# Verificar MySQL do Keycloak
docker logs mysql-keycloak

# Reiniciar Keycloak
docker-compose restart keycloak

# Ver logs
docker logs -f keycloak
```

### Problema: RabbitMQ não está criando filas

**Causa**: Configuração incorreta ou delay na inicialização.

**Solução**:

```bash
# Acessar RabbitMQ Management
http://localhost:15672
# Login: guest / guest

# Verificar se as filas existem:
# - consulta.queue
# - exame.queue

# Reiniciar RabbitMQ
docker-compose restart rabbitmq
```

### Problema: Build falha com "package does not exist"

**Causa**: Dependências do Maven não foram baixadas ou há erro no pom.xml.

**Solução**:

```bash
# Limpar e reinstalar dependências
cd agendamento-service
mvn clean install -U

# Ou force update
mvn clean install -DskipTests -U
```

### Problema: MySQL "Access denied"

**Causa**: Credenciais incorretas ou usuário não tem permissões.

**Solução**:

```bash
# Resetar volumes do MySQL
docker-compose down -v
docker-compose up -d
```

### Ver Logs de Todos os Serviços:

```bash
# Docker Compose
docker-compose -f docker-compose-complete.yml logs -f

# Apenas um serviço
docker-compose -f docker-compose-complete.yml logs -f agendamento-service
```

---

## 📚 Recursos Adicionais

- **README.md**: Visão geral do projeto
- **INICIO_RAPIDO.md**: Guia rápido (5 minutos)
- **docs/KEYCLOAK_SETUP.md**: Configuração detalhada do Keycloak
- **SITEMAP.md**: Mapa completo do projeto
- **docs/CONTRIBUTING.md**: Como contribuir

---

## ✅ Checklist de Execução

Após seguir este guia, verifique:

- [ ] Todos os containers estão rodando (`docker ps`)
- [ ] Todos os serviços retornam `UP` no `/actuator/health`
- [ ] Keycloak está acessível em http://localhost:8090
- [ ] RabbitMQ está acessível em http://localhost:15672
- [ ] Realm `hospital` foi importado no Keycloak
- [ ] Consegui obter um token JWT
- [ ] Consegui fazer uma requisição autenticada
- [ ] Swagger está acessível e funcionando

**Sistema funcionando perfeitamente!** 🎉

---

## 🎯 Próximos Passos

1. Explore a **documentação Swagger** de cada serviço
2. Teste os diferentes **endpoints com diferentes roles**
3. Consulte o **SITEMAP.md** para entender a arquitetura
4. Leia o **README.md** para mais detalhes do projeto

**Bom desenvolvimento!** 🚀
