# 📚 Documentação da API HealthGo

## 🌐 Visão Geral

A API HealthGo fornece endpoints REST para gerenciamento de dados de monitoramento multiparamétrico de pacientes, com foco em **segurança**, **LGPD** e **tempo real**.

**Base URL**: `http://localhost:8080/api`

**Versão**: 1.0.0

**Formato**: JSON

## 🔐 Autenticação

### JWT Token
```http
Authorization: Bearer <seu-token-jwt>
```

### Obter Token
```http
POST /auth/login
Content-Type: application/json

{
  "username": "medico@healthgo.com",
  "password": "senha123"
}
```

**Resposta**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tipo": "Bearer",
  "expiraEm": 86400000
}
```

## 📊 Endpoints de Dados dos Pacientes

### 1. Buscar Todos os Dados

**GET** `/pacientes/dados`

**Descrição**: Retorna todos os dados de todos os pacientes.

**Headers**:
```http
Authorization: Bearer <token>
Content-Type: application/json
```

**Resposta**:
```json
[
  {
    "id": 1,
    "timestamp": "12:00:01.20",
    "pacienteId": "PAC001",
    "pacienteNome": "João Silva",
    "pacienteCpf": "123.456.789-00",
    "freqCardiaca": 87,
    "saturacaoO2": 96,
    "pressaoSistolica": 130,
    "pressaoDiastolica": 85,
    "temperatura": 36.7,
    "freqRespiratoria": 18,
    "status": "NORMAL",
    "dataCriacao": "2024-01-15T12:00:01.200Z"
  }
]
```

**Status Codes**:
- `200 OK`: Dados retornados com sucesso
- `401 Unauthorized`: Token inválido ou ausente
- `403 Forbidden`: Sem permissão para acessar
- `500 Internal Server Error`: Erro interno do servidor

### 2. Buscar Dados de um Paciente Específico

**GET** `/pacientes/{pacienteId}/dados`

**Descrição**: Retorna todos os dados de um paciente específico.

**Parâmetros**:
- `pacienteId` (path): ID do paciente (ex: PAC001)

**Resposta**:
```json
[
  {
    "id": 1,
    "timestamp": "12:00:01.20",
    "pacienteId": "PAC001",
    "pacienteNome": "João Silva",
    "pacienteCpf": "123.456.789-00",
    "freqCardiaca": 87,
    "saturacaoO2": 96,
    "pressaoSistolica": 130,
    "pressaoDiastolica": 85,
    "temperatura": 36.7,
    "freqRespiratoria": 18,
    "status": "NORMAL",
    "dataCriacao": "2024-01-15T12:00:01.200Z"
  }
]
```

### 3. Buscar Dados Recentes

**GET** `/pacientes/dados/recentes`

**Descrição**: Retorna os dados mais recentes de todos os pacientes.

**Resposta**:
```json
[
  {
    "id": 1,
    "timestamp": "12:00:01.20",
    "pacienteId": "PAC001",
    "pacienteNome": "João Silva",
    "freqCardiaca": 87,
    "saturacaoO2": 96,
    "pressaoSistolica": 130,
    "pressaoDiastolica": 85,
    "temperatura": 36.7,
    "freqRespiratoria": 18,
    "status": "NORMAL",
    "dataCriacao": "2024-01-15T12:00:01.200Z"
  },
  {
    "id": 2,
    "timestamp": "12:00:01.20",
    "pacienteId": "PAC002",
    "pacienteNome": "Maria Santos",
    "freqCardiaca": 72,
    "saturacaoO2": 98,
    "pressaoSistolica": 120,
    "pressaoDiastolica": 80,
    "temperatura": 36.5,
    "freqRespiratoria": 16,
    "status": "NORMAL",
    "dataCriacao": "2024-01-15T12:00:01.200Z"
  }
]
```

### 4. Buscar Dados por ID

**GET** `/pacientes/dados/{id}`

**Descrição**: Retorna dados específicos por ID.

**Parâmetros**:
- `id` (path): ID único do registro

**Resposta**:
```json
{
  "id": 1,
  "timestamp": "12:00:01.20",
  "pacienteId": "PAC001",
  "pacienteNome": "João Silva",
  "pacienteCpf": "123.456.789-00",
  "freqCardiaca": 87,
  "saturacaoO2": 96,
  "pressaoSistolica": 130,
  "pressaoDiastolica": 85,
  "temperatura": 36.7,
  "freqRespiratoria": 18,
  "status": "NORMAL",
  "dataCriacao": "2024-01-15T12:00:01.200Z"
}
```

**Status Codes**:
- `200 OK`: Dados encontrados
- `404 Not Found`: Dados não encontrados

### 5. Buscar Dados por Status

**GET** `/pacientes/dados/status/{status}`

**Descrição**: Retorna dados filtrados por status (NORMAL/ALERTA).

**Parâmetros**:
- `status` (path): Status dos dados (NORMAL ou ALERTA)

**Resposta**:
```json
[
  {
    "id": 3,
    "timestamp": "12:00:01.20",
    "pacienteId": "PAC003",
    "pacienteNome": "Pedro Oliveira",
    "freqCardiaca": 105,
    "saturacaoO2": 92,
    "pressaoSistolica": 140,
    "pressaoDiastolica": 95,
    "temperatura": 37.2,
    "freqRespiratoria": 24,
    "status": "ALERTA",
    "dataCriacao": "2024-01-15T12:00:01.200Z"
  }
]
```

### 6. Salvar Novos Dados

**POST** `/pacientes/dados`

**Descrição**: Salva novos dados de um paciente.

**Headers**:
```http
Authorization: Bearer <token>
Content-Type: application/json
```

**Body**:
```json
{
  "timestamp": "12:00:01.20",
  "pacienteId": "PAC001",
  "pacienteNome": "João Silva",
  "pacienteCpf": "123.456.789-00",
  "freqCardiaca": 87,
  "saturacaoO2": 96,
  "pressaoSistolica": 130,
  "pressaoDiastolica": 85,
  "temperatura": 36.7,
  "freqRespiratoria": 18,
  "status": "NORMAL"
}
```

**Resposta**:
```json
{
  "id": 1,
  "timestamp": "12:00:01.20",
  "pacienteId": "PAC001",
  "pacienteNome": "João Silva",
  "pacienteCpf": "123.456.789-00",
  "freqCardiaca": 87,
  "saturacaoO2": 96,
  "pressaoSistolica": 130,
  "pressaoDiastolica": 85,
  "temperatura": 36.7,
  "freqRespiratoria": 18,
  "status": "NORMAL",
  "dataCriacao": "2024-01-15T12:00:01.200Z"
}
```

**Status Codes**:
- `201 Created`: Dados criados com sucesso
- `400 Bad Request`: Dados inválidos
- `401 Unauthorized`: Token inválido
- `500 Internal Server Error`: Erro interno

### 7. Deletar Dados de um Paciente

**DELETE** `/pacientes/{pacienteId}/dados`

**Descrição**: Remove todos os dados de um paciente específico.

**Parâmetros**:
- `pacienteId` (path): ID do paciente

**Headers**:
```http
Authorization: Bearer <token>
```

**Status Codes**:
- `200 OK`: Dados deletados com sucesso
- `404 Not Found`: Paciente não encontrado
- `401 Unauthorized`: Token inválido

## 📈 Endpoints de Estatísticas

### 1. Buscar Estatísticas Gerais

**GET** `/pacientes/estatisticas`

**Descrição**: Retorna estatísticas gerais do sistema.

**Resposta**:
```json
{
  "totalRegistros": 1350,
  "registrosNormais": 1200,
  "registrosAlertas": 150
}
```

## 🔍 Endpoints de Verificação

### 1. Health Check

**GET** `/pacientes/health`

**Descrição**: Verifica se a API está funcionando.

**Resposta**:
```json
{
  "status": "UP",
  "timestamp": "2024-01-15T12:00:01.200Z",
  "version": "1.0.0"
}
```

### 2. Verificar Existência de Dados

**GET** `/pacientes/{pacienteId}/existe`

**Descrição**: Verifica se existem dados para um paciente.

**Parâmetros**:
- `pacienteId` (path): ID do paciente

**Resposta**:
```json
{
  "existe": true,
  "quantidadeRegistros": 450
}
```

## 📤 Endpoints de Exportação

### 1. Exportar Dados CSV

**GET** `/pacientes/{pacienteId}/exportar/csv`

**Descrição**: Exporta dados de um paciente em formato CSV.

**Parâmetros**:
- `pacienteId` (path): ID do paciente

**Headers**:
```http
Accept: text/csv
Authorization: Bearer <token>
```

**Resposta**:
```csv
timestamp,paciente_id,paciente_nome,freq_cardiaca,saturacao_o2,pressao_sistolica,pressao_diastolica,temperatura,freq_respiratoria,status
12:00:01.20,PAC001,João Silva,87,96,130,85,36.7,18,NORMAL
12:00:01.40,PAC001,João Silva,88,95,132,86,36.8,17,NORMAL
```

### 2. Exportar Dados JSON

**GET** `/pacientes/{pacienteId}/exportar/json`

**Descrição**: Exporta dados de um paciente em formato JSON.

**Parâmetros**:
- `pacienteId` (path): ID do paciente

**Headers**:
```http
Accept: application/json
Authorization: Bearer <token>
```

**Resposta**:
```json
{
  "pacienteId": "PAC001",
  "pacienteNome": "João Silva",
  "totalRegistros": 450,
  "periodo": {
    "inicio": "2024-01-15T12:00:01.200Z",
    "fim": "2024-01-15T12:01:31.200Z"
  },
  "dados": [
    {
      "timestamp": "12:00:01.20",
      "freqCardiaca": 87,
      "saturacaoO2": 96,
      "pressaoSistolica": 130,
      "pressaoDiastolica": 85,
      "temperatura": 36.7,
      "freqRespiratoria": 18,
      "status": "NORMAL"
    }
  ]
}
```

## 🔐 Endpoints de Segurança

### 1. Verificar Permissões

**GET** `/auth/permissions`

**Descrição**: Verifica as permissões do usuário atual.

**Headers**:
```http
Authorization: Bearer <token>
```

**Resposta**:
```json
{
  "usuario": "medico@healthgo.com",
  "roles": ["MEDICO"],
  "permissions": [
    "READ_PATIENT_DATA",
    "WRITE_PATIENT_DATA"
  ],
  "expiraEm": "2024-01-16T12:00:01.200Z"
}
```

### 2. Renovar Token

**POST** `/auth/refresh`

**Descrição**: Renova o token JWT.

**Headers**:
```http
Authorization: Bearer <token>
```

**Resposta**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tipo": "Bearer",
  "expiraEm": 86400000
}
```

## 📊 WebSocket

### Conexão WebSocket

**URL**: `ws://localhost:8080/ws`

**Descrição**: Conexão em tempo real para receber dados dos pacientes.

**Mensagem de Entrada** (do simulador):
```json
{
  "timestamp": "12:00:01.20",
  "pacienteId": "PAC001",
  "pacienteNome": "João Silva",
  "pacienteCpf": "123.456.789-00",
  "freqCardiaca": 87,
  "saturacaoO2": 96,
  "pressaoSistolica": 130,
  "pressaoDiastolica": 85,
  "temperatura": 36.7,
  "freqRespiratoria": 18,
  "status": "NORMAL"
}
```

**Mensagem de Saída** (para frontend):
```json
{
  "id": 1,
  "timestamp": "12:00:01.20",
  "pacienteId": "PAC001",
  "pacienteNome": "João Silva",
  "freqCardiaca": 87,
  "saturacaoO2": 96,
  "pressaoSistolica": 130,
  "pressaoDiastolica": 85,
  "temperatura": 36.7,
  "freqRespiratoria": 18,
  "status": "NORMAL",
  "dataCriacao": "2024-01-15T12:00:01.200Z"
}
```

## 🚨 Códigos de Erro

### Erros Comuns

| Código | Descrição | Solução |
|--------|-----------|---------|
| `400` | Bad Request | Verificar formato dos dados enviados |
| `401` | Unauthorized | Verificar token de autenticação |
| `403` | Forbidden | Verificar permissões do usuário |
| `404` | Not Found | Verificar se o recurso existe |
| `500` | Internal Server Error | Contatar suporte técnico |

### Exemplo de Erro
```json
{
  "timestamp": "2024-01-15T12:00:01.200Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Dados inválidos: freqCardiaca deve ser entre 40 e 200",
  "path": "/api/pacientes/dados"
}
```

## 📝 Validações

### Dados Obrigatórios
- `timestamp`: String no formato HH:mm:ss.SSS
- `pacienteId`: String não vazia
- `pacienteNome`: String não vazia
- `freqCardiaca`: Integer entre 40 e 200
- `saturacaoO2`: Integer entre 70 e 100
- `pressaoSistolica`: Integer entre 70 e 200
- `pressaoDiastolica`: Integer entre 40 e 120
- `temperatura`: Double entre 35.0 e 42.0
- `freqRespiratoria`: Integer entre 8 e 40
- `status`: String "NORMAL" ou "ALERTA"

### Exemplo de Validação
```json
{
  "timestamp": "12:00:01.20",
  "pacienteId": "PAC001",
  "pacienteNome": "João Silva",
  "freqCardiaca": 87,
  "saturacaoO2": 96,
  "pressaoSistolica": 130,
  "pressaoDiastolica": 85,
  "temperatura": 36.7,
  "freqRespiratoria": 18,
  "status": "NORMAL"
}
```

## 🔄 Rate Limiting

### Limites por Endpoint
- **GET**: 1000 requests/minuto
- **POST**: 100 requests/minuto
- **DELETE**: 50 requests/minuto

### Headers de Rate Limiting
```http
X-RateLimit-Limit: 1000
X-RateLimit-Remaining: 999
X-RateLimit-Reset: 1642248000
```

## 📚 Exemplos de Uso

### cURL

#### Buscar Dados Recentes
```bash
curl -X GET "http://localhost:8080/api/pacientes/dados/recentes" \
  -H "Authorization: Bearer seu-token-aqui" \
  -H "Content-Type: application/json"
```

#### Salvar Novos Dados
```bash
curl -X POST "http://localhost:8080/api/pacientes/dados" \
  -H "Authorization: Bearer seu-token-aqui" \
  -H "Content-Type: application/json" \
  -d '{
    "timestamp": "12:00:01.20",
    "pacienteId": "PAC001",
    "pacienteNome": "João Silva",
    "freqCardiaca": 87,
    "saturacaoO2": 96,
    "pressaoSistolica": 130,
    "pressaoDiastolica": 85,
    "temperatura": 36.7,
    "freqRespiratoria": 18,
    "status": "NORMAL"
  }'
```

### JavaScript

#### Conectar WebSocket
```javascript
const ws = new WebSocket('ws://localhost:8080/ws');

ws.onopen = () => {
  console.log('Conectado ao WebSocket');
};

ws.onmessage = (event) => {
  const dados = JSON.parse(event.data);
  console.log('Dados recebidos:', dados);
};

ws.onclose = () => {
  console.log('Desconectado do WebSocket');
};
```

#### Fazer Requisição HTTP
```javascript
const response = await fetch('http://localhost:8080/api/pacientes/dados/recentes', {
  headers: {
    'Authorization': 'Bearer seu-token-aqui',
    'Content-Type': 'application/json'
  }
});

const dados = await response.json();
console.log('Dados:', dados);
```

## 🔧 Configuração

### Variáveis de Ambiente
```bash
# Servidor
SERVER_PORT=8080
SERVER_CONTEXT_PATH=/

# Banco de Dados
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/healthgo
SPRING_DATASOURCE_USERNAME=healthgo_user
SPRING_DATASOURCE_PASSWORD=healthgo_pass

# Redis
SPRING_REDIS_HOST=localhost
SPRING_REDIS_PORT=6379

# JWT
JWT_SECRET=sua-chave-secreta-aqui
JWT_EXPIRATION=86400000

# Rate Limiting
RATE_LIMIT_REQUESTS_PER_MINUTE=1000
```

---

**API desenvolvida com foco em segurança, performance e conformidade LGPD.** 