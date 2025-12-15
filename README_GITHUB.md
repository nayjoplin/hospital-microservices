# 🏥 Sistema de Gerenciamento Hospitalar - Microsserviços

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.1-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Docker](https://img.shields.io/badge/Docker-Compose-blue.svg)](https://www.docker.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

Sistema completo de gerenciamento hospitalar utilizando arquitetura de microsserviços com Java 21, Spring Boot, RabbitMQ, MySQL e Keycloak.

![Arquitetura](docs/arquitetura-diagram.png)

---

## 📋 Índice

- [Sobre o Projeto](#sobre-o-projeto)
- [Arquitetura](#arquitetura)
- [Tecnologias](#tecnologias)
- [Funcionalidades](#funcionalidades)
- [Pré-requisitos](#pré-requisitos)
- [Instalação](#instalação)
- [Uso](#uso)
- [Documentação](#documentação)
- [Contribuição](#contribuição)
- [Licença](#licença)
- [Contato](#contato)

---

## 🎯 Sobre o Projeto

Sistema de gerenciamento hospitalar desenvolvido com arquitetura de microsserviços, permitindo:

- **Agendamento** de consultas e exames
- **Atendimento clínico** com diagnóstico por sintomas
- **Gestão de procedimentos** cirúrgicos e exames de alta complexidade
- **Autenticação e autorização** com controle de acesso baseado em roles

### ✨ Destaques

- 🚀 **4 microsserviços independentes** com responsabilidades bem definidas
- 🔐 **Segurança robusta** com Keycloak (OAuth2/JWT)
- 📨 **Mensageria assíncrona** com RabbitMQ
- 🗄️ **Bancos isolados** (MySQL) para cada serviço
- 📊 **Documentação Swagger** em todos os serviços
- 🐳 **Docker Compose** completo para toda infraestrutura
- 📚 **Documentação extensiva** em português

---

## 🏗️ Arquitetura

```
┌─────────────────┐
│   API Gateway   │ :8080
│  (Spring Cloud) │
└────────┬────────┘
         │
    ┌────┴────┬──────────┬───────────┐
    │         │          │           │
┌───▼───┐ ┌──▼──┐  ┌────▼─────┐ ┌──▼────┐
│ Agend │ │Clín │  │  Centro  │ │Keycloak│
│ :8081 │ │:8082│  │Cirúrgico │ │ :8090  │
│       │ │     │  │  :8083   │ │        │
└───┬───┘ └──┬──┘  └────┬─────┘ └────────┘
    │        │          │
    └────┬───┴──────────┘
         │
    ┌────▼────┐
    │RabbitMQ │ :5672
    │ :15672  │
    └─────────┘
         │
    ┌────┴────────────┬───────────┬──────────┐
┌───▼────┐ ┌─────────▼┐ ┌────────▼┐ ┌───────▼┐
│MySQL   │ │MySQL     │ │MySQL    │ │MySQL   │
│Agend   │ │Clínica   │ │Centro   │ │Keycloak│
│:3307   │ │:3308     │ │:3309    │ │:3310   │
└────────┘ └──────────┘ └─────────┘ └────────┘
```

### Microsserviços

| Serviço | Porta | Descrição |
|---------|-------|-----------|
| **API Gateway** | 8080 | Roteamento centralizado e autenticação |
| **Agendamento** | 8081 | Gestão de consultas, exames e pacientes |
| **Clínica** | 8082 | Atendimento médico e diagnóstico |
| **Centro Cirúrgico** | 8083 | Procedimentos e exames complexos |

---

## 🛠️ Tecnologias

### Backend
- **Java 21** - LTS mais recente
- **Spring Boot 3.2.1** - Framework principal
- **Spring Security** - Segurança e OAuth2
- **Spring Data JPA** - Persistência de dados
- **Spring Cloud Gateway** - API Gateway
- **Spring AMQP** - Integração RabbitMQ

### Infraestrutura
- **MySQL 8.0** - Banco de dados relacional
- **RabbitMQ 3.12** - Message broker
- **Keycloak 23.0** - Identity and Access Management
- **Docker & Docker Compose** - Containerização

### Ferramentas
- **Maven 3.8+** - Gerenciamento de dependências
- **Swagger/OpenAPI 3.0** - Documentação de APIs
- **Lombok** - Redução de boilerplate

---

## ⚡ Funcionalidades

### 🗓️ Serviço de Agendamento
- ✅ Cadastro de consultas e exames
- ✅ Validação de conflitos de horário
- ✅ Pesquisa por CPF e nome
- ✅ Envio de mensagens para outros serviços
- ✅ Cancelamento (somente admin)

### 🏥 Serviço de Clínica
- ✅ Atendimento de consultas
- ✅ Diagnóstico inteligente por sintomas
- ✅ Sugestão de tratamentos
- ✅ Solicitação de exames de alta complexidade
- ✅ Base de conhecimento (sintomas, doenças)

### 🔬 Centro Cirúrgico
- ✅ Gestão de procedimentos
- ✅ Exames de alta complexidade
- ✅ Priorização (baixa, normal, alta, emergencial)
- ✅ Procedimentos emergenciais

### 🔐 Segurança
- ✅ Autenticação JWT via Keycloak
- ✅ Controle de acesso por roles (USUARIO, MEDICO, ADMIN)
- ✅ API Gateway com autenticação centralizada

---

## 📦 Pré-requisitos

```bash
# Softwares necessários
☐ Java 21 JDK
☐ Maven 3.8+
☐ Docker Desktop
☐ Git

# Verificar instalações
java -version    # Deve mostrar 21.x.x
mvn -version     # Deve mostrar 3.8.x ou superior
docker --version # Deve estar instalado
```

---

## 🚀 Instalação

### 1. Clonar o Repositório

```bash
git clone https://github.com/SEU_USUARIO/hospital-microservices.git
cd hospital-microservices
```

### 2. Iniciar Infraestrutura

```bash
# Usando script automatizado (recomendado)
./start-infrastructure.sh

# OU usando Docker Compose diretamente
docker-compose up -d

# OU usando Makefile
make infra-up
```

Aguarde ~2 minutos para todos os serviços iniciarem.

### 3. Configurar Keycloak

Acesse http://localhost:8090 (admin/admin) e configure:

1. Criar realm: `hospital`
2. Criar roles: `USUARIO`, `MEDICO`, `ADMIN`
3. Criar clients: `agendamento-service`, `clinica-service`, `centro-cirurgico-service`
4. Criar usuários de teste

📖 **Guia detalhado:** [GUIA_6_HORAS.md](GUIA_6_HORAS.md) - Seção "Hora 1"

### 4. Compilar Serviços

```bash
# Compilar todos
./build-all.sh

# OU usando Makefile
make build-all

# OU individualmente
cd agendamento-service && mvn clean install
cd clinica-service && mvn clean install
cd centro-cirurgico-service && mvn clean install
cd gateway-service && mvn clean install
```

### 5. Executar Serviços

**Opção 1: Scripts (4 terminais separados)**
```bash
# Terminal 1
make run-agendamento

# Terminal 2
make run-clinica

# Terminal 3
make run-centro

# Terminal 4
make run-gateway
```

**Opção 2: Spring Boot diretamente**
```bash
cd agendamento-service && mvn spring-boot:run
```

---

## 💻 Uso

### Acessar Documentação Swagger

- Agendamento: http://localhost:8081/swagger-ui.html
- Clínica: http://localhost:8082/swagger-ui.html
- Centro Cirúrgico: http://localhost:8083/swagger-ui.html

### Exemplo de Uso

#### 1. Obter Token JWT

```bash
curl -X POST http://localhost:8090/realms/hospital/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "username=paciente1" \
  -d "password=senha123" \
  -d "grant_type=password" \
  -d "client_id=agendamento-service" \
  -d "client_secret=SEU_CLIENT_SECRET"
```

#### 2. Cadastrar Consulta

```bash
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
```

#### 3. Atender Consulta

```bash
curl -X POST http://localhost:8082/api/clinica/AtenderConsulta \
  -H "Authorization: Bearer TOKEN_MEDICO" \
  -H "Content-Type: application/json" \
  -d '{
    "CPF paciente": "123.456.789-00",
    "Horario": "20/12/2024 14:00",
    "Sintomas": ["febre", "tosse", "cansaço"]
  }'
```

📖 **Mais exemplos:** [GUIA_6_HORAS.md](GUIA_6_HORAS.md)

---

## 📚 Documentação

| Documento | Descrição |
|-----------|-----------|
| [README.md](README.md) | Este arquivo - visão geral |
| [PROJETO_COMPLETO.md](PROJETO_COMPLETO.md) | Resumo técnico detalhado |
| [GUIA_6_HORAS.md](GUIA_6_HORAS.md) | Tutorial passo a passo (6h) |
| [INICIO_RAPIDO.md](INICIO_RAPIDO.md) | Quick start (5 minutos) |
| [SITEMAP.md](SITEMAP.md) | Mapa visual do projeto |
| [DOCKER_README.md](DOCKER_README.md) | Guia completo de Docker |
| [ARQUIVOS_CRIADOS.md](ARQUIVOS_CRIADOS.md) | Lista de todos os arquivos |
| [docs/DOCUMENTACAO_COMPLETA.docx](docs/DOCUMENTACAO_COMPLETA.docx) | Documentação Word (15+ páginas) |

---

## 🔧 Comandos Úteis

```bash
# Ver ajuda do Makefile
make help

# Verificar saúde dos serviços
./health-check.sh

# Ver status da infraestrutura
make status

# Ver logs
make logs

# Parar tudo (preservar dados)
make infra-down

# Parar e APAGAR todos os dados
make infra-clean

# Rebuild completo
make clean && make build-all
```

---

## 🧪 Testes

```bash
# Executar testes de todos os serviços
make test

# Executar testes de um serviço específico
cd agendamento-service && mvn test
```

---

## 📊 Estrutura do Projeto

```
hospital-microservices/
├── 📁 agendamento-service/      # Microsserviço de Agendamento
├── 📁 clinica-service/          # Microsserviço de Clínica
├── 📁 centro-cirurgico-service/ # Microsserviço Centro Cirúrgico
├── 📁 gateway-service/          # API Gateway
├── 📁 docs/                     # Documentação adicional
├── 🐳 docker-compose.yml        # Infraestrutura
├── 🐳 docker-compose-full.yml   # Infraestrutura + Serviços
├── 🛠️ Makefile                 # Comandos facilitados
├── 📜 *.sh                      # Scripts de automação
├── 📄 README.md                 # Este arquivo
└── 📄 *.md                      # Documentações
```

---

## 🤝 Contribuição

Contribuições são bem-vindas! Siga os passos:

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/MinhaFeature`)
3. Commit suas mudanças (`git commit -m 'feat: Adiciona MinhaFeature'`)
4. Push para a branch (`git push origin feature/MinhaFeature`)
5. Abra um Pull Request

### Padrão de Commits

Seguimos o padrão [Conventional Commits](https://www.conventionalcommits.org/):

- `feat:` - Nova funcionalidade
- `fix:` - Correção de bug
- `docs:` - Alteração em documentação
- `refactor:` - Refatoração de código
- `test:` - Adição/alteração de testes
- `chore:` - Tarefas de manutenção

---

## 📜 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---

## 👤 Autor

**Seu Nome**

- GitHub: [@seu_usuario](https://github.com/seu_usuario)
- LinkedIn: [Seu Nome](https://linkedin.com/in/seu-perfil)
- Email: seu.email@example.com

---

## 🙏 Agradecimentos

- Comunidade Spring Boot
- Documentação oficial do Keycloak
- Equipe RabbitMQ
- Todos os contribuidores

---

## 📈 Status do Projeto

✅ **Completo e Funcional**

- [x] Microsserviços implementados
- [x] Autenticação e autorização
- [x] Mensageria RabbitMQ
- [x] Docker Compose
- [x] Documentação completa
- [x] Scripts de automação
- [ ] Testes unitários (em desenvolvimento)
- [ ] Testes de integração (em desenvolvimento)
- [ ] CI/CD Pipeline (planejado)
- [ ] Kubernetes deployment (planejado)

---

## 🔗 Links Úteis

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Keycloak Documentation](https://www.keycloak.org/documentation)
- [RabbitMQ Tutorials](https://www.rabbitmq.com/getstarted.html)
- [Docker Documentation](https://docs.docker.com/)

---

<div align="center">

**⭐ Se este projeto te ajudou, deixe uma estrela!**

Made with ❤️ and ☕

</div>
