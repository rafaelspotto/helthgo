# Build stage
FROM golang:1.21-alpine AS builder

WORKDIR /app

# Copia os arquivos de dependências
COPY go.mod go.sum ./

# Baixa as dependências
RUN go mod download

# Copia o código fonte
COPY . .

# Compila o aplicativo
RUN CGO_ENABLED=0 GOOS=linux go build -a -installsuffix cgo -o simulador .

# Runtime stage
FROM alpine:latest

RUN apk --no-cache add ca-certificates

WORKDIR /root/

# Copia o binário compilado
COPY --from=builder /app/simulador .

# Copia os dados CSV
COPY dados/ ./dados/

# Expõe a porta (se necessário)
EXPOSE 8080

# Comando para executar o simulador
CMD ["./simulador"] 