# 🤝 Guia de Contribuição

Obrigado por considerar contribuir com o Sistema de Gerenciamento Hospitalar! 

## 📋 Índice

- [Código de Conduta](#código-de-conduta)
- [Como Posso Contribuir?](#como-posso-contribuir)
- [Processo de Desenvolvimento](#processo-de-desenvolvimento)
- [Padrões de Código](#padrões-de-código)
- [Commit Guidelines](#commit-guidelines)
- [Pull Request Process](#pull-request-process)

---

## 📜 Código de Conduta

Este projeto adota um Código de Conduta. Ao participar, você concorda em:

- ✅ Usar linguagem acolhedora e inclusiva
- ✅ Respeitar pontos de vista e experiências diferentes
- ✅ Aceitar críticas construtivas
- ✅ Focar no que é melhor para a comunidade
- ❌ Não usar linguagem ou imagens sexualizadas
- ❌ Não fazer ataques pessoais ou políticos
- ❌ Não assediar públicos ou privadamente

---

## 🚀 Como Posso Contribuir?

### Reportar Bugs

Bugs são rastreados como [GitHub Issues](https://github.com/SEU_USUARIO/hospital-microservices/issues).

**Antes de criar um issue:**
- Verifique se já não existe um issue similar
- Use o template de bug report

**Ao reportar, inclua:**
- Descrição clara e concisa
- Passos para reproduzir
- Comportamento esperado vs atual
- Screenshots (se aplicável)
- Ambiente (OS, Java version, etc)
- Logs relevantes

### Sugerir Melhorias

Melhorias são bem-vindas! 

**Ao sugerir:**
- Use o template de feature request
- Explique o problema que resolve
- Descreva a solução proposta
- Descreva alternativas consideradas

### Contribuir com Código

1. **Fork** o repositório
2. **Clone** seu fork
3. **Crie** uma branch
4. **Faça** suas alterações
5. **Teste** suas alterações
6. **Commit** seguindo os padrões
7. **Push** para seu fork
8. **Abra** um Pull Request

---

## 💻 Processo de Desenvolvimento

### 1. Setup do Ambiente

```bash
# Clone o repositório
git clone https://github.com/SEU_USUARIO/hospital-microservices.git
cd hospital-microservices

# Instale dependências
./start-infrastructure.sh
./build-all.sh

# Execute os testes
make test
```

### 2. Crie uma Branch

```bash
# Para nova feature
git checkout -b feature/nome-da-feature

# Para correção de bug
git checkout -b fix/nome-do-bug

# Para documentação
git checkout -b docs/descricao
```

### 3. Faça as Alterações

- Siga os [padrões de código](#padrões-de-código)
- Adicione testes para novas funcionalidades
- Atualize documentação se necessário
- Mantenha commits pequenos e focados

### 4. Teste

```bash
# Testes unitários
mvn test

# Testes de integração
mvn verify

# Health check
./health-check.sh
```

---

## 📏 Padrões de Código

### Java

#### Formatação
- **Indentação:** 4 espaços
- **Linha:** Máximo 120 caracteres
- **Encoding:** UTF-8

#### Nomenclatura
```java
// Classes: PascalCase
public class ConsultaService { }

// Métodos e variáveis: camelCase
private void agendarConsulta() { }
String nomePaciente = "João";

// Constantes: UPPER_SNAKE_CASE
private static final int MAX_TENTATIVAS = 3;

// Packages: lowercase
package com.hospital.agendamento;
```

#### Boas Práticas
```java
// ✅ BOM
@Service
@RequiredArgsConstructor
@Slf4j
public class ConsultaService {
    
    private final ConsultaRepository repository;
    
    @Transactional
    public ConsultaDTO criar(ConsultaRequestDTO dto) {
        log.info("Criando consulta para CPF: {}", dto.getCpf());
        
        validarDisponibilidade(dto);
        
        Consulta consulta = converter(dto);
        consulta = repository.save(consulta);
        
        return toDTO(consulta);
    }
}

// ❌ RUIM
public class ConsultaService {
    @Autowired
    private ConsultaRepository repository; // Não use @Autowired em fields
    
    public ConsultaDTO criar(ConsultaRequestDTO dto) {
        // Sem logs
        // Sem validação
        // Sem tratamento de erros
        return toDTO(repository.save(converter(dto)));
    }
}
```

#### Comentários
```java
// ✅ Comentários em português
// ✅ Explicar "por que", não "o que"
// ✅ Javadoc para métodos públicos

/**
 * Agenda uma consulta médica.
 * 
 * @param dto dados da consulta
 * @return consulta agendada
 * @throws ConsultaConflictException se houver conflito de horário
 */
public ConsultaDTO agendar(ConsultaRequestDTO dto) {
    // Validar disponibilidade antes de criar
    // Isso evita consultas duplicadas no mesmo horário
    validarDisponibilidade(dto);
    
    return criar(dto);
}
```

### Spring Boot

```java
// ✅ Use Constructor Injection
@Service
@RequiredArgsConstructor
public class MeuService {
    private final MeuRepository repository;
}

// ✅ Use @Transactional em operações de escrita
@Transactional
public void salvar(Entity entity) { }

// ✅ Use DTOs para transferência de dados
public ConsultaDTO criar(ConsultaRequestDTO request) { }

// ✅ Valide entrada com Bean Validation
public class ConsultaRequestDTO {
    @NotBlank(message = "CPF é obrigatório")
    private String cpf;
}
```

### Testes

```java
@SpringBootTest
class ConsultaServiceTest {
    
    @Autowired
    private ConsultaService service;
    
    @MockBean
    private ConsultaRepository repository;
    
    @Test
    @DisplayName("Deve criar consulta com sucesso")
    void deveCriarConsulta() {
        // Given
        ConsultaRequestDTO dto = criarDTO();
        
        // When
        ConsultaDTO resultado = service.criar(dto);
        
        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getCpf()).isEqualTo(dto.getCpf());
    }
}
```

---

## 📝 Commit Guidelines

Seguimos o padrão [Conventional Commits](https://www.conventionalcommits.org/).

### Formato

```
<tipo>(<escopo>): <descrição>

[corpo opcional]

[rodapé opcional]
```

### Tipos

- `feat`: Nova funcionalidade
- `fix`: Correção de bug
- `docs`: Alteração em documentação
- `style`: Formatação (não afeta código)
- `refactor`: Refatoração
- `test`: Adição/alteração de testes
- `chore`: Tarefas de manutenção
- `perf`: Melhoria de performance
- `ci`: Alterações em CI/CD

### Exemplos

```bash
# Feature
feat(agendamento): adiciona validação de CPF

# Bug fix
fix(clinica): corrige erro ao buscar sintomas

# Documentação
docs(readme): atualiza instruções de instalação

# Refatoração
refactor(centro): extrai lógica de validação para método

# Teste
test(agendamento): adiciona testes de integração

# Breaking change
feat(auth)!: migra para OAuth2

BREAKING CHANGE: A autenticação agora requer OAuth2
```

### Regras

- ✅ Use imperativo ("adiciona" não "adicionado")
- ✅ Não capitalize primeira letra
- ✅ Sem ponto final
- ✅ Máximo 72 caracteres no título
- ✅ Corpo do commit opcional para explicações
- ✅ Use português

---

## 🔄 Pull Request Process

### 1. Antes de Abrir

- ✅ Código compilando sem erros
- ✅ Todos os testes passando
- ✅ Documentação atualizada
- ✅ Commits seguem padrão
- ✅ Branch atualizada com main

```bash
# Atualizar branch
git checkout main
git pull upstream main
git checkout sua-branch
git rebase main
```

### 2. Abrir PR

**Template do PR:**

```markdown
## Descrição
Descreva suas alterações aqui.

## Tipo de Mudança
- [ ] Bug fix
- [ ] Nova feature
- [ ] Breaking change
- [ ] Documentação

## Como Testar?
Descreva como testar suas alterações.

## Checklist
- [ ] Código compila
- [ ] Testes passam
- [ ] Documentação atualizada
- [ ] Commits seguem padrão
```

### 3. Revisão

- Responda aos comentários prontamente
- Faça alterações solicitadas
- Seja receptivo a feedback
- Mantenha discussões profissionais

### 4. Merge

Após aprovação:
- Squash commits se necessário
- Garanta que CI passou
- Aguarde merge do maintainer

---

## 🎯 Áreas para Contribuir

### Alto Impacto
- 🐛 Correção de bugs reportados
- 📚 Melhoria de documentação
- ✅ Adição de testes
- ♿ Melhorias de acessibilidade

### Novas Features
- 📧 Sistema de notificações por email
- 📊 Dashboard de métricas
- 🔍 Busca avançada
- 📱 API Mobile
- 🌐 Internacionalização

### Infraestrutura
- ⚙️ CI/CD Pipeline
- 📈 Monitoramento e observabilidade
- 🐳 Kubernetes deployment
- 🔒 Melhorias de segurança

---

## 💬 Comunicação

- **Issues:** Para bugs e features
- **Discussions:** Para perguntas e discussões
- **Pull Requests:** Para código

---

## 📚 Recursos

- [Spring Boot Docs](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Java Code Conventions](https://www.oracle.com/java/technologies/javase/codeconventions-contents.html)
- [Conventional Commits](https://www.conventionalcommits.org/)

---

## ❓ Dúvidas?

Abra uma [Discussion](https://github.com/SEU_USUARIO/hospital-microservices/discussions) ou entre em contato!

---

**Obrigado por contribuir! 🎉**
