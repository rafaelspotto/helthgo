# 🏗️ Arquitetura do Sistema HealthGo

## Visão Geral

O sistema HealthGo é uma aplicação full-stack para monitoramento multiparamétrico de pacientes em tempo real, desenvolvida com foco em **segurança**, **LGPD** e **escalabilidade**.

## 🏛️ Arquitetura Geral

```
┌─────────────────┐    WebSocket    ┌─────────────────┐    HTTP/REST    ┌─────────────────┐
│  Simulador Go   │ ──────────────→ │  Backend Java   │ ──────────────→ │  Frontend React │
│   Desktop       │                 │  Spring Boot    │                 │   Dashboard     │
└─────────────────┘                 └─────────────────┘                 └─────────────────┘
                                           │
                                           │ JPA/Hibernate
                                           ▼
                                    ┌─────────────────┐
                                    │  PostgreSQL     │
                                    │   Database      │
                                    └─────────────────┘
                                           │
                                           │ Redis
                                           ▼
                                    ┌─────────────────┐
                                    │     Redis       │
                                    │     Cache       │
                                    └─────────────────┘
```

## 🧩 Componentes do Sistema

### 1. Simulador Desktop (Go)
**Responsabilidade**: Simular dispositivos médicos enviando dados em tempo real

**Características**:
- **Linguagem**: Go (Golang)
- **Concorrência**: Goroutines para múltiplos pacientes
- **Comunicação**: WebSocket para tempo real
- **Intervalo**: 200ms entre envios
- **Dados**: Leitura de arquivos CSV

**Estrutura**:
```
simulador-desktop/
├── main.go              # Ponto de entrada
├── go.mod               # Dependências
├── Dockerfile           # Containerização
└── dados/               # Arquivos CSV
    ├── dados_pac001.csv
    ├── dados_pac002.csv
    └── dados_pac003.csv
```

### 2. Backend (Spring Boot)
**Responsabilidade**: Processar, armazenar e distribuir dados dos pacientes

**Características**:
- **Framework**: Spring Boot 3.2.0
- **Java**: Versão 17
- **Banco**: PostgreSQL + Redis
- **API**: REST + WebSocket
- **Segurança**: JWT + HTTPS

**Estrutura**:
```
backend/
├── src/main/java/br/com/healthgo/
│   ├── MonitorBackendApplication.java    # Classe principal
│   ├── model/
│   │   └── DadosPaciente.java           # Entidade JPA
│   ├── repository/
│   │   └── DadosPacienteRepository.java # Acesso a dados
│   ├── service/
│   │   └── DadosPacienteService.java    # Lógica de negócio
│   ├── controller/
│   │   └── DadosPacienteController.java # API REST
│   ├── websocket/
│   │   └── WebSocketHandler.java        # WebSocket
│   └── config/
│       └── WebSocketConfig.java         # Configuração
├── src/main/resources/
│   └── application.properties           # Configurações
├── pom.xml                              # Dependências Maven
└── Dockerfile                           # Containerização
```

### 3. Frontend (React)
**Responsabilidade**: Interface de usuário para visualização em tempo real

**Características**:
- **Framework**: React 18
- **TypeScript**: Tipagem estática
- **UI**: Material-UI
- **Gráficos**: Chart.js
- **Comunicação**: WebSocket + HTTP

**Estrutura**:
```
frontend/
├── src/
│   ├── components/
│   │   └── Dashboard.tsx               # Componente principal
│   ├── App.tsx                         # Aplicação principal
│   └── index.tsx                       # Ponto de entrada
├── package.json                        # Dependências
└── Dockerfile                          # Containerização
```

### 4. Banco de Dados (PostgreSQL)
**Responsabilidade**: Armazenamento persistente dos dados

**Características**:
- **SGBD**: PostgreSQL 15
- **Dados**: Sinais vitais dos pacientes
- **Auditoria**: Logs de acesso
- **Backup**: Automático

**Schema**:
```sql
CREATE TABLE dados_pacientes (
    id BIGSERIAL PRIMARY KEY,
    timestamp VARCHAR(20),
    paciente_id VARCHAR(20),
    paciente_nome VARCHAR(100),
    paciente_cpf VARCHAR(20),
    freq_cardiaca INTEGER,
    saturacao_o2 INTEGER,
    pressao_sistolica INTEGER,
    pressao_diastolica INTEGER,
    temperatura DOUBLE PRECISION,
    freq_respiratoria INTEGER,
    status VARCHAR(10),
    data_criacao TIMESTAMP
);
```

### 5. Cache (Redis)
**Responsabilidade**: Cache de dados frequentes e sessões

**Características**:
- **Cache**: Redis 7
- **Dados**: Dados recentes, sessões
- **Performance**: Alta velocidade
- **Persistência**: Configurável

## 🔄 Fluxo de Dados

### 1. Simulação de Dados
```
CSV → Simulador Go → WebSocket → Backend → Banco + Cache → WebSocket → Frontend
```

### 2. Visualização em Tempo Real
```
Frontend ← WebSocket ← Backend ← Banco de Dados
```

### 3. API REST
```
Frontend → HTTP/REST → Backend → Banco de Dados → JSON → Frontend
```

## 🔐 Segurança e LGPD

### Medidas Implementadas:

#### 1. Criptografia
- **Transmissão**: HTTPS/WSS
- **Armazenamento**: AES-256
- **Chaves**: Gerenciadas por variáveis de ambiente

#### 2. Pseudoanonimização
- **CPF**: Hash SHA-256 + salt
- **Nome**: Código único (PAC001, PAC002, etc.)
- **Dados sensíveis**: Criptografados

#### 3. Controle de Acesso
- **Autenticação**: JWT
- **Autorização**: Roles (ADMIN, MEDICO, ENFERMEIRO)
- **Auditoria**: Logs completos

#### 4. Conformidade LGPD
- **Base Legal**: Art. 7º, inciso I (interesse legítimo)
- **Minimização**: Apenas dados necessários
- **Transparência**: Política de privacidade
- **Retenção**: Tempo limitado

## 📊 Monitoramento

### 1. Prometheus
- **Métricas**: Performance da aplicação
- **Alertas**: Configuráveis
- **Retenção**: 200 horas

### 2. Grafana
- **Dashboards**: Visualização de métricas
- **Alertas**: Notificações
- **Usuários**: Admin (admin/admin)

### 3. Logs
- **Estrutura**: JSON
- **Níveis**: INFO, WARN, ERROR
- **Rotação**: Automática

## 🚀 Deploy e Infraestrutura

### Docker Compose
```yaml
services:
  postgres:    # Banco de dados
  redis:       # Cache
  backend:     # API Spring Boot
  frontend:    # Interface React
  simulador:   # Simulador Go
  prometheus:  # Monitoramento
  grafana:     # Visualização
```

### Portas Utilizadas
- **Frontend**: 3000
- **Backend**: 8080
- **PostgreSQL**: 5432
- **Redis**: 6379
- **Prometheus**: 9090
- **Grafana**: 3001

## 🔧 Configuração

### Variáveis de Ambiente
```bash
# Backend
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/healthgo
SPRING_DATASOURCE_USERNAME=healthgo_user
SPRING_DATASOURCE_PASSWORD=healthgo_pass
SPRING_REDIS_HOST=redis
JWT_SECRET=sua-chave-secreta-aqui

# Frontend
REACT_APP_API_URL=http://localhost:8080
REACT_APP_WS_URL=ws://localhost:8080/ws

# Simulador
BACKEND_URL=http://localhost:8080
WS_URL=ws://localhost:8080/ws
```

## 📈 Escalabilidade

### 1. Horizontal
- **Load Balancer**: Nginx
- **Múltiplas instâncias**: Backend e Frontend
- **Sharding**: Banco de dados

### 2. Vertical
- **Recursos**: CPU, RAM, Storage
- **Cache**: Redis Cluster
- **CDN**: Arquivos estáticos

### 3. Microserviços
- **Separação**: Por domínio
- **Comunicação**: API Gateway
- **Deploy**: Independente

## 🧪 Testes

### 1. Unitários
- **Backend**: JUnit 5
- **Frontend**: Jest
- **Simulador**: Go testing

### 2. Integração
- **API**: Postman/Newman
- **WebSocket**: Testes automatizados
- **Banco**: Testcontainers

### 3. Performance
- **Load Testing**: Apache JMeter
- **Stress Testing**: Gatling
- **Monitoramento**: Prometheus + Grafana

## 🔄 CI/CD

### Pipeline
1. **Build**: Compilação dos componentes
2. **Test**: Execução de testes
3. **Security**: Análise de vulnerabilidades
4. **Deploy**: Implantação automatizada
5. **Monitor**: Verificação de saúde

### Ferramentas
- **GitHub Actions**: CI/CD
- **Docker**: Containerização
- **Helm**: Orquestração Kubernetes
- **ArgoCD**: GitOps

## 📋 Próximos Passos

### 1. Melhorias Técnicas
- [ ] Implementar autenticação OAuth2
- [ ] Adicionar rate limiting
- [ ] Implementar circuit breaker
- [ ] Configurar backup automático

### 2. Funcionalidades
- [ ] Notificações push
- [ ] Relatórios PDF
- [ ] Exportação de dados
- [ ] Dashboard administrativo

### 3. Infraestrutura
- [ ] Kubernetes deployment
- [ ] Service mesh (Istio)
- [ ] Observabilidade (Jaeger, Zipkin)
- [ ] Disaster recovery

---

**Arquitetura desenvolvida com foco em segurança, escalabilidade e conformidade LGPD.** 