# 🧪 Guia de Testes - Sistema Hospitalar

Este guia contém exemplos práticos de como testar todos os endpoints do sistema.

## 📋 Índice

- [Obter Token de Autenticação](#obter-token-de-autenticação)
- [Endpoints de Agendamento](#endpoints-de-agendamento)
- [Endpoints de Clínica](#endpoints-de-clínica)
- [Endpoints de Centro Cirúrgico](#endpoints-de-centro-cirúrgico)
- [Endpoints Administrativos](#endpoints-administrativos)
- [Coleção Postman](#coleção-postman)

---

## 🔐 Obter Token de Autenticação

Antes de testar qualquer endpoint, você precisa obter um token JWT.

### Usuário Comum (USUARIO)

```bash
curl -X POST http://localhost:8090/realms/hospital/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=agendamento-service" \
  -d "client_secret=agendamento-secret-key-2024" \
  -d "grant_type=password" \
  -d "username=paciente1" \
  -d "password=senha123"
```

### Médico (MEDICO + USUARIO)

```bash
curl -X POST http://localhost:8090/realms/hospital/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=agendamento-service" \
  -d "client_secret=agendamento-secret-key-2024" \
  -d "grant_type=password" \
  -d "username=medico1" \
  -d "password=senha123"
```

### Administrador (ADMIN + MEDICO + USUARIO)

```bash
curl -X POST http://localhost:8090/realms/hospital/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=agendamento-service" \
  -d "client_secret=agendamento-secret-key-2024" \
  -d "grant_type=password" \
  -d "username=admin1" \
  -d "password=admin123"
```

**Resposta**:
```json
{
  "access_token": "eyJhbGciOiJSUzI1NiIsInR5cC...",
  "expires_in": 3600,
  "refresh_expires_in": 1800,
  "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cC...",
  "token_type": "Bearer"
}
```

**💡 Dica**: Copie o `access_token` e use nas próximas requisições!

---

## 📅 Endpoints de Agendamento

### 1. Cadastrar Consulta

**Endpoint**: `POST /api/cadastro/consulta`
**Roles**: `USUARIO`, `MEDICO`, `ADMIN`
**Via Gateway**: `http://localhost:8080/api/cadastro/consulta`
**Direto**: `http://localhost:8081/api/cadastro/consulta`

```bash
curl -X POST http://localhost:8080/api/cadastro/consulta \
  -H "Authorization: Bearer SEU_TOKEN_AQUI" \
  -H "Content-Type: application/json" \
  -d '{
    "cpfPaciente": "123.456.789-00",
    "nomePaciente": "Maria Silva",
    "horario": "2024-12-25T10:00:00",
    "especialidadeMedico": "Cardiologia"
  }'
```

**Resposta (200 OK)**:
```json
{
  "mensagem": "Consulta agendada com sucesso!",
  "codigo": "CONS-001",
  "sucesso": true
}
```

### 2. Cadastrar Exame

**Endpoint**: `POST /api/cadastro/exame`
**Roles**: `USUARIO`, `MEDICO`, `ADMIN`

```bash
curl -X POST http://localhost:8080/api/cadastro/exame \
  -H "Authorization: Bearer SEU_TOKEN_AQUI" \
  -H "Content-Type: application/json" \
  -d '{
    "cpfPaciente": "123.456.789-00",
    "nomePaciente": "Maria Silva",
    "horario": "2024-12-26T14:00:00",
    "tipoExame": "Hemograma Completo"
  }'
```

### 3. Buscar Consultas por CPF

**Endpoint**: `GET /api/pesquisa/consultas/cpf/{cpf}`
**Roles**: `USUARIO`, `MEDICO`, `ADMIN`

```bash
curl -X GET http://localhost:8080/api/pesquisa/consultas/cpf/123.456.789-00 \
  -H "Authorization: Bearer SEU_TOKEN_AQUI"
```

**Resposta**:
```json
[
  {
    "id": 1,
    "cpfPaciente": "123.456.789-00",
    "nomePaciente": "Maria Silva",
    "horario": "2024-12-25T10:00:00",
    "especialidadeMedico": "Cardiologia",
    "status": "AGENDADA",
    "dataCriacao": "2024-12-15T10:30:00"
  }
]
```

### 4. Buscar Consultas por Nome (Médico/Admin)

**Endpoint**: `GET /api/pesquisa/consultas/nome/{nome}`
**Roles**: `MEDICO`, `ADMIN`

```bash
curl -X GET http://localhost:8080/api/pesquisa/consultas/nome/Maria \
  -H "Authorization: Bearer SEU_TOKEN_MEDICO_OU_ADMIN"
```

### 5. Buscar Exames por CPF

**Endpoint**: `GET /api/pesquisa/exames/cpf/{cpf}`
**Roles**: `USUARIO`, `MEDICO`, `ADMIN`

```bash
curl -X GET http://localhost:8080/api/pesquisa/exames/cpf/123.456.789-00 \
  -H "Authorization: Bearer SEU_TOKEN_AQUI"
```

### 6. Buscar Exames por Tipo (Médico/Admin)

**Endpoint**: `GET /api/pesquisa/exames/tipo/{tipo}`
**Roles**: `MEDICO`, `ADMIN`

```bash
curl -X GET http://localhost:8080/api/pesquisa/exames/tipo/Hemograma \
  -H "Authorization: Bearer SEU_TOKEN_MEDICO_OU_ADMIN"
```

---

## 🏥 Endpoints de Clínica

### 1. Atender Consulta (Médico)

**Endpoint**: `POST /api/clinica/AtenderConsulta`
**Roles**: `MEDICO`, `ADMIN`
**Via Gateway**: `http://localhost:8080/api/clinica/AtenderConsulta`
**Direto**: `http://localhost:8082/api/clinica/AtenderConsulta`

```bash
curl -X POST http://localhost:8080/api/clinica/AtenderConsulta \
  -H "Authorization: Bearer SEU_TOKEN_MEDICO" \
  -H "Content-Type: application/json" \
  -d '{
    "cpfPaciente": "123.456.789-00",
    "nomePaciente": "Maria Silva",
    "sintomas": ["Dor no peito", "Falta de ar", "Tontura"],
    "medicoId": 1
  }'
```

**Resposta**:
```json
{
  "diagnostico": "Possível arritmia cardíaca",
  "tratamentoRecomendado": "Repouso, medicação antiarrítmica, acompanhamento cardiológico",
  "examesSolicitados": [
    {
      "codigoExame": "EXAM-12345",
      "tipo": "Eletrocardiograma",
      "prioridade": "ALTA"
    }
  ],
  "observacoes": "Paciente apresenta sintomas de alta complexidade. Encaminhado para exame urgente."
}
```

### 2. Verificar Disponibilidade de Médico

**Endpoint**: `POST /api/clinica/verificar-disponibilidade`
**Roles**: Interno (usado por outros serviços)

```bash
curl -X POST http://localhost:8082/api/clinica/verificar-disponibilidade \
  -H "Content-Type: application/json" \
  -d '{
    "especialidade": "Cardiologia",
    "horario": "2024-12-25T10:00:00"
  }'
```

---

## ⚕️ Endpoints de Centro Cirúrgico

### 1. Marcar Procedimento

**Endpoint**: `POST /api/procedimentos/marcar`
**Roles**: `USUARIO`, `MEDICO`, `ADMIN`
**Via Gateway**: `http://localhost:8080/api/procedimentos/marcar`
**Direto**: `http://localhost:8083/api/procedimentos/marcar`

```bash
curl -X POST http://localhost:8080/api/procedimentos/marcar \
  -H "Authorization: Bearer SEU_TOKEN_AQUI" \
  -H "Content-Type: application/json" \
  -d '{
    "cpfPaciente": "123.456.789-00",
    "nomePaciente": "Maria Silva",
    "tipo": "EXAME_SIMPLES",
    "horario": "2024-12-27T09:00:00",
    "descricao": "Raio-X de Tórax",
    "emergencial": false
  }'
```

**Tipos de Procedimento**:
- `EXAME_SIMPLES`
- `EXAME_ALTA_COMPLEXIDADE`
- `CIRURGIA`
- `PROCEDIMENTO_AMBULATORIAL`

### 2. Criar Solicitação de Exame (Médico)

**Endpoint**: `POST /api/procedimentos/criar-solicitacao`
**Roles**: `MEDICO`, `ADMIN`

```bash
curl -X POST http://localhost:8080/api/procedimentos/criar-solicitacao \
  -H "Authorization: Bearer SEU_TOKEN_MEDICO" \
  -H "Content-Type: application/json" \
  -d '{
    "cpfPaciente": "123.456.789-00",
    "nomePaciente": "Maria Silva",
    "tipo": "EXAME_ALTA_COMPLEXIDADE",
    "descricao": "Ressonância Magnética Cardíaca",
    "prioridade": "ALTA",
    "observacoes": "Paciente com suspeita de arritmia"
  }'
```

**Resposta**:
```json
{
  "codigoProcedimento": "PROC-67890",
  "mensagem": "Procedimento de alta complexidade criado com sucesso",
  "horario": "2024-12-28T08:00:00",
  "status": "AGENDADO"
}
```

### 3. Buscar Procedimentos por CPF

**Endpoint**: `GET /api/procedimentos/cpf/{cpf}`
**Roles**: `USUARIO`, `MEDICO`, `ADMIN`

```bash
curl -X GET http://localhost:8080/api/procedimentos/cpf/123.456.789-00 \
  -H "Authorization: Bearer SEU_TOKEN_AQUI"
```

---

## 🔒 Endpoints Administrativos

### 1. Cancelar Consulta (Admin)

**Endpoint**: `DELETE /api/admin/consultas/{id}`
**Roles**: `ADMIN`
**Via Gateway**: `http://localhost:8080/api/admin/consultas/{id}`

```bash
curl -X DELETE http://localhost:8080/api/admin/consultas/1 \
  -H "Authorization: Bearer SEU_TOKEN_ADMIN"
```

**Resposta**: `204 No Content`

### 2. Cancelar Exame (Admin)

**Endpoint**: `DELETE /api/admin/exames/{id}`
**Roles**: `ADMIN`

```bash
curl -X DELETE http://localhost:8080/api/admin/exames/1 \
  -H "Authorization: Bearer SEU_TOKEN_ADMIN"
```

---

## 🧪 Fluxo Completo de Teste

### Cenário: Paciente agenda consulta → Médico atende → Exame solicitado

#### 1. Paciente cadastra consulta

```bash
# Token de paciente1
TOKEN_PACIENTE="eyJhbGci..."

curl -X POST http://localhost:8080/api/cadastro/consulta \
  -H "Authorization: Bearer $TOKEN_PACIENTE" \
  -H "Content-Type: application/json" \
  -d '{
    "cpfPaciente": "111.222.333-44",
    "nomePaciente": "João Santos",
    "horario": "2024-12-20T14:00:00",
    "especialidadeMedico": "Cardiologia"
  }'
```

#### 2. Médico atende a consulta

```bash
# Token de medico1
TOKEN_MEDICO="eyJhbGci..."

curl -X POST http://localhost:8080/api/clinica/AtenderConsulta \
  -H "Authorization: Bearer $TOKEN_MEDICO" \
  -H "Content-Type: application/json" \
  -d '{
    "cpfPaciente": "111.222.333-44",
    "nomePaciente": "João Santos",
    "sintomas": ["Dor no peito", "Cansaço"],
    "medicoId": 1
  }'
```

#### 3. Sistema automaticamente cria exame de alta complexidade

O endpoint acima já retorna o código do exame criado automaticamente.

#### 4. Paciente consulta seus exames

```bash
curl -X GET http://localhost:8080/api/pesquisa/exames/cpf/111.222.333-44 \
  -H "Authorization: Bearer $TOKEN_PACIENTE"
```

---

## 📦 Coleção Postman

### Importar para Postman:

1. Abra o Postman
2. Clique em **Import**
3. Cole a URL: `https://raw.githubusercontent.com/seu-usuario/hospital-microservices/main/postman/hospital-collection.json`

### Variáveis de Ambiente (Postman):

```json
{
  "gateway_url": "http://localhost:8080",
  "keycloak_url": "http://localhost:8090",
  "realm": "hospital",
  "client_id": "agendamento-service",
  "client_secret": "agendamento-secret-key-2024",
  "username_paciente": "paciente1",
  "password_paciente": "senha123",
  "username_medico": "medico1",
  "password_medico": "senha123",
  "username_admin": "admin1",
  "password_admin": "admin123"
}
```

---

## ✅ Checklist de Testes

- [ ] Obtenção de token para USUARIO
- [ ] Obtenção de token para MEDICO
- [ ] Obtenção de token para ADMIN
- [ ] Cadastro de consulta
- [ ] Cadastro de exame
- [ ] Busca de consultas por CPF
- [ ] Busca de exames por CPF
- [ ] Atendimento de consulta (médico)
- [ ] Criação de procedimento
- [ ] Cancelamento de consulta (admin)
- [ ] Teste de permissões (403 com role incorreta)
- [ ] Teste de autenticação (401 sem token)

---

## 🎯 Dicas de Teste

1. **Use variáveis de ambiente** no Postman para facilitar os testes
2. **Salve os tokens** em variáveis para não precisar copiar sempre
3. **Teste com diferentes usuários** para validar as permissões
4. **Teste cenários de erro** (token inválido, role incorreta, dados inválidos)
5. **Use o Swagger** para testar de forma visual: http://localhost:8081/swagger-ui.html

---

**Bons testes!** 🚀
