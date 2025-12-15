# 🔐 Guia de Configuração do Keycloak

Este guia explica como configurar o Keycloak para autenticação e autorização no Sistema de Gerenciamento Hospitalar.

## 📋 Índice

- [Visão Geral](#visão-geral)
- [Opção 1: Importação Automática (RECOMENDADO)](#opção-1-importação-automática-recomendado)
- [Opção 2: Configuração Manual](#opção-2-configuração-manual)
- [Testando a Configuração](#testando-a-configuração)
- [Usuários Pré-configurados](#usuários-pré-configurados)
- [Troubleshooting](#troubleshooting)

---

## 🎯 Visão Geral

O Keycloak fornece autenticação OAuth2/JWT para todos os microsserviços. A configuração inclui:

- **1 Realm**: `hospital`
- **3 Roles**: `USUARIO`, `MEDICO`, `ADMIN`
- **4 Clients**: Um para cada microsserviço
- **5 Usuários**: Para teste (2 pacientes, 2 médicos, 1 admin)

---

## ✅ Opção 1: Importação Automática (RECOMENDADO)

### Passo 1: Garantir que o Keycloak está rodando

```bash
# Verificar se o Keycloak está ativo
docker ps | grep keycloak

# Se não estiver, inicie:
docker-compose up -d keycloak
```

### Passo 2: Acessar o Console Admin

Abra seu navegador em: **http://localhost:8090**

**Credenciais do Admin:**
- Usuário: `admin`
- Senha: `admin`

### Passo 3: Importar o Realm

1. No menu lateral esquerdo, clique no **dropdown do realm** (canto superior esquerdo onde diz "master")
2. Clique em **"Create Realm"** ou **"Adicionar realm"**
3. Clique em **"Browse"** ou **"Procurar arquivo"**
4. Selecione o arquivo: `keycloak/realm-hospital.json`
5. Clique em **"Create"** ou **"Criar"**

**Pronto!** ✅ O realm `hospital` foi criado com:
- ✅ 3 Roles (USUARIO, MEDICO, ADMIN)
- ✅ 4 Clients (agendamento, clínica, centro-cirurgico, gateway)
- ✅ 5 Usuários de teste

### Passo 4: Verificar a Importação

1. Selecione o realm **"hospital"** no dropdown superior esquerdo
2. Vá em **"Realm roles"** → Você deve ver: USUARIO, MEDICO, ADMIN
3. Vá em **"Clients"** → Você deve ver os 4 clients
4. Vá em **"Users"** → Você deve ver 5 usuários

---

## 🔧 Opção 2: Configuração Manual

Se preferir configurar manualmente, siga estes passos:

### Passo 1: Criar o Realm

1. Acesse: **http://localhost:8090**
2. Login: `admin` / `admin`
3. No dropdown do realm (canto superior esquerdo), clique em **"Create Realm"**
4. Nome do realm: `hospital`
5. Clique em **"Create"**

### Passo 2: Criar as Roles

1. No menu lateral, vá em **"Realm roles"**
2. Clique em **"Create role"**
3. Crie as seguintes roles:

| Role | Descrição |
|------|-----------|
| `USUARIO` | Usuário comum - pode agendar consultas e exames |
| `MEDICO` | Médico - pode atender consultas e solicitar exames |
| `ADMIN` | Administrador - acesso total ao sistema |

### Passo 3: Criar os Clients

Crie 4 clients com as seguintes configurações:

#### Client 1: agendamento-service

1. Vá em **"Clients"** → **"Create client"**
2. **Client ID**: `agendamento-service`
3. Clique em **"Next"**
4. Ative:
   - ✅ Client authentication
   - ✅ Service accounts roles
   - ✅ Direct access grants
5. Clique em **"Next"**
6. **Valid redirect URIs**: `*`
7. **Web origins**: `*`
8. Clique em **"Save"**
9. Vá na aba **"Credentials"**
10. Copie o **Client Secret** (ou defina como: `agendamento-secret-key-2024`)

#### Client 2: clinica-service

Repita o processo com:
- **Client ID**: `clinica-service`
- **Client Secret**: `clinica-secret-key-2024`

#### Client 3: centro-cirurgico-service

Repita o processo com:
- **Client ID**: `centro-cirurgico-service`
- **Client Secret**: `centro-cirurgico-secret-key-2024`

#### Client 4: gateway-service

Repita o processo com:
- **Client ID**: `gateway-service`
- **Client Secret**: `gateway-secret-key-2024`

### Passo 4: Criar Usuários de Teste

1. Vá em **"Users"** → **"Add user"**
2. Crie os seguintes usuários:

#### Usuário 1: paciente1

- **Username**: `paciente1`
- **Email**: `maria.silva@hospital.com`
- **First name**: `Maria`
- **Last name**: `Silva`
- **Email verified**: ✅ ON
- Salve e vá na aba **"Credentials"**
- Defina senha: `senha123`
- **Temporary**: ❌ OFF
- Vá na aba **"Role mapping"**
- Clique em **"Assign role"**
- Selecione: `USUARIO`

#### Usuário 2: paciente2

- **Username**: `paciente2`
- **Email**: `joao.santos@hospital.com`
- **First name**: `João`
- **Last name**: `Santos`
- **Senha**: `senha123`
- **Role**: `USUARIO`

#### Usuário 3: medico1

- **Username**: `medico1`
- **Email**: `carlos.oliveira@hospital.com`
- **First name**: `Dr. Carlos`
- **Last name**: `Oliveira`
- **Senha**: `senha123`
- **Roles**: `MEDICO`, `USUARIO`

#### Usuário 4: medico2

- **Username**: `medico2`
- **Email**: `ana.costa@hospital.com`
- **First name**: `Dra. Ana`
- **Last name**: `Costa`
- **Senha**: `senha123`
- **Roles**: `MEDICO`, `USUARIO`

#### Usuário 5: admin1

- **Username**: `admin1`
- **Email**: `admin@hospital.com`
- **First name**: `Administrador`
- **Last name**: `Sistema`
- **Senha**: `admin123`
- **Roles**: `ADMIN`, `MEDICO`, `USUARIO`

---

## 🧪 Testando a Configuração

### Teste 1: Obter Token de Acesso

Use o comando `curl` para testar:

```bash
# Token para USUARIO (paciente1)
curl -X POST http://localhost:8090/realms/hospital/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=agendamento-service" \
  -d "client_secret=agendamento-secret-key-2024" \
  -d "grant_type=password" \
  -d "username=paciente1" \
  -d "password=senha123"

# Token para MEDICO (medico1)
curl -X POST http://localhost:8090/realms/hospital/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=agendamento-service" \
  -d "client_secret=agendamento-secret-key-2024" \
  -d "grant_type=password" \
  -d "username=medico1" \
  -d "password=senha123"

# Token para ADMIN (admin1)
curl -X POST http://localhost:8090/realms/hospital/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=agendamento-service" \
  -d "client_secret=agendamento-secret-key-2024" \
  -d "grant_type=password" \
  -d "username=admin1" \
  -d "password=admin123"
```

**Resposta esperada**: JSON com `access_token`, `refresh_token`, etc.

### Teste 2: Decodificar o Token

Copie o `access_token` e cole em: **https://jwt.io**

Você deve ver as roles no campo `realm_access.roles`:

```json
{
  "realm_access": {
    "roles": ["USUARIO"]
  }
}
```

---

## 👥 Usuários Pré-configurados

| Username | Senha | Roles | Descrição |
|----------|-------|-------|-----------|
| `paciente1` | `senha123` | USUARIO | Paciente Maria Silva |
| `paciente2` | `senha123` | USUARIO | Paciente João Santos |
| `medico1` | `senha123` | MEDICO, USUARIO | Dr. Carlos Oliveira |
| `medico2` | `senha123` | MEDICO, USUARIO | Dra. Ana Costa |
| `admin1` | `admin123` | ADMIN, MEDICO, USUARIO | Administrador do Sistema |

---

## 🔍 Troubleshooting

### Problema: "Realm not found"

**Solução**: Certifique-se de que o realm `hospital` foi criado e está selecionado.

### Problema: "Invalid client credentials"

**Solução**: Verifique se o `client_secret` está correto. Vá em:
1. Clients → Selecione o client → Aba "Credentials"
2. Copie o secret correto ou redefina para os valores padrão

### Problema: "User not found"

**Solução**: Certifique-se de que está no realm `hospital` antes de procurar usuários.

### Problema: Token não contém roles

**Solução**: Configure o mapper de roles:
1. Clients → Selecione o client → Aba "Client scopes"
2. Clique no scope dedicado
3. Adicione mapper:
   - Name: `roles`
   - Mapper Type: `User Realm Role`
   - Token Claim Name: `realm_access.roles`
   - Add to ID token: ON
   - Add to access token: ON
   - Add to userinfo: ON

### Problema: Keycloak não inicia

**Solução**:
```bash
# Verificar logs
docker logs keycloak

# Reiniciar Keycloak
docker-compose restart keycloak

# Verificar se MySQL do Keycloak está OK
docker exec mysql-keycloak mysqladmin ping -h localhost -u root -proot
```

---

## 📚 Recursos Adicionais

- **Documentação oficial do Keycloak**: https://www.keycloak.org/documentation
- **OAuth2/JWT**: https://oauth.net/2/
- **Postman Collection**: Crie uma collection com os endpoints para testar

---

## ✅ Checklist Final

Após configurar o Keycloak, verifique:

- [ ] Realm `hospital` criado
- [ ] 3 Roles criadas (USUARIO, MEDICO, ADMIN)
- [ ] 4 Clients criados (agendamento, clínica, centro-cirurgico, gateway)
- [ ] Client secrets configurados
- [ ] 5 Usuários criados
- [ ] Usuários com roles corretas atribuídas
- [ ] Teste de token bem-sucedido
- [ ] Token contém as roles no campo `realm_access.roles`

**Configuração concluída!** 🎉

Agora você pode iniciar os microsserviços com autenticação funcional.
