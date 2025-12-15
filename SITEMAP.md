# 🗺️ SITEMAP COMPLETO DO PROJETO
## Sistema de Gerenciamento Hospitalar - Microsserviços

```
hospital-microservices/
│
├── 📄 README.md                          # Documentação principal do projeto
├── 📄 PROJETO_COMPLETO.md                # Resumo técnico detalhado
├── 📄 INICIO_RAPIDO.md                   # Guia de início rápido (5 min)
├── 📄 docker-compose.yml                 # Configuração de toda infraestrutura
├── 📄 gerar_estrutura.sh                 # Script auxiliar de estrutura
│
├── 📁 docs/                              # Documentação
│   ├── 📄 DOCUMENTACAO_COMPLETA.docx    # Doc Word profissional (15+ páginas)
│   └── 📄 gerar_documentacao.py         # Script que gera a documentação
│
├── 📁 agendamento-service/               # ⭐ MICROSSERVIÇO 1 - Porta 8081
│   ├── 📄 pom.xml                       # Dependências Maven
│   │
│   └── src/main/
│       ├── java/com/hospital/agendamento/
│       │   │
│       │   ├── 📄 AgendamentoServiceApplication.java    # ⚡ CLASSE PRINCIPAL
│       │   │
│       │   ├── 📁 entity/                # 🗄️ ENTIDADES (Banco de Dados)
│       │   │   ├── 📄 Paciente.java     # Tabela: pacientes
│       │   │   ├── 📄 Consulta.java     # Tabela: consultas
│       │   │   └── 📄 Exame.java        # Tabela: exames
│       │   │
│       │   ├── 📁 repository/            # 🔍 ACESSO AO BANCO (JPA)
│       │   │   ├── 📄 PacienteRepository.java
│       │   │   ├── 📄 ConsultaRepository.java
│       │   │   └── 📄 ExameRepository.java
│       │   │
│       │   ├── 📁 service/               # 🧠 LÓGICA DE NEGÓCIO
│       │   │   ├── 📄 ConsultaService.java      # Lógica de consultas
│       │   │   └── 📄 ExameService.java         # Lógica de exames
│       │   │
│       │   ├── 📁 controller/            # 🌐 ENDPOINTS REST (APIs)
│       │   │   └── 📄 AgendamentoController.java
│       │   │       # POST /api/cadastro/consulta
│       │   │       # POST /api/cadastro/exame
│       │   │       # GET  /api/pesquisa/consultas/cpf/{cpf}
│       │   │       # GET  /api/pesquisa/exames/cpf/{cpf}
│       │   │       # DELETE /api/admin/consultas/{id}
│       │   │       # DELETE /api/admin/exames/{id}
│       │   │
│       │   ├── 📁 dto/                   # 📦 OBJETOS DE TRANSFERÊNCIA
│       │   │   ├── 📄 ConsultaRequestDTO.java   # Request de consulta
│       │   │   ├── 📄 ExameRequestDTO.java      # Request de exame
│       │   │   └── 📄 ResponseDTO.java          # Respostas padrão
│       │   │
│       │   ├── 📁 config/                # ⚙️ CONFIGURAÇÕES
│       │   │   ├── 📄 SecurityConfig.java       # Segurança + Keycloak
│       │   │   ├── 📄 RabbitMQConfig.java       # Filas + Exchange
│       │   │   ├── 📄 SwaggerConfig.java        # Documentação API
│       │   │   └── 📄 WebClientConfig.java      # HTTP Client
│       │   │
│       │   ├── 📁 exception/             # ⚠️ TRATAMENTO DE ERROS
│       │   │   ├── 📄 CustomExceptions.java
│       │   │   └── 📄 GlobalExceptionHandler.java
│       │   │
│       │   └── 📁 messaging/             # 📨 MENSAGERIA (RabbitMQ)
│       │       └── 📄 MessagePublisher.java     # Envia mensagens
│       │
│       └── resources/
│           └── 📄 application.properties # Configurações do serviço
│               # - Porta: 8081
│               # - Banco: agendamento_db (porta 3307)
│               # - RabbitMQ
│               # - Keycloak
│
├── 📁 clinica-service/                   # ⭐ MICROSSERVIÇO 2 - Porta 8082
│   ├── 📄 pom.xml
│   │
│   └── src/main/
│       ├── java/com/hospital/clinica/
│       │   │
│       │   ├── 📄 ClinicaServiceApplication.java    # ⚡ CLASSE PRINCIPAL
│       │   │
│       │   ├── 📁 entity/                # 🗄️ ENTIDADES
│       │   │   ├── 📄 Medico.java       # Tabela: medicos
│       │   │   ├── 📄 ConsultaClinica.java  # Tabela: consultas_clinica
│       │   │   └── 📄 Sintoma.java      # Tabelas: sintomas, doencas
│       │   │                             #          doenca_sintoma
│       │   │
│       │   ├── 📁 repository/            # 🔍 ACESSO AO BANCO
│       │   │   ├── 📄 MedicoRepository.java
│       │   │   ├── 📄 ConsultaClinicaRepository.java
│       │   │   └── 📄 SintomaRepository.java
│       │   │
│       │   ├── 📁 service/               # 🧠 LÓGICA DE NEGÓCIO
│       │   │   └── 📄 ClinicaService.java
│       │   │       # - Recebe consultas do RabbitMQ
│       │   │       # - Atende consultas
│       │   │       # - Analisa sintomas
│       │   │       # - Gera diagnósticos
│       │   │       # - Sugere tratamentos
│       │   │       # - Solicita exames de alta complexidade
│       │   │
│       │   ├── 📁 controller/            # 🌐 ENDPOINTS REST
│       │   │   └── 📄 ClinicaController.java
│       │   │       # POST /api/clinica/AtenderConsulta
│       │   │       # POST /api/clinica/verificar-disponibilidade
│       │   │
│       │   ├── 📁 dto/                   # 📦 DTOs
│       │   │   └── 📄 ClinicaDTOs.java
│       │   │
│       │   ├── 📁 config/                # ⚙️ CONFIGURAÇÕES
│       │   │   ├── 📄 SecurityConfig.java
│       │   │   ├── 📄 RabbitMQConfig.java
│       │   │   └── 📄 SwaggerConfig.java
│       │   │
│       │   └── 📁 messaging/             # 📨 MENSAGERIA
│       │       └── 📄 MessageConsumer.java      # Consome fila consulta.queue
│       │
│       └── resources/
│           ├── 📄 application.properties
│           │   # - Porta: 8082
│           │   # - Banco: clinica_db (porta 3308)
│           │
│           └── 📄 data.sql              # 💾 DADOS INICIAIS
│               # - 5 Médicos pré-cadastrados
│               # - 12 Sintomas (com prioridades)
│               # - 6 Doenças (com tratamentos)
│               # - Relacionamentos sintoma-doença
│
├── 📁 centro-cirurgico-service/          # ⭐ MICROSSERVIÇO 3 - Porta 8083
│   ├── 📄 pom.xml
│   │
│   └── src/main/
│       ├── java/com/hospital/centrocirurgico/
│       │   │
│       │   ├── 📄 CentroCirurgicoServiceApplication.java  # ⚡ CLASSE PRINCIPAL
│       │   │
│       │   ├── 📁 entity/                # 🗄️ ENTIDADES
│       │   │   └── 📄 Procedimento.java # Tabela: procedimentos
│       │   │                             # - Exames simples
│       │   │                             # - Exames alta complexidade
│       │   │                             # - Cirurgias
│       │   │                             # - Procedimentos ambulatoriais
│       │   │
│       │   ├── 📁 repository/            # 🔍 ACESSO AO BANCO
│       │   │   └── 📄 ProcedimentoRepository.java
│       │   │
│       │   ├── 📁 service/               # 🧠 LÓGICA DE NEGÓCIO
│       │   │   └── 📄 CentroCirurgicoService.java
│       │   │       # - Recebe exames do RabbitMQ
│       │   │       # - Cria solicitações (da clínica)
│       │   │       # - Marca procedimentos
│       │   │       # - Valida alta complexidade
│       │   │       # - Permite emergenciais
│       │   │
│       │   ├── 📁 controller/            # 🌐 ENDPOINTS REST
│       │   │   └── 📄 CentroCirurgicoController.java
│       │   │       # POST /api/procedimentos/marcar
│       │   │       # POST /api/procedimentos/criar-solicitacao
│       │   │       # POST /api/procedimentos/verificar-disponibilidade
│       │   │       # GET  /api/procedimentos/cpf/{cpf}
│       │   │
│       │   ├── 📁 dto/                   # 📦 DTOs
│       │   │   └── 📄 CentroCirurgicoDTOs.java
│       │   │
│       │   ├── 📁 config/                # ⚙️ CONFIGURAÇÕES
│       │   │   ├── 📄 SecurityConfig.java
│       │   │   ├── 📄 RabbitMQConfig.java
│       │   │   └── 📄 SwaggerConfig.java
│       │   │
│       │   └── 📁 messaging/             # 📨 MENSAGERIA
│       │       └── 📄 MessageConsumer.java      # Consome fila exame.queue
│       │
│       └── resources/
│           └── 📄 application.properties
│               # - Porta: 8083
│               # - Banco: centro_cirurgico_db (porta 3309)
│
└── 📁 gateway-service/                   # ⭐ MICROSSERVIÇO 4 - Porta 8080
    ├── 📄 pom.xml                       # Spring Cloud Gateway
    │
    └── src/main/
        ├── java/com/hospital/gateway/
        │   │
        │   ├── 📄 GatewayServiceApplication.java    # ⚡ CLASSE PRINCIPAL
        │   │
        │   └── 📁 config/                # ⚙️ CONFIGURAÇÕES
        │       └── 📄 SecurityConfig.java    # Autenticação WebFlux
        │
        └── resources/
            └── 📄 application.yml       # 🚦 ROTEAMENTO
                # Rotas:
                # /api/cadastro/**     → agendamento-service:8081
                # /api/pesquisa/**     → agendamento-service:8081
                # /api/admin/**        → agendamento-service:8081
                # /api/clinica/**      → clinica-service:8082
                # /api/procedimentos/** → centro-cirurgico:8083
```

---

## 🗄️ BANCOS DE DADOS (via docker-compose.yml)

```
📊 MySQL Containers:
│
├── mysql-agendamento (porta 3307)
│   └── agendamento_db
│       ├── pacientes
│       ├── consultas
│       └── exames
│
├── mysql-clinica (porta 3308)
│   └── clinica_db
│       ├── medicos
│       ├── consultas_clinica
│       ├── sintomas
│       ├── doencas
│       ├── doenca_sintoma
│       └── consulta_sintomas
│
├── mysql-centro-cirurgico (porta 3309)
│   └── centro_cirurgico_db
│       └── procedimentos
│
└── mysql-keycloak (porta 3310)
    └── keycloak
        └── (tabelas internas do Keycloak)
```

---

## 📨 MENSAGERIA (RabbitMQ - Porta 5672)

```
🐰 RabbitMQ:
│
├── Exchange: hospital.exchange (tipo: Topic)
│
├── Queue: consulta.queue
│   ├── Producer: agendamento-service
│   └── Consumer: clinica-service
│
└── Queue: exame.queue
    ├── Producer: agendamento-service
    └── Consumer: centro-cirurgico-service
```

---

## 🔐 AUTENTICAÇÃO (Keycloak - Porta 8090)

```
🔑 Keycloak:
│
├── Realm: hospital
│
├── Roles:
│   ├── USUARIO    (cadastrar e ver suas consultas/exames)
│   ├── MEDICO     (+ atender consultas, criar exames complexos)
│   └── ADMIN      (+ acesso total, cancelamentos)
│
├── Clients:
│   ├── agendamento-service
│   ├── clinica-service
│   ├── centro-cirurgico-service
│   └── gateway-service
│
└── Users (exemplos):
    ├── paciente1 / senha123  [USUARIO]
    ├── medico1   / senha123  [MEDICO]
    └── admin1    / senha123  [ADMIN]
```

---

## 🌐 ENDPOINTS POR SERVIÇO

### 📍 AGENDAMENTO (via Gateway: localhost:8080 ou direto: localhost:8081)

```
POST   /api/cadastro/consulta              [USUARIO, MEDICO, ADMIN]
POST   /api/cadastro/exame                 [USUARIO, MEDICO, ADMIN]
GET    /api/pesquisa/consultas/cpf/{cpf}   [USUARIO, MEDICO, ADMIN]
GET    /api/pesquisa/consultas/nome/{nome} [MEDICO, ADMIN]
GET    /api/pesquisa/exames/cpf/{cpf}      [USUARIO, MEDICO, ADMIN]
GET    /api/pesquisa/exames/nome/{nome}    [MEDICO, ADMIN]
GET    /api/pesquisa/exames/tipo/{tipo}    [MEDICO, ADMIN]
DELETE /api/admin/consultas/{id}           [ADMIN]
DELETE /api/admin/exames/{id}              [ADMIN]
```

### 📍 CLÍNICA (via Gateway: localhost:8080 ou direto: localhost:8082)

```
POST   /api/clinica/AtenderConsulta              [MEDICO, ADMIN]
POST   /api/clinica/verificar-disponibilidade    [Interno]
```

### 📍 CENTRO CIRÚRGICO (via Gateway: localhost:8080 ou direto: localhost:8083)

```
POST   /api/procedimentos/marcar                    [USUARIO, MEDICO, ADMIN]
POST   /api/procedimentos/criar-solicitacao        [MEDICO, ADMIN]
POST   /api/procedimentos/verificar-disponibilidade [Interno]
GET    /api/procedimentos/cpf/{cpf}                [USUARIO, MEDICO, ADMIN]
```

---

## 📚 SWAGGER (Documentação Interativa)

```
🔍 Acesse:
├── Agendamento:      http://localhost:8081/swagger-ui.html
├── Clínica:          http://localhost:8082/swagger-ui.html
└── Centro Cirúrgico: http://localhost:8083/swagger-ui.html
```

---

## 🔄 FLUXO DE DADOS

### Exemplo: Cadastrar e Atender Consulta

```
1. Cliente → Gateway (8080) → Agendamento (8081)
   POST /api/cadastro/consulta
   
2. Agendamento valida e salva no banco agendamento_db
   
3. Agendamento → RabbitMQ → consulta.queue
   
4. Clínica consome consulta.queue
   
5. Clínica salva no banco clinica_db
   
6. Médico → Gateway (8080) → Clínica (8082)
   POST /api/clinica/AtenderConsulta
   
7. Clínica analisa sintomas e gera diagnóstico
   
8. Se necessário: Clínica → Centro Cirúrgico (HTTP)
   POST /api/procedimentos/criar-solicitacao
   
9. Centro Cirúrgico cria exame e retorna código
   
10. Clínica retorna diagnóstico + código do exame
```

---

## 📊 ESTATÍSTICAS

```
✅ 46 arquivos Java
✅ 4 microsserviços
✅ 3 bancos MySQL (+ 1 para Keycloak)
✅ 2 filas RabbitMQ
✅ 3 documentações Swagger
✅ 1 API Gateway
✅ Sistema completo de autenticação
✅ 100% dos requisitos implementados
```

---

**Este sitemap mostra EXATAMENTE onde está cada arquivo e o que cada um faz!** 🗺️
