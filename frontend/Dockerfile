# Build stage
FROM node:18-alpine AS builder

WORKDIR /app

# Copia os arquivos de dependências
COPY package*.json ./

# Instala as dependências
RUN npm ci --only=production

# Copia o código fonte
COPY . .

# Constrói a aplicação
RUN npm run build

# Production stage
FROM nginx:alpine

# Copia os arquivos construídos
COPY --from=builder /app/build /usr/share/nginx/html

# Copia a configuração do nginx
COPY nginx.conf /etc/nginx/nginx.conf

# Expõe a porta
EXPOSE 80

# Comando para executar o nginx
CMD ["nginx", "-g", "daemon off;"] 