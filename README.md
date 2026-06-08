# 🛰️ Eclipse Protocol — IoT Agronegócio

> **FIAP Global Solution 2026** — Plataforma inteligente de monitoramento agrícola via sensores IoT, leituras ambientais e camada espacial com satélites.

---

## 📋 Índice

1. [Descrição do Projeto](#1-descrição-do-projeto)
2. [Benefícios para o Negócio](#2-benefícios-para-o-negócio)
3. [Arquitetura Macro na Nuvem](#3-arquitetura-macro-na-nuvem)
4. [Entidades e Rotas da API](#4-entidades-e-rotas-da-api)
5. [Instalação — How To](#5-instalação--how-to)
6. [Dockerfile](#6-dockerfile)
7. [Docker Compose](#7-docker-compose)
8. [Script Azure CLI](#8-script-azure-cli)
9. [Evidências SELECT no Banco](#9-evidências-select-no-banco)
10. [Equipe](#10-equipe)

---

## 1. Descrição do Projeto

O **Eclipse Protocol** é uma plataforma de monitoramento inteligente para o agronegócio que centraliza dados de sensores IoT distribuídos em campo, permitindo acompanhamento em tempo real de temperatura, umidade, precipitação e NDVI das plantações.

A solução gera **alertas automáticos** com base nas leituras ambientais, classificados por tipo e severidade, apoiando produtores rurais na tomada de decisão preventiva.

Além da camada agrícola, o projeto conta com uma **camada espacial** que monitora satélites, imagens de satélite, lixo espacial e riscos orbitais.

**Stack:**

| Tecnologia | Versão |
|-----------|--------|
| Java | 17 |
| Spring Boot | 3.x |
| Spring Data JPA | — |
| H2 Database | Modo arquivo |
| SpringDoc OpenAPI | — |
| Docker | 24+ |
| Azure VM | Ubuntu 22.04 |

**Entidades:** Usuario, Localizacao, Propriedade, Plantacao, Sensor, Leitura, Alerta, Satelite, ImagemSatelite, LixoEspacial, RiscoOrbital

---

## 2. Benefícios para o Negócio

| Benefício | Impacto |
|-----------|---------|
| **Alertas automáticos por IoT** | Detecta anomalias (temperatura, NDVI, umidade) antes de virarem perdas |
| **Histórico longitudinal de leituras** | Análise de tendências por talhão e cultura |
| **Gestão multi-propriedade** | Um produtor gerencia várias fazendas e plantações centralizadas |
| **Camada espacial** | Integração futura com Copernicus/Sentinel para NDVI via satélite |
| **API REST documentada** | Integração com apps mobile e dashboards externos via Swagger |
| **Containerização cloud-native** | Deploy em minutos, escalável e reproduzível |

---

## 3. Arquitetura Macro na Nuvem

```
┌────────────────────────────────────────────────────────────────┐
│                  MICROSOFT AZURE — East US                     │
│           Resource Group: rg-eclipse-protocol                  │
│                                                                │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │   VM — vm-eclipse-protocol (Standard_B2s)                │  │
│  │   Ubuntu 22.04 LTS · 2 vCPUs · 4 GB RAM                  │  │
│  │   NSG: 22 | 80 | 8080 | 8082 | 9092                      │  │
│  │                                                          │  │
│  │   ┌──────────────────────────────────────────────────┐   │  │
│  │   │  Docker Engine — rede: eclipse-network           │   │  │
│  │   │                                                  │   │  │
│  │   │  ┌─────────────────────┐  ┌───────────────────┐  │   │  │
│  │   │  │ RM561082-eclipse-app│  │RM561082-eclipse-db│  │   │  │
│  │   │  │                     │  │                   │  │   │  │
│  │   │  │ Spring Boot 3       │  │ H2 Server         │  │   │  │
│  │   │  │ Java 17             │  │ oscarfonts/h2     │  │   │  │
│  │   │  │ USER: eclipse(1001) │  │ :8082 (console)   │  │   │  │
│  │   │  │ WORKDIR: /app       │  │ :9092 (TCP)       │  │   │  │
│  │   │  │ :8080               │  │                   │  │   │  │
│  │   │  └──────────┬──────────┘  └────────┬──────────┘  │   │  │
│  │   │             │                      │             │   │  │
│  │   │             └──────────┬───────────┘             │   │  │
│  │   │                        │                         │   │  │
│  │   │             ┌──────────▼──────────┐              │   │  │
│  │   │             │  Volume Nomeado     │              │   │  │
│  │   │             │  eclipse_h2_data    │              │   │  │
│  │   │             │  /app/data          │              │   │  │
│  │   │             │  eclipsedb.mv.db    │              │   │  │
│  │   │             └─────────────────────┘              │   │  │
│  │   └──────────────────────────────────────────────────┘   │  │
│  └──────────────────────────────────────────────────────────┘  │
└────────────────────────────────────────────────────────────────┘
         ▲
         │  HTTP :8080
         │
┌────────┴──────────┐
│  Cliente / Postman│
│  curl / Swagger   │
└───────────────────┘
```

---

## 4. Entidades e Rotas da API

### 🧑 Usuario — `/usuarios`
| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/usuarios` | Listar todos |
| GET | `/usuarios/{id}` | Buscar por ID |
| POST | `/usuarios` | Criar usuário |
| PUT | `/usuarios/{id}` | Atualizar |
| DELETE | `/usuarios/{id}` | Remover |

### 🏡 Propriedade — `/propriedades`
| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/propriedades` | Listar todas |
| GET | `/propriedades/{id}` | Buscar por ID |
| POST | `/propriedades` | Criar propriedade |
| PUT | `/propriedades/{id}` | Atualizar |
| DELETE | `/propriedades/{id}` | Remover |

### 🌱 Plantacao — `/plantacoes`
| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/plantacoes` | Listar todas |
| GET | `/plantacoes/{id}` | Buscar por ID |
| POST | `/plantacoes` | Criar plantação |
| PUT | `/plantacoes/{id}` | Atualizar |
| DELETE | `/plantacoes/{id}` | Remover |

### 📡 Sensor — `/sensores`
| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/sensores` | Listar todos |
| GET | `/sensores/{id}` | Buscar por ID |
| POST | `/sensores` | Criar sensor |
| PUT | `/sensores/{id}` | Atualizar |
| DELETE | `/sensores/{id}` | Remover |

### 📊 Leitura — `/leituras`
| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/leituras` | Listar todas |
| GET | `/leituras/{id}` | Buscar por ID |
| POST | `/leituras` | Registrar leitura |
| PUT | `/leituras/{id}` | Atualizar |
| DELETE | `/leituras/{id}` | Remover |

### 🚨 Alerta — `/alertas`
| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/alertas` | Listar todos |
| GET | `/alertas/{id}` | Buscar por ID |
| GET | `/alertas/abertos` | Alertas em aberto |
| POST | `/alertas` | Criar alerta |
| PUT | `/alertas/{id}` | Atualizar status |
| DELETE | `/alertas/{id}` | Remover |

### 🔎 Utilitários
| URL | Descrição |
|-----|-----------|
| `http://<HOST>:8080/swagger-ui.html` | Documentação interativa |
| `http://<HOST>:8080/actuator/health` | Health check |
| `http://<HOST>:8082` | H2 Console Web |

**H2 Console:** URL: `jdbc:h2:file:/app/data/eclipsedb` · User: `sa` · Senha: *(vazio)*

---

## 5. Instalação — How To

### Pré-requisitos
- Docker 24+ e Docker Compose Plugin
- Git
- Azure CLI *(apenas para deploy em nuvem)*

### ▶️ Rodar localmente

```bash
# 1. Clonar o repositório
git clone https://github.com/GlobalSolution-FIAP2026/eclipse-protocol-java.git
cd eclipse-protocol-java

# 2. Subir os 2 containers em background
docker compose up -d --build

# 3. Verificar containers
docker compose ps

# 4. Ver logs
docker compose logs -f eclipse-app
docker compose logs -f eclipse-db

# 5. Testar API
curl http://localhost:8080/actuator/health
curl http://localhost:8080/usuarios

# 6. Acessar H2 Console
# http://localhost:8082
# JDBC URL: jdbc:h2:file:/app/data/eclipsedb

# 7. Acessar Swagger
# http://localhost:8080/swagger-ui.html

# 8. Verificar usuário dos containers
docker container exec RM561082-eclipse-app whoami   # eclipse
docker container exec RM561082-eclipse-app pwd       # /app
docker container exec RM561082-eclipse-app ls -l /app

# 9. Derrubar (sem apagar dados)
docker compose down
# Para apagar dados também:
docker compose down -v
```

### ☁️ Deploy na Azure

```bash
# 1. Login
az login

# 2. Provisionar VM
chmod +x azure-setup.sh && ./azure-setup.sh

# 3. Conectar na VM
ssh eclipseadmin@<VM_IP>

# 4. Clonar e subir
cd /opt/eclipse-protocol
git clone https://github.com/GlobalSolution-FIAP2026/eclipse-protocol-java.git .
docker compose up -d --build

# 5. Rodar demo completo
chmod +x demo.sh && ./demo.sh <VM_IP>

# 6. AO FINALIZAR — deletar VM
exit
./azure-delete.sh
```

---

## 6. Dockerfile

```dockerfile
FROM maven:3.9-eclipse-temurin-17-alpine
RUN apk add --no-cache curl
RUN addgroup -S eclipse && adduser -S eclipse -G eclipse -u 1001
WORKDIR /app
ENV APP_NAME="eclipse-protocol" APP_ENV="prod" APP_PORT=8080
COPY pom.xml ./
RUN mvn dependency:go-offline -q
COPY src ./src
RUN mvn package -DskipTests -q
RUN mkdir -p /app/data && chown -R eclipse:eclipse /app
RUN cp target/*.jar app.jar && chown eclipse:eclipse app.jar
USER eclipse
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=10s --start-period=90s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", \
  "-Dspring.datasource.url=jdbc:h2:file:/app/data/eclipsedb;AUTO_SERVER=TRUE", \
  "-jar", "app.jar"]
```

---

## 7. Docker Compose

```yaml
services:
  eclipse-db:
    image: oscarfonts/h2:latest
    container_name: RM561082-eclipse-db
    restart: unless-stopped
    environment:
      H2_OPTIONS: "-tcp -tcpAllowOthers -tcpPort 9092 -web -webAllowOthers -webPort 8082"
    ports:
      - "9092:9092"
      - "8082:8082"
    volumes:
      - eclipse_h2_data:/opt/h2-data
    networks:
      - eclipse-network

  eclipse-app:
    build: .
    container_name: RM561082-eclipse-app
    restart: unless-stopped
    depends_on: [eclipse-db]
    environment:
      SPRING_DATASOURCE_URL: jdbc:h2:file:/app/data/eclipsedb;AUTO_SERVER=TRUE
      SPRING_PROFILES_ACTIVE: prod
    ports:
      - "8080:8080"
    volumes:
      - eclipse_h2_data:/app/data
    networks:
      - eclipse-network
    user: "1001"

networks:
  eclipse-network:
    name: eclipse-network

volumes:
  eclipse_h2_data:
    name: eclipse_h2_data
```

---

## 8. Script Azure CLI

O script `azure-setup.sh` executa em sequência:
1. Resource Group `rg-eclipse-protocol` em `eastus`
2. VM Ubuntu 22.04 LTS — `Standard_B2s`
3. NSG com portas: 22, 80, 8080, 8082, 9092
4. Docker Engine, Docker Compose Plugin, Git e ferramentas

Para remover ao final:
```bash
./azure-delete.sh
```

---

## 9. Evidências SELECT no Banco

```bash
# Conectar no container do banco e executar SELECT
docker container exec RM561082-eclipse-db \
  java -cp /opt/h2*.jar org.h2.tools.Shell \
  -url "jdbc:h2:file:/opt/h2-data/eclipsedb" \
  -user sa -password "" \
  -sql "SELECT * FROM TB_USUARIO;"

# Ou via H2 Console Web:
# http://<HOST>:8082
# JDBC URL: jdbc:h2:file:/opt/h2-data/eclipsedb
```

---

## 10. Equipe

| Nome | RM |
|------|----|
| Gustavo Gomes Martins | 555999 |
| Pedro dos Anjos | 563832 |
| Matheus de Mattos Vecchi | 561716 |
| Nicholas Albuquerque Buzo | 561082 |
| Nicholas Camillo Canadas de Paula | 561262 |

---

> **FIAP** — Global Solution 2026  
> Disciplina: DevOps Tools & Cloud Computing  
> Repositório: [github.com/GlobalSolution-FIAP2026/eclipse-protocol-cloud](https://github.com/GlobalSolution-FIAP2026/eclipse-protocol-cloud)
