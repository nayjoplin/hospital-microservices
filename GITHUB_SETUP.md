# 📤 GUIA COMPLETO: SUBIR PROJETO NO GITHUB

Guia passo a passo para colocar todo o projeto hospital-microservices no GitHub.

---

## 📋 PRÉ-REQUISITOS

```bash
☐ Conta no GitHub criada (https://github.com/signup)
☐ Git instalado (git --version)
☐ Projeto hospital-microservices completo
☐ Terminal/Git Bash aberto
```

---

## 🚀 PASSO A PASSO COMPLETO

### PASSO 1: Criar Repositório no GitHub (5 min)

1. **Acessar GitHub:**
   - Ir para https://github.com
   - Fazer login

2. **Criar Novo Repositório:**
   - Clicar no `+` no canto superior direito
   - Selecionar `New repository`

3. **Configurar Repositório:**
   ```
   Repository name: hospital-microservices
   Description: Sistema de Gerenciamento Hospitalar com Microsserviços Java 21
   
   ☑ Public (ou Private, sua escolha)
   ☐ Add a README file (NÃO marcar - já temos README)
   ☐ Add .gitignore (NÃO marcar - já temos)
   ☑ Choose a license: MIT License (ou sua preferência)
   ```

4. **Criar Repositório:**
   - Clicar em `Create repository`
   - **ANOTAR A URL:** `https://github.com/SEU_USUARIO/hospital-microservices.git`

---

### PASSO 2: Preparar Projeto Local (10 min)

#### 2.1 Abrir Terminal na Pasta do Projeto

```bash
cd /caminho/para/hospital-microservices

# Verificar se está na pasta correta
ls
# Deve mostrar: agendamento-service, clinica-service, docker-compose.yml, etc
```

#### 2.2 Substituir README

```bash
# Substituir README padrão pelo README do GitHub
mv README.md README_OLD.md
mv README_GITHUB.md README.md

# Editar README.md e substituir placeholders:
# - SEU_USUARIO → seu username do GitHub
# - [Seu Nome] → seu nome real
# - seu.email@example.com → seu email
```

**No VSCode:**
1. Abrir `README.md`
2. `Ctrl+H` (Find and Replace)
3. Substituir:
   - `SEU_USUARIO` → `seu-username-github`
   - `[Seu Nome]` → `Seu Nome Real`
   - `seu.email@example.com` → `seu@email.com`

#### 2.3 Atualizar LICENSE

```bash
# Editar LICENSE e colocar seu nome
# Substituir [Seu Nome] por seu nome real
```

#### 2.4 Verificar .gitignore

```bash
# Já temos .gitignore criado, verificar se está correto
cat .gitignore

# Deve ignorar: target/, .idea/, *.log, etc
```

---

### PASSO 3: Inicializar Git (5 min)

```bash
# Inicializar repositório git
git init

# Verificar status
git status
# Vai mostrar todos os arquivos não rastreados
```

---

### PASSO 4: Adicionar Arquivos ao Git (10 min)

```bash
# Adicionar TODOS os arquivos
git add .

# OU adicionar seletivamente (recomendado para revisar)
git add README.md
git add LICENSE
git add CONTRIBUTING.md
git add Makefile
git add docker-compose.yml
git add docker-compose-full.yml
git add *.sh
git add *.md
git add .gitignore
git add .env.example
git add agendamento-service/
git add clinica-service/
git add centro-cirurgico-service/
git add gateway-service/
git add docs/

# Verificar o que será commitado
git status
```

**⚠️ ATENÇÃO:** NÃO adicione:
- ❌ Pastas `target/` (binários compilados)
- ❌ Arquivos `.env` (credenciais)
- ❌ Arquivos `.iml` ou `.idea/` (IDE)
- ❌ `application-local.properties` (configurações locais)

---

### PASSO 5: Fazer Primeiro Commit (2 min)

```bash
# Commit inicial
git commit -m "feat: commit inicial do projeto hospital-microservices

- 4 microsserviços completos (Agendamento, Clínica, Centro Cirúrgico, Gateway)
- Arquitetura com Spring Boot 3.2.1 e Java 21
- Autenticação com Keycloak
- Mensageria com RabbitMQ
- Docker Compose completo
- Documentação extensiva
- Scripts de automação
- 46 arquivos Java
- 77+ arquivos no total"
```

---

### PASSO 6: Adicionar Remote do GitHub (2 min)

```bash
# Adicionar repositório remoto
git remote add origin https://github.com/SEU_USUARIO/hospital-microservices.git

# Verificar
git remote -v
# Deve mostrar:
# origin  https://github.com/SEU_USUARIO/hospital-microservices.git (fetch)
# origin  https://github.com/SEU_USUARIO/hospital-microservices.git (push)
```

---

### PASSO 7: Fazer Push para GitHub (5 min)

```bash
# Renomear branch para main (se necessário)
git branch -M main

# Fazer push
git push -u origin main

# Vai pedir suas credenciais do GitHub
# Username: seu-username
# Password: seu Personal Access Token (não é a senha normal!)
```

#### 🔐 Como Criar Personal Access Token:

1. GitHub → Settings → Developer settings → Personal access tokens → Tokens (classic)
2. Generate new token (classic)
3. Name: `hospital-microservices`
4. Expiration: 90 days (ou No expiration)
5. Scopes: Marcar `repo` (todos)
6. Generate token
7. **COPIAR O TOKEN** (só aparece uma vez!)
8. Usar como senha no `git push`

#### Alternativa: SSH

```bash
# Se preferir usar SSH (sem senha toda hora):
# 1. Gerar chave SSH
ssh-keygen -t ed25519 -C "seu@email.com"

# 2. Adicionar chave ao ssh-agent
eval "$(ssh-agent -s)"
ssh-add ~/.ssh/id_ed25519

# 3. Copiar chave pública
cat ~/.ssh/id_ed25519.pub
# Copiar o output

# 4. Adicionar no GitHub
# GitHub → Settings → SSH and GPG keys → New SSH key
# Colar a chave

# 5. Mudar remote para SSH
git remote set-url origin git@github.com:SEU_USUARIO/hospital-microservices.git

# 6. Push
git push -u origin main
```

---

### PASSO 8: Verificar no GitHub (2 min)

1. Acessar: https://github.com/SEU_USUARIO/hospital-microservices
2. Verificar se todos os arquivos estão lá
3. Verificar se README está renderizando corretamente
4. Verificar se LICENSE aparece

---

### PASSO 9: Configurar GitHub (10 min)

#### 9.1 Adicionar Topics (Tags)

1. No repositório, clicar em ⚙️ ao lado de About
2. Adicionar topics:
   ```
   java spring-boot microservices docker mysql rabbitmq 
   keycloak oauth2 jwt healthcare hospital-management
   ```

#### 9.2 Atualizar Description

```
Sistema de Gerenciamento Hospitalar com arquitetura de microsserviços usando Java 21, Spring Boot 3, RabbitMQ, MySQL e Keycloak
```

#### 9.3 Adicionar Website (opcional)

Se tiver deploy, adicionar URL. Senão, deixar em branco.

#### 9.4 Configurar Issues

1. Settings → Features
2. Marcar: ☑ Issues

#### 9.5 Criar Labels para Issues

Settings → Labels → New label:

```
bug - Algo não está funcionando - #d73a4a
enhancement - Nova feature - #a2eeef
documentation - Melhorias na documentação - #0075ca
good first issue - Bom para iniciantes - #7057ff
help wanted - Ajuda extra é desejada - #008672
question - Mais informações necessárias - #d876e3
```

---

### PASSO 10: Melhorias Opcionais (20 min)

#### 10.1 Adicionar Badges no README

Editar `README.md` e adicionar badges:

```markdown
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.1-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Docker](https://img.shields.io/badge/Docker-Compose-blue.svg)](https://www.docker.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
```

#### 10.2 Criar Arquivo CHANGELOG.md

```bash
# Criar CHANGELOG.md
cat > CHANGELOG.md << 'EOF'
# Changelog

Todas as mudanças notáveis neste projeto serão documentadas neste arquivo.

O formato é baseado em [Keep a Changelog](https://keepachangelog.com/pt-BR/1.0.0/),
e este projeto adere ao [Semantic Versioning](https://semver.org/lang/pt-BR/).

## [1.0.0] - 2024-12-15

### Adicionado
- Sistema completo de microsserviços
- Serviço de Agendamento (consultas e exames)
- Serviço de Clínica (atendimento e diagnóstico)
- Serviço de Centro Cirúrgico (procedimentos)
- API Gateway com Spring Cloud
- Autenticação com Keycloak
- Mensageria com RabbitMQ
- Docker Compose completo
- Documentação extensiva
- Scripts de automação
- 46 arquivos Java completos
EOF

git add CHANGELOG.md
git commit -m "docs: adiciona changelog"
git push
```

#### 10.3 Criar GitHub Actions (CI/CD)

Criar `.github/workflows/ci.yml`:

```yaml
name: CI

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v3
    
    - name: Set up JDK 21
      uses: actions/setup-java@v3
      with:
        java-version: '21'
        distribution: 'temurin'
        
    - name: Build Agendamento
      run: cd agendamento-service && mvn clean install -DskipTests
      
    - name: Build Clínica
      run: cd clinica-service && mvn clean install -DskipTests
      
    - name: Build Centro Cirúrgico
      run: cd centro-cirurgico-service && mvn clean install -DskipTests
      
    - name: Build Gateway
      run: cd gateway-service && mvn clean install -DskipTests
```

```bash
# Commit
git add .github/
git commit -m "ci: adiciona GitHub Actions"
git push
```

#### 10.4 Criar Branch de Desenvolvimento

```bash
# Criar branch develop
git checkout -b develop
git push -u origin develop

# Voltar para main
git checkout main
```

#### 10.5 Configurar Branch Protection (Recomendado)

1. Settings → Branches → Add rule
2. Branch name pattern: `main`
3. Marcar:
   - ☑ Require a pull request before merging
   - ☑ Require status checks to pass before merging

---

## 🎉 PRONTO! PROJETO NO GITHUB!

### ✅ Checklist Final

```bash
☑ Repositório criado no GitHub
☑ Projeto inicializado com git
☑ Todos os arquivos commitados
☑ Push feito com sucesso
☑ README renderizando corretamente
☑ LICENSE presente
☑ .gitignore funcionando
☑ Topics adicionados
☑ Issues habilitado
☑ (Opcional) CI/CD configurado
☑ (Opcional) Branch develop criado
```

---

## 📊 Estatísticas do Projeto

Seu repositório agora tem:

```
✅ 77+ arquivos
✅ 46 arquivos Java
✅ 4 microsserviços completos
✅ 8 documentações em Markdown
✅ 1 documentação Word profissional
✅ 6 scripts shell
✅ 1 Makefile
✅ Docker Compose completo
✅ Autenticação e autorização
✅ Mensageria RabbitMQ
✅ 100% funcional
```

---

## 🔄 Fluxo de Trabalho Diário

### Fazer Mudanças

```bash
# 1. Atualizar repositório local
git pull origin main

# 2. Criar branch para feature
git checkout -b feature/nova-funcionalidade

# 3. Fazer alterações...

# 4. Adicionar e commitar
git add .
git commit -m "feat: adiciona nova funcionalidade"

# 5. Push da branch
git push -u origin feature/nova-funcionalidade

# 6. Abrir Pull Request no GitHub

# 7. Após aprovação, merge no GitHub

# 8. Atualizar local
git checkout main
git pull origin main

# 9. Deletar branch local
git branch -d feature/nova-funcionalidade
```

---

## 🌟 Divulgar o Projeto

### LinkedIn
```
🚀 Acabei de publicar um projeto completo de Sistema de Gerenciamento Hospitalar!

✨ Arquitetura de Microsserviços
💻 Java 21 + Spring Boot 3
🔐 Keycloak OAuth2/JWT
📨 RabbitMQ
🐳 Docker Compose
📚 Documentação completa

Confira: https://github.com/SEU_USUARIO/hospital-microservices

#Java #SpringBoot #Microservices #Docker #Backend
```

### Twitter/X
```
🏥 Sistema de Gerenciamento Hospitalar open source!

4 microsserviços | Java 21 | Spring Boot 3 | RabbitMQ | Docker

Completo e funcional com docs extensiva 📚

https://github.com/SEU_USUARIO/hospital-microservices

#Java #SpringBoot #Microservices
```

---

## 📞 Suporte

Se tiver problemas:

1. **Erro de autenticação:**
   - Verificar Personal Access Token
   - Verificar permissões do token

2. **Push rejeitado:**
   ```bash
   git pull origin main --rebase
   git push origin main
   ```

3. **Arquivo muito grande:**
   ```bash
   # Remover do histórico
   git filter-branch --tree-filter 'rm -f arquivo-grande.zip' HEAD
   ```

4. **Desfazer commit:**
   ```bash
   # Último commit (mantém alterações)
   git reset --soft HEAD~1
   
   # Último commit (descarta alterações)
   git reset --hard HEAD~1
   ```

---

## 🎯 Próximos Passos

1. ⭐ **Adicionar estrela** no próprio repositório
2. 📝 **Criar Issues** para features futuras
3. 🔧 **Configurar Projects** para organizar tarefas
4. 📊 **Adicionar Wiki** com documentação extra
5. 🤝 **Convidar colaboradores**
6. 🚀 **Fazer deploy** (Heroku, AWS, etc)

---

**Parabéns! Seu projeto está no GitHub! 🎊**

URL do projeto: https://github.com/SEU_USUARIO/hospital-microservices
