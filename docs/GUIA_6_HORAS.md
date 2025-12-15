# ⏱️ GUIA COMPLETO: DO ZERO AO FUNCIONANDO EM 6 HORAS
## Sistema de Gerenciamento Hospitalar - Microsserviços

---

## 📋 PRÉ-REQUISITOS (Instalar antes de começar)

```bash
✅ Java 21 JDK          → https://www.oracle.com/java/technologies/downloads/
✅ Maven 3.8+           → https://maven.apache.org/download.cgi
✅ Docker Desktop       → https://www.docker.com/get-started
✅ Git                  → https://git-scm.com/downloads
✅ IDE (IntelliJ/VSCode)→ https://www.jetbrains.com/idea/download/
```

**Verificar instalações:**
```bash
java -version      # Deve mostrar Java 21
mvn -version       # Deve mostrar Maven 3.8+
docker --version   # Deve mostrar Docker
```

---

## ⏰ CRONOGRAMA DE 6 HORAS

```
┌──────────────────────────────────────────────┐
│ HORA 1: Configuração Inicial (15:00-16:00)  │
│ ├─ Baixar arquivos                           │
│ ├─ Subir infraestrutura Docker              │
│ └─ Configurar Keycloak                       │
├──────────────────────────────────────────────┤
│ HORA 2: Serviço de Agendamento (16:00-17:00)│
│ ├─ Compilar projeto                          │
│ ├─ Executar serviço                          │
│ └─ Testar endpoints                          │
├──────────────────────────────────────────────┤
│ HORA 3: Serviço de Clínica (17:00-18:00)    │
│ ├─ Compilar projeto                          │
│ ├─ Executar serviço                          │
│ └─ Testar atendimento                        │
├──────────────────────────────────────────────┤
│ HORA 4: Centro Cirúrgico (18:00-19:00)      │
│ ├─ Compilar projeto                          │
│ ├─ Executar serviço                          │
│ └─ Testar procedimentos                      │
├──────────────────────────────────────────────┤
│ HORA 5: API Gateway (19:00-20:00)           │
│ ├─ Compilar e executar                       │
│ └─ Testar fluxo completo                     │
├──────────────────────────────────────────────┤
│ HORA 6: Testes Integrados (20:00-21:00)     │
│ ├─ Fluxo completo de consulta                │
│ ├─ Fluxo completo de exame                   │
│ └─ Validar todos os requisitos               │
└──────────────────────────────────────────────┘
```

---

# 🕐 HORA 1: CONFIGURAÇÃO INICIAL (60 min)

## ⏱️ [00:00 - 00:10] Baixar e Organizar Arquivos

### Passo 1.1: Baixar o projeto
```bash
# Criar diretório de trabalho
mkdir ~/projetos
cd ~/projetos

# Baixar os arquivos (você já tem a pasta hospital-microservices)
# Copiar para ~/projetos/hospital-microservices
```

### Passo 1.2: Verificar estrutura
```bash
cd hospital-microservices
ls -la

# Deve ver:
# - agendamento-service/
# - clinica-service/
# - centro-cirurgico-service/
# - gateway-service/
# - docker-compose.yml
# - README.md
```

---

## ⏱️ [00:10 - 00:30] Subir Infraestrutura Docker

### Passo 1.3: Iniciar containers
```bash
# No diretório hospital-microservices/
docker-compose up -d

# Aguardar 2-3 minutos para todos subirem
```

### Passo 1.4: Verificar containers
```bash
docker-compose ps

# Deve mostrar 6 containers rodando:
# ✅ mysql-agendamento
# ✅ mysql-clinica
# ✅ mysql-centro-cirurgico
# ✅ mysql-keycloak
# ✅ rabbitmq
# ✅ keycloak
```

### Passo 1.5: Verificar logs (se houver erro)
```bash
docker-compose logs mysql-agendamento
docker-compose logs keycloak
docker-compose logs rabbitmq
```

---

## ⏱️ [00:30 - 01:00] Configurar Keycloak

### Passo 1.6: Acessar Keycloak
1. Abrir navegador: http://localhost:8090
2. Login: `admin` / `admin`
3. Aguardar carregar completamente

### Passo 1.7: Criar Realm
1. Clicar em **"Create Realm"** (canto superior esquerdo)
2. Name: `hospital`
3. Clicar em **"Create"**

### Passo 1.8: Criar Roles
1. Menu lateral: **"Realm roles"**
2. Clicar em **"Create role"**
3. Criar 3 roles (uma por vez):
   ```
   Role name: USUARIO
   Description: Usuário comum
   [Create]
   
   Role name: MEDICO
   Description: Médico
   [Create]
   
   Role name: ADMIN
   Description: Administrador
   [Create]
   ```

### Passo 1.9: Criar Client - agendamento-service
1. Menu lateral: **"Clients"** → **"Create client"**
2. Preencher:
   ```
   Client type: OpenID Connect
   Client ID: agendamento-service
   ```
3. Clicar **"Next"**
4. Habilitar:
   ```
   ☑ Client authentication: ON
   ☑ Authorization: OFF
   ☑ Standard flow: ON
   ☑ Direct access grants: ON
   ```
5. Clicar **"Next"** e depois **"Save"**
6. Na aba **"Credentials"**: copiar o **Client Secret** (você vai usar depois)

### Passo 1.10: Criar outros Clients
Repetir Passo 1.9 para:
- `clinica-service`
- `centro-cirurgico-service`
- `gateway-service`

### Passo 1.11: Criar Usuários de Teste

**Usuário 1 - paciente1:**
1. Menu: **"Users"** → **"Create new user"**
2. Username: `paciente1`
3. Email: `paciente1@test.com`
4. Email verified: `ON`
5. Clicar **"Create"**
6. Aba **"Credentials"**:
   - Password: `senha123`
   - Password confirmation: `senha123`
   - Temporary: `OFF`
   - Clicar **"Set password"**
7. Aba **"Role mapping"**:
   - Clicar **"Assign role"**
   - Marcar `USUARIO`
   - Clicar **"Assign"**

**Usuário 2 - medico1:**
Repetir processo acima com:
- Username: `medico1`
- Email: `medico1@test.com`
- Password: `senha123`
- Role: `MEDICO`

**Usuário 3 - admin1:**
Repetir processo acima com:
- Username: `admin1`
- Email: `admin1@test.com`
- Password: `senha123`
- Role: `ADMIN`

---

# ✅ CHECKPOINT HORA 1
```
Antes de prosseguir, verificar:
☑ Docker rodando (6 containers)
☑ Keycloak acessível em http://localhost:8090
☑ Realm "hospital" criado
☑ 3 Roles criadas
☑ 4 Clients criados
☑ 3 Usuários criados
```

---

# 🕑 HORA 2: SERVIÇO DE AGENDAMENTO (60 min)

## ⏱️ [01:00 - 01:20] Compilar e Executar

### Passo 2.1: Abrir terminal no serviço
```bash
cd ~/projetos/hospital-microservices/agendamento-service
```

### Passo 2.2: Compilar (primeira vez demora ~5 min)
```bash
mvn clean install -DskipTests

# Aguardar aparecer: BUILD SUCCESS
```

### Passo 2.3: Executar o serviço
```bash
mvn spring-boot:run

# Aguardar aparecer:
# "Started AgendamentoServiceApplication"
# "Tomcat started on port 8081"
```

**⚠️ NÃO FECHAR ESTE TERMINAL! Deixar rodando.**

---

## ⏱️ [01:20 - 01:40] Testar Endpoints

### Passo 2.4: Verificar Swagger
1. Abrir navegador: http://localhost:8081/swagger-ui.html
2. Deve carregar a documentação da API

### Passo 2.5: Obter Token JWT

Abrir novo terminal:
```bash
# Substituir SEU_CLIENT_SECRET pelo secret copiado do Keycloak
curl -X POST http://localhost:8090/realms/hospital/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "username=paciente1" \
  -d "password=senha123" \
  -d "grant_type=password" \
  -d "client_id=agendamento-service" \
  -d "client_secret=SEU_CLIENT_SECRET"

# Copiar o valor de "access_token" da resposta
```

**📝 SALVAR O TOKEN EM UM ARQUIVO token.txt PARA USO POSTERIOR**

### Passo 2.6: Cadastrar Primeira Consulta
```bash
# Substituir SEU_TOKEN pelo token obtido acima
curl -X POST http://localhost:8081/api/cadastro/consulta \
  -H "Authorization: Bearer SEU_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "Paciente": {
      "Nome": "João Silva",
      "CPF": "123.456.789-00",
      "idade": 35,
      "Sexo": "Masculino"
    },
    "Horario": "20/12/2024 14:00",
    "Medico": "Cardiologista"
  }'

# Deve retornar:
# {
#   "mensagem": "O medico de João Silva foi marcado para 20/12/2024 14:00",
#   "codigo": "Aguardando confirmação da clínica"
# }
```

### Passo 2.7: Buscar Consulta por CPF
```bash
curl -X GET "http://localhost:8081/api/pesquisa/consultas/cpf/123.456.789-00" \
  -H "Authorization: Bearer SEU_TOKEN"

# Deve retornar a consulta cadastrada
```

### Passo 2.8: Cadastrar Primeiro Exame
```bash
curl -X POST http://localhost:8081/api/cadastro/exame \
  -H "Authorization: Bearer SEU_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "Paciente": {
      "Nome": "Maria Santos",
      "CPF": "987.654.321-00",
      "idade": 28,
      "Sexo": "Feminino"
    },
    "Horario": "21/12/2024 09:00",
    "Exame": "Coleta de sangue"
  }'
```

---

## ⏱️ [01:40 - 02:00] Validar RabbitMQ

### Passo 2.9: Acessar RabbitMQ Management
1. Abrir navegador: http://localhost:15672
2. Login: `guest` / `guest`

### Passo 2.10: Verificar Filas
1. Clicar em **"Queues"**
2. Deve ver:
   - `consulta.queue` com 1 mensagem (Ready)
   - `exame.queue` com 1 mensagem (Ready)

**✅ Isso confirma que as mensagens foram enviadas!**

---

# ✅ CHECKPOINT HORA 2
```
☑ Agendamento compilado e rodando na porta 8081
☑ Swagger acessível
☑ Token JWT obtido
☑ Consulta cadastrada com sucesso
☑ Exame cadastrado com sucesso
☑ Mensagens no RabbitMQ
```

---

# 🕒 HORA 3: SERVIÇO DE CLÍNICA (60 min)

## ⏱️ [02:00 - 02:20] Compilar e Executar

### Passo 3.1: Abrir NOVO terminal
```bash
cd ~/projetos/hospital-microservices/clinica-service
```

### Passo 3.2: Compilar
```bash
mvn clean install -DskipTests

# Aguardar: BUILD SUCCESS
```

### Passo 3.3: Executar
```bash
mvn spring-boot:run

# Aguardar:
# "Started ClinicaServiceApplication"
# "Tomcat started on port 8082"
```

**⚠️ DEIXAR RODANDO! Abrir novo terminal para próximos comandos.**

---

## ⏱️ [02:20 - 02:35] Verificar Consumo de Mensagens

### Passo 3.4: Checar logs do serviço
No terminal onde o serviço está rodando, procurar por:
```
INFO ... MessageConsumer : Mensagem recebida da fila de consultas
INFO ... ClinicaService : Criando consulta para paciente: 123.456.789-00
INFO ... ClinicaService : Consulta criada com ID: 1
```

### Passo 3.5: Verificar RabbitMQ
1. Voltar ao RabbitMQ Management: http://localhost:15672
2. A fila `consulta.queue` deve estar vazia (Ready: 0)
3. Isso confirma que a mensagem foi consumida!

### Passo 3.6: Verificar banco de dados (opcional)
```bash
# Conectar ao MySQL da clínica
docker exec -it mysql-clinica mysql -uroot -proot clinica_db

# Verificar consulta
SELECT * FROM consultas_clinica;

# Verificar dados iniciais
SELECT * FROM medicos;
SELECT * FROM sintomas;
SELECT * FROM doencas;

# Sair
exit
```

---

## ⏱️ [02:35 - 03:00] Testar Atendimento

### Passo 3.7: Verificar Swagger
http://localhost:8082/swagger-ui.html

### Passo 3.8: Obter novo token (se expirou)
```bash
# Mesmo comando do Passo 2.5, mas com client_id=clinica-service
curl -X POST http://localhost:8090/realms/hospital/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "username=medico1" \
  -d "password=senha123" \
  -d "grant_type=password" \
  -d "client_id=clinica-service" \
  -d "client_secret=SEU_CLIENT_SECRET_CLINICA"
```

### Passo 3.9: Atender Consulta por Horário
```bash
curl -X POST http://localhost:8082/api/clinica/AtenderConsulta \
  -H "Authorization: Bearer TOKEN_MEDICO" \
  -H "Content-Type: application/json" \
  -d '{
    "CPF paciente": "123.456.789-00",
    "Horario": "20/12/2024 14:00",
    "Sintomas": ["febre", "tosse", "cansaço"]
  }'

# Resposta esperada:
# {
#   "mensagem": "Consulta atendida com sucesso",
#   "possiveisDiagnosticos": ["COVID-19", "Gripe"],
#   "tratamentoSugerido": "Tratamento sugerido: Antitérmico...",
#   "exameSolicitado": null
# }
```

### Passo 3.10: Testar Sintomas Emergenciais
```bash
curl -X POST http://localhost:8082/api/clinica/AtenderConsulta \
  -H "Authorization: Bearer TOKEN_MEDICO" \
  -H "Content-Type: application/json" \
  -d '{
    "CPF paciente": "123.456.789-00",
    "Horario": "20/12/2024 14:00",
    "Sintomas": ["sangramento agudo", "dores internas"]
  }'

# Deve retornar exame solicitado (Tomografia)
```

---

# ✅ CHECKPOINT HORA 3
```
☑ Clínica compilado e rodando na porta 8082
☑ Mensagens consumidas do RabbitMQ
☑ Consulta criada no banco clinica_db
☑ Atendimento de consulta funcionando
☑ Diagnóstico por sintomas funcionando
☑ Solicitação de exame de alta complexidade funcionando
```

---

# 🕓 HORA 4: CENTRO CIRÚRGICO (60 min)

## ⏱️ [03:00 - 03:20] Compilar e Executar

### Passo 4.1: Abrir NOVO terminal
```bash
cd ~/projetos/hospital-microservices/centro-cirurgico-service
```

### Passo 4.2: Compilar
```bash
mvn clean install -DskipTests
```

### Passo 4.3: Executar
```bash
mvn spring-boot:run

# Aguardar:
# "Started CentroCirurgicoServiceApplication"
# "Tomcat started on port 8083"
```

---

## ⏱️ [03:20 - 03:40] Verificar Consumo

### Passo 4.4: Checar logs
Procurar por:
```
INFO ... MessageConsumer : Mensagem recebida da fila de exames
INFO ... CentroCirurgicoService : Criando procedimento para paciente: 987.654.321-00
INFO ... CentroCirurgicoService : Procedimento criado com ID: 1
```

### Passo 4.5: Verificar RabbitMQ
- Fila `exame.queue` deve estar vazia

---

## ⏱️ [03:40 - 04:00] Testar Procedimentos

### Passo 4.6: Swagger
http://localhost:8083/swagger-ui.html

### Passo 4.7: Buscar Procedimentos por CPF
```bash
curl -X GET "http://localhost:8083/api/procedimentos/cpf/987.654.321-00" \
  -H "Authorization: Bearer SEU_TOKEN"

# Deve retornar o procedimento criado
```

### Passo 4.8: Marcar Procedimento com Horário
```bash
# Primeiro, anotar o ID do procedimento retornado acima

curl -X POST http://localhost:8083/api/procedimentos/marcar \
  -H "Authorization: Bearer SEU_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "CPF paciente": "987.654.321-00",
    "Codigo exame": "1",
    "Horario desejado": "22/12/2024 10:00"
  }'

# Resposta:
# {
#   "mensagem": "Procedimento marcado com sucesso",
#   "codigo": "1"
# }
```

### Passo 4.9: Testar Procedimento Emergencial
```bash
# Obter token de ADMIN
curl -X POST http://localhost:8090/realms/hospital/protocol/openid-connect/token \
  -d "username=admin1&password=senha123&grant_type=password" \
  -d "client_id=centro-cirurgico-service&client_secret=SEU_SECRET"

# Criar procedimento emergencial
curl -X POST http://localhost:8083/api/procedimentos/criar-solicitacao \
  -H "Authorization: Bearer TOKEN_ADMIN" \
  -H "Content-Type: application/json" \
  -d '{
    "cpfPaciente": "111.222.333-44",
    "nomePaciente": "Carlos Emergência",
    "tipoExame": "Tomografia",
    "prioridade": "EMERGENCIAL",
    "solicitadoPor": "Dr. Teste"
  }'
```

---

# ✅ CHECKPOINT HORA 4
```
☑ Centro Cirúrgico compilado e rodando na porta 8083
☑ Mensagens consumidas do RabbitMQ
☑ Procedimento criado no banco
☑ Marcação de procedimento funcionando
☑ Procedimento emergencial funcionando
```

---

# 🕔 HORA 5: API GATEWAY (60 min)

## ⏱️ [04:00 - 04:20] Compilar e Executar

### Passo 5.1: Abrir NOVO terminal
```bash
cd ~/projetos/hospital-microservices/gateway-service
```

### Passo 5.2: Compilar
```bash
mvn clean install -DskipTests
```

### Passo 5.3: Executar
```bash
mvn spring-boot:run

# Aguardar:
# "Started GatewayServiceApplication"
# "Netty started on port 8080"
```

---

## ⏱️ [04:20 - 05:00] Testar via Gateway

### Passo 5.4: Testar Roteamento - Agendamento
```bash
# Via Gateway (porta 8080) em vez de direto (8081)
curl -X POST http://localhost:8080/api/cadastro/consulta \
  -H "Authorization: Bearer SEU_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "Paciente": {
      "Nome": "Pedro Gateway",
      "CPF": "555.666.777-88",
      "idade": 40,
      "Sexo": "Masculino"
    },
    "Horario": "23/12/2024 16:00",
    "Medico": "Ortopedista"
  }'

# Deve funcionar igual!
```

### Passo 5.5: Testar Roteamento - Clínica
```bash
curl -X POST http://localhost:8080/api/clinica/AtenderConsulta \
  -H "Authorization: Bearer TOKEN_MEDICO" \
  -H "Content-Type: application/json" \
  -d '{
    "CPF paciente": "555.666.777-88",
    "Horario": "23/12/2024 16:00",
    "Sintomas": ["dor de cabeça"]
  }'
```

### Passo 5.6: Testar Roteamento - Centro Cirúrgico
```bash
curl -X GET "http://localhost:8080/api/procedimentos/cpf/987.654.321-00" \
  -H "Authorization: Bearer SEU_TOKEN"
```

---

# ✅ CHECKPOINT HORA 5
```
☑ Gateway compilado e rodando na porta 8080
☑ Roteamento funcionando para todos os serviços
☑ Autenticação centralizada funcionando
☑ Todos os 4 serviços rodando simultaneamente
```

---

# 🕕 HORA 6: TESTES INTEGRADOS (60 min)

## ⏱️ [05:00 - 05:30] Fluxo Completo: Consulta

### Teste 6.1: Fluxo End-to-End Completo

```bash
# 1. Cadastrar consulta (via Gateway)
curl -X POST http://localhost:8080/api/cadastro/consulta \
  -H "Authorization: Bearer TOKEN_PACIENTE" \
  -H "Content-Type: application/json" \
  -d '{
    "Paciente": {
      "Nome": "Ana Teste Final",
      "CPF": "999.888.777-66",
      "idade": 25,
      "Sexo": "Feminino"
    },
    "Horario": "24/12/2024 14:00",
    "Medico": "Pediatra"
  }'

# 2. Aguardar 5 segundos (mensageria processar)

# 3. Atender consulta (via Gateway, como médico)
curl -X POST http://localhost:8080/api/clinica/AtenderConsulta \
  -H "Authorization: Bearer TOKEN_MEDICO" \
  -H "Content-Type: application/json" \
  -d '{
    "CPF paciente": "999.888.777-66",
    "Horario": "24/12/2024 14:00",
    "Sintomas": ["febre", "tosse", "dor de cabeça"]
  }'

# 4. Verificar resposta com diagnóstico

# 5. Buscar consultas do paciente
curl -X GET "http://localhost:8080/api/pesquisa/consultas/cpf/999.888.777-66" \
  -H "Authorization: Bearer TOKEN_PACIENTE"
```

---

## ⏱️ [05:30 - 06:00] Validação Final

### Teste 6.2: Checklist de Requisitos

```bash
# ✅ Microsserviços rodando
http://localhost:8081/swagger-ui.html  # Agendamento
http://localhost:8082/swagger-ui.html  # Clínica
http://localhost:8083/swagger-ui.html  # Centro Cirúrgico
http://localhost:8080                   # Gateway

# ✅ Bancos de dados
docker exec -it mysql-agendamento mysql -uroot -proot -e "USE agendamento_db; SELECT COUNT(*) FROM consultas;"
docker exec -it mysql-clinica mysql -uroot -proot -e "USE clinica_db; SELECT COUNT(*) FROM consultas_clinica;"
docker exec -it mysql-centro-cirurgico mysql -uroot -proot -e "USE centro_cirurgico_db; SELECT COUNT(*) FROM procedimentos;"

# ✅ RabbitMQ
http://localhost:15672  # Verificar mensagens processadas

# ✅ Keycloak
http://localhost:8090   # Verificar usuários e roles
```

### Teste 6.3: Testar Conflitos

```bash
# Tentar cadastrar consulta no mesmo horário (deve dar 409)
curl -X POST http://localhost:8080/api/cadastro/consulta \
  -H "Authorization: Bearer TOKEN_PACIENTE" \
  -H "Content-Type: application/json" \
  -d '{
    "Paciente": {
      "Nome": "Ana Teste Final",
      "CPF": "999.888.777-66",
      "idade": 25,
      "Sexo": "Feminino"
    },
    "Horario": "24/12/2024 14:00",
    "Medico": "Pediatra"
  }'

# Deve retornar erro 409 Conflict
```

### Teste 6.4: Testar Roles

```bash
# Tentar cancelar como USUARIO (deve dar 403)
curl -X DELETE http://localhost:8080/api/admin/consultas/1 \
  -H "Authorization: Bearer TOKEN_PACIENTE"

# Deve retornar 403 Forbidden

# Cancelar como ADMIN (deve funcionar)
curl -X DELETE http://localhost:8080/api/admin/consultas/1 \
  -H "Authorization: Bearer TOKEN_ADMIN"

# Deve retornar 204 No Content
```

---

# ✅ CHECKPOINT FINAL - HORA 6

```
TUDO FUNCIONANDO! 🎉

☑ 4 microsserviços rodando
☑ 4 bancos MySQL operacionais
☑ RabbitMQ processando mensagens
☑ Keycloak autenticando
☑ API Gateway roteando
☑ Swagger documentando
☑ Fluxo completo testado
☑ Validações funcionando
☑ Roles funcionando
☑ 100% dos requisitos atendidos
```

---

## 🎯 COMANDOS ÚTEIS DURANTE O DESENVOLVIMENTO

### Ver logs de um serviço
```bash
# Logs do MySQL
docker-compose logs mysql-agendamento

# Logs do RabbitMQ
docker-compose logs rabbitmq

# Logs do Keycloak
docker-compose logs keycloak
```

### Reiniciar um container
```bash
docker-compose restart mysql-agendamento
docker-compose restart rabbitmq
```

### Parar tudo
```bash
docker-compose down
```

### Limpar tudo e recomeçar
```bash
docker-compose down -v  # Remove volumes (apaga dados!)
docker-compose up -d
```

### Ver status dos serviços Spring Boot
```bash
# No terminal de cada serviço, procurar por:
# "Started [Nome]Application in X seconds"
```

---

## 🐛 TROUBLESHOOTING

### Problema: Porta ocupada
```bash
# Ver o que está usando a porta
lsof -i :8081  # Linux/Mac
netstat -ano | findstr :8081  # Windows

# Matar processo
kill -9 PID  # Linux/Mac
taskkill /PID PID /F  # Windows
```

### Problema: Token expirado
```
Erro: 401 Unauthorized
Solução: Gerar novo token (tokens expiram em 5 min)
```

### Problema: Banco não conecta
```bash
# Verificar se container está rodando
docker ps | grep mysql

# Reiniciar container
docker-compose restart mysql-agendamento

# Ver logs
docker-compose logs mysql-agendamento
```

### Problema: RabbitMQ não consome mensagens
```bash
# Verificar logs do serviço consumidor
# Procurar por "MessageConsumer"

# Reiniciar RabbitMQ
docker-compose restart rabbitmq
```

---

## 📝 CHECKLIST COMPLETO DE 6 HORAS

```
HORA 1: Configuração Inicial
☐ Java 21 instalado
☐ Maven instalado
☐ Docker instalado
☐ Arquivos baixados
☐ docker-compose up -d executado
☐ Keycloak configurado
☐ Realm criado
☐ Roles criadas
☐ Clients criados
☐ Usuários criados

HORA 2: Agendamento
☐ Projeto compilado
☐ Serviço rodando na 8081
☐ Swagger acessível
☐ Token obtido
☐ Consulta cadastrada
☐ Exame cadastrado
☐ Mensagens no RabbitMQ

HORA 3: Clínica
☐ Projeto compilado
☐ Serviço rodando na 8082
☐ Mensagens consumidas
☐ Consulta atendida
☐ Diagnóstico funcionando
☐ Exame solicitado

HORA 4: Centro Cirúrgico
☐ Projeto compilado
☐ Serviço rodando na 8083
☐ Mensagens consumidas
☐ Procedimento marcado
☐ Emergencial testado

HORA 5: Gateway
☐ Projeto compilado
☐ Serviço rodando na 8080
☐ Roteamento funcionando
☐ Todos os serviços acessíveis via gateway

HORA 6: Testes
☐ Fluxo completo executado
☐ Conflitos validados
☐ Roles testadas
☐ Documentação revisada
☐ Sistema 100% funcional
```

---

## 🎉 PARABÉNS!

Se você seguiu todos os passos, agora tem um sistema completo de microsserviços funcionando!

**O que você construiu:**
- 4 microsserviços independentes
- 4 bancos de dados
- Sistema de mensageria
- Autenticação e autorização
- API Gateway
- Documentação Swagger
- Sistema completo de hospital

**Próximos passos:**
1. Explore o Swagger de cada serviço
2. Teste diferentes fluxos
3. Leia o código fonte
4. Customize conforme necessário
5. Adicione os extras (email, observabilidade, cache)

---

**🚀 BOM TRABALHO! SISTEMA COMPLETO EM 6 HORAS!**
