# Build stage
FROM golang:1.21-alpine AS builder

WORKDIR /app

# Copia todo o código fonte
COPY . .

# Baixa as dependências e compila
RUN go mod download
RUN go mod verify
RUN CGO_ENABLED=0 GOOS=linux go build -o simulador .

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