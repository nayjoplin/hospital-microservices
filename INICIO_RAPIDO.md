# 🚀 GUIA DE INÍCIO RÁPIDO
## Sistema de Gerenciamento Hospitalar

### ⚡ Começando em 5 Minutos

#### 1️⃣ Pré-requisitos Instalados?
- [x] Java 21
- [x] Maven 3.8+
- [x] Docker & Docker Compose

#### 2️⃣ Subir Infraestrutura
```bash
cd hospital-microservices
docker-compose up -d
```

**Aguarde 2 minutos** para todos os containers iniciarem.

#### 3️⃣ Configurar Keycloak (5 minutos)

1. Acesse: http://localhost:8090
2. Login: `admin` / `admin`
3. Criar Realm: `hospital`
4. Criar Roles: `USUARIO`, `MEDICO`, `ADMIN`
5. Criar Clients: `agendamento-service`, `clinica-service`, `centro-cirurgico-service`
6. Criar Usuário: `paciente1` / `senha123` com role `USUARIO`

#### 4️⃣ Iniciar Serviços

Abra 3 terminais:

**Terminal 1:**
```bash
cd agendamento-service
mvn clean install && mvn spring-boot:run
```

**Terminal 2:**
```bash
cd clinica-service
mvn clean install && mvn spring-boot:run
```

**Terminal 3:**
```bash
cd centro-cirurgico-service
mvn clean install && mvn spring-boot:run
```

#### 5️⃣ Testar!

**Obter Token:**
```bash
curl -X POST http://localhost:8090/realms/hospital/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "username=paciente1" \
  -d "password=senha123" \
  -d "grant_type=password" \
  -d "client_id=agendamento-service" \
  -d "client_secret=SEU_SECRET_AQUI"
```

**Cadastrar Consulta:**
```bash
curl -X POST http://localhost:8081/api/cadastro/consulta \
  -H "Authorization: Bearer SEU_TOKEN_AQUI" \
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

### 📚 Documentação Completa
- Swagger: http://localhost:8081/swagger-ui.html
- Documento Word: `docs/DOCUMENTACAO_COMPLETA.docx`
- README completo: `README.md`

### ⚠️ Troubleshooting Rápido

**Porta ocupada?**
```bash
# Linux/Mac
lsof -i :8081
# Windows
netstat -ano | findstr :8081
```

**Containers não sobem?**
```bash
docker-compose down
docker-compose up -d
docker-compose ps
docker-compose logs
```

**Token expirado?**
Tokens expiram em 5 minutos. Gere um novo!

### 🎯 Endpoints Principais

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | /api/cadastro/consulta | Agendar consulta |
| POST | /api/cadastro/exame | Agendar exame |
| GET | /api/pesquisa/consultas/cpf/{cpf} | Buscar consultas |
| GET | /api/pesquisa/exames/cpf/{cpf} | Buscar exames |

### 🔐 Roles

- **USUARIO**: Pode agendar e ver suas consultas/exames
- **MEDICO**: Pode criar exames de alta complexidade
- **ADMIN**: Acesso total ao sistema

### ✅ Pronto!

Seu sistema está funcionando! 🎉

Para mais detalhes, consulte a documentação completa.
