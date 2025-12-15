#!/bin/bash

# Script para gerar estrutura completa dos microsserviços
# Sistema de Gerenciamento Hospitalar

echo "🏥 Gerando estrutura completa dos microsserviços..."

BASE_DIR="/home/claude/hospital-microservices"

# Cores para output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}GERANDO SERVIÇO DE CLÍNICA${NC}"
echo -e "${BLUE}========================================${NC}"

# Criar estrutura de diretórios para clinica-service
mkdir -p "$BASE_DIR/clinica-service/src/main/java/com/hospital/clinica"/{config,controller,dto,entity,repository,service,exception,messaging}
mkdir -p "$BASE_DIR/clinica-service/src/main/resources"

echo -e "${GREEN}✓${NC} Estrutura de diretórios criada para clinica-service"

# Criar pom.xml para clinica-service
cat > "$BASE_DIR/clinica-service/pom.xml" << 'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.1</version>
    </parent>

    <groupId>com.hospital</groupId>
    <artifactId>clinica-service</artifactId>
    <version>1.0.0</version>
    <name>Clinica Service</name>

    <properties>
        <java.version>21</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-amqp</artifactId>
        </dependency>
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
            <version>2.3.0</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-webflux</artifactId>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
EOF

echo -e "${GREEN}✓${NC} pom.xml criado para clinica-service"

# Criar application.properties para clinica-service
cat > "$BASE_DIR/clinica-service/src/main/resources/application.properties" << 'EOF'
# Configurações da Aplicação
spring.application.name=clinica-service
server.port=8082

# Banco de Dados
spring.datasource.url=jdbc:mysql://localhost:3308/clinica_db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# RabbitMQ
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
rabbitmq.queue.consulta=consulta.queue
rabbitmq.exchange=hospital.exchange

# Keycloak
spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:8090/realms/hospital

# Swagger
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
EOF

echo -e "${GREEN}✓${NC} application.properties criado para clinica-service"

echo ""
echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}GERANDO SERVIÇO DE CENTRO CIRÚRGICO${NC}"
echo -e "${BLUE}========================================${NC}"

# Criar estrutura para centro-cirurgico-service
mkdir -p "$BASE_DIR/centro-cirurgico-service/src/main/java/com/hospital/centrocirurgico"/{config,controller,dto,entity,repository,service,exception,messaging}
mkdir -p "$BASE_DIR/centro-cirurgico-service/src/main/resources"

echo -e "${GREEN}✓${NC} Estrutura de diretórios criada para centro-cirurgico-service"

# Criar pom.xml para centro-cirurgico-service
cat > "$BASE_DIR/centro-cirurgico-service/pom.xml" << 'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.1</version>
    </parent>

    <groupId>com.hospital</groupId>
    <artifactId>centro-cirurgico-service</artifactId>
    <version>1.0.0</version>
    <name>Centro Cirurgico Service</name>

    <properties>
        <java.version>21</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-amqp</artifactId>
        </dependency>
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
            <version>2.3.0</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
EOF

echo -e "${GREEN}✓${NC} pom.xml criado para centro-cirurgico-service"

# Criar application.properties para centro-cirurgico-service
cat > "$BASE_DIR/centro-cirurgico-service/src/main/resources/application.properties" << 'EOF'
# Configurações da Aplicação
spring.application.name=centro-cirurgico-service
server.port=8083

# Banco de Dados
spring.datasource.url=jdbc:mysql://localhost:3309/centro_cirurgico_db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# RabbitMQ
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
rabbitmq.queue.exame=exame.queue
rabbitmq.exchange=hospital.exchange

# Keycloak
spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:8090/realms/hospital

# Swagger
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
EOF

echo -e "${GREEN}✓${NC} application.properties criado para centro-cirurgico-service"

echo ""
echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}GERANDO API GATEWAY${NC}"
echo -e "${BLUE}========================================${NC}"

# Criar estrutura para gateway-service
mkdir -p "$BASE_DIR/gateway-service/src/main/java/com/hospital/gateway"/{config,filter}
mkdir -p "$BASE_DIR/gateway-service/src/main/resources"

echo -e "${GREEN}✓${NC} Estrutura de diretórios criada para gateway-service"

# Criar pom.xml para gateway-service
cat > "$BASE_DIR/gateway-service/pom.xml" << 'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.1</version>
    </parent>

    <groupId>com.hospital</groupId>
    <artifactId>gateway-service</artifactId>
    <version>1.0.0</version>
    <name>Gateway Service</name>

    <properties>
        <java.version>21</java.version>
        <spring-cloud.version>2023.0.0</spring-cloud.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-gateway</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
EOF

echo -e "${GREEN}✓${NC} pom.xml criado para gateway-service"

# Criar application.yml para gateway-service
cat > "$BASE_DIR/gateway-service/src/main/resources/application.yml" << 'EOF'
server:
  port: 8080

spring:
  application:
    name: gateway-service
  
  cloud:
    gateway:
      routes:
        - id: agendamento-service
          uri: http://localhost:8081
          predicates:
            - Path=/api/cadastro/**, /api/pesquisa/**, /api/admin/**
          
        - id: clinica-service
          uri: http://localhost:8082
          predicates:
            - Path=/api/clinica/**
          
        - id: centro-cirurgico-service
          uri: http://localhost:8083
          predicates:
            - Path=/api/procedimentos/**
      
      default-filters:
        - DedupeResponseHeader=Access-Control-Allow-Origin Access-Control-Allow-Credentials
  
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: http://localhost:8090/realms/hospital
EOF

echo -e "${GREEN}✓${NC} application.yml criado para gateway-service"

echo ""
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}✓ Estrutura completa gerada com sucesso!${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""
echo "Próximos passos:"
echo "1. Execute: docker-compose up -d"
echo "2. Configure o Keycloak em http://localhost:8090"
echo "3. Execute cada serviço com: mvn spring-boot:run"
echo ""
echo "Documentação completa: docs/DOCUMENTACAO_COMPLETA.docx"
echo "README: README.md"
