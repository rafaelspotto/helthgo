# 🏥 HealthGo - Monitoramento Multiparamétrico de Pacientes

Sistema de monitoramento em tempo real para dados de dispositivos médicos, desenvolvido com foco em **LGPD**, **segurança** e **visualização eficaz** para uso clínico.

## 🎯 Objetivo

Simular o monitoramento multiparamétrico de pacientes a partir de dados recebidos de dispositivos médicos, garantindo:
- ✅ Segurança da informação
- ✅ Privacidade dos dados (LGPD)
- ✅ Visualização eficaz para uso clínico
- ✅ Arquitetura escalável e modular

## 🏗️ Arquitetura

```
┌─────────────────┐    WebSocket    ┌─────────────────┐
│  Simulador Go   │ ──────────────→ │  Backend Java   │
│   Desktop       │                 │  Spring Boot    │
└─────────────────┘                 └─────────────────┘
                                           │
                                           │ WebSocket
                                           ▼
                                    ┌─────────────────┐
                                    │  Frontend React │
                                    │   Dashboard     │
                                    └─────────────────┘
```

## 🛠️ Stack Tecnológica

- **Simulador Desktop**: Go + goroutines + crypto/aes
- **Backend**: Java Spring Boot + PostgreSQL + Redis
- **Frontend**: ReactJS + TypeScript + Material-UI
- **Infraestrutura**: Docker + Docker Compose
- **Segurança**: JWT + HTTPS + AES-256
- **Monitoramento**: Prometheus + Grafana

## 🚀 Como Executar

### Pré-requisitos
- Docker e Docker Compose instalados
- Git

### Execução Rápida
```bash
# Clone o repositório
git clone <url-do-repositorio>
cd healthgo-monitor

# Execute com Docker Compose
docker-compose up -d

# Acesse a aplicação
# Frontend: http://localhost:3000
# Backend API: http://localhost:8080
# Prometheus: http://localhost:9090
# Grafana: http://localhost:3001
```

### Execução Manual
```bash
# 1. Banco de dados
docker-compose up postgres redis -d

# 2. Backend
cd backend
./mvnw spring-boot:run

# 3. Frontend
cd frontend
npm install
npm start

# 4. Simulador
cd simulador-desktop
go run main.go
```

## 📁 Estrutura do Projeto

```
healthgo-monitor/
├── simulador-desktop/     # Simulador Go
├── backend/              # Spring Boot API
├── frontend/             # ReactJS App
├── banco-dados/          # Scripts SQL
├── docker/               # Configurações Docker
├── docs/                 # Documentação
│   ├── ARQUITETURA.md
│   ├── LGPD_COMPLIANCE.md
│   └── API.md
└── README.md
```

## 🔐 Segurança e LGPD

### Medidas Implementadas:
- ✅ **Pseudoanonimização** automática de dados sensíveis
- ✅ **Criptografia AES-256** em repouso e trânsito
- ✅ **HTTPS/WSS** para comunicação segura
- ✅ **JWT** para autenticação
- ✅ **Auditoria completa** de acessos
- ✅ **Logs de conformidade** LGPD

### Dados Sensíveis:
- CPF → Hash SHA-256 + salt
- Nome → Código único (PAC001, PAC002, etc.)
- Dados médicos → Criptografados

## 📊 Funcionalidades

### Dashboard Multi-Paciente
- Visualização simultânea de todos os pacientes
- Indicadores visuais de status (NORMAL/ALERTA)
- Gráficos em tempo real
- Atualização automática a cada 5 segundos

### Visualização Individual
- Tela dedicada por paciente
- Histórico detalhado de dados
- Gráficos específicos por parâmetro

### Exportação de Dados
- Download em CSV/JSON
- Dados anonimizados
- Logs de auditoria
- Relatórios de conformidade

## 🧪 Testes

```bash
# Testes do Backend
cd backend
./mvnw test

# Testes do Frontend
cd frontend
npm test

# Testes do Simulador
cd simulador-desktop
go test ./...
```

## 📈 Monitoramento

- **Prometheus**: Métricas de performance
- **Grafana**: Dashboards de monitoramento
- **Logs**: Estruturados e centralizados
- **Health Checks**: Endpoints de verificação

## 🔧 Configuração

### Variáveis de Ambiente
```bash
# Backend
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/healthgo
SPRING_REDIS_HOST=redis
JWT_SECRET=sua-chave-secreta-aqui

# Frontend
REACT_APP_API_URL=http://localhost:8080
REACT_APP_WS_URL=ws://localhost:8080/ws

# Simulador
BACKEND_URL=http://localhost:8080
WS_URL=ws://localhost:8080/ws
```

## 📝 Documentação

- [Arquitetura](./docs/ARQUITETURA.md)
- [Conformidade LGPD](./docs/LGPD_COMPLIANCE.md)
- [API Documentation](./docs/API.md)

## 🤝 Contribuição

1. Fork o projeto
2. Crie uma branch para sua feature
3. Commit suas mudanças
4. Push para a branch
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

## 📞 Suporte

Para dúvidas ou problemas:
- Abra uma [Issue](../../issues)
- Entre em contato: rh@healthgo.com.br

---

**Desenvolvido com ❤️ para a HealthGo** 