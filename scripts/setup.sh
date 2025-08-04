#!/bin/bash

# Script de setup para o projeto HealthGo
# Este script configura e inicia todos os serviços

set -e

echo "🏥 HealthGo - Setup do Sistema de Monitoramento"
echo "================================================"

# Verificar se o Docker está instalado
if ! command -v docker &> /dev/null; then
    echo "❌ Docker não está instalado. Por favor, instale o Docker primeiro."
    exit 1
fi

# Verificar se o Docker Compose está instalado
if ! command -v docker-compose &> /dev/null; then
    echo "❌ Docker Compose não está instalado. Por favor, instale o Docker Compose primeiro."
    exit 1
fi

echo "✅ Docker e Docker Compose encontrados"

# Criar diretórios necessários
echo "📁 Criando diretórios..."
mkdir -p logs
mkdir -p data/postgres
mkdir -p data/redis
mkdir -p data/prometheus
mkdir -p data/grafana

# Definir permissões
echo "🔐 Configurando permissões..."
chmod +x scripts/*.sh

# Parar containers existentes (se houver)
echo "🛑 Parando containers existentes..."
docker-compose down --remove-orphans

# Remover volumes antigos (opcional)
read -p "Deseja remover volumes antigos? (y/N): " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    echo "🗑️ Removendo volumes antigos..."
    docker volume prune -f
fi

# Construir as imagens
echo "🔨 Construindo imagens Docker..."
docker-compose build --no-cache

# Iniciar os serviços
echo "🚀 Iniciando serviços..."
docker-compose up -d

# Aguardar os serviços iniciarem
echo "⏳ Aguardando serviços iniciarem..."
sleep 30

# Verificar status dos serviços
echo "🔍 Verificando status dos serviços..."

# Verificar PostgreSQL
if docker-compose exec -T postgres pg_isready -U healthgo_user -d healthgo; then
    echo "✅ PostgreSQL: OK"
else
    echo "❌ PostgreSQL: ERRO"
fi

# Verificar Redis
if docker-compose exec -T redis redis-cli ping | grep -q "PONG"; then
    echo "✅ Redis: OK"
else
    echo "❌ Redis: ERRO"
fi

# Verificar Backend
if curl -f http://localhost:8080/api/pacientes/health > /dev/null 2>&1; then
    echo "✅ Backend: OK"
else
    echo "❌ Backend: ERRO"
fi

# Verificar Frontend
if curl -f http://localhost:3000 > /dev/null 2>&1; then
    echo "✅ Frontend: OK"
else
    echo "❌ Frontend: ERRO"
fi

# Verificar Prometheus
if curl -f http://localhost:9090 > /dev/null 2>&1; then
    echo "✅ Prometheus: OK"
else
    echo "❌ Prometheus: ERRO"
fi

# Verificar Grafana
if curl -f http://localhost:3001 > /dev/null 2>&1; then
    echo "✅ Grafana: OK"
else
    echo "❌ Grafana: ERRO"
fi

echo ""
echo "🎉 Setup concluído!"
echo ""
echo "📊 URLs dos serviços:"
echo "   Frontend:     http://localhost:3000"
echo "   Backend API:  http://localhost:8080"
echo "   Prometheus:   http://localhost:9090"
echo "   Grafana:      http://localhost:3001 (admin/admin)"
echo ""
echo "📋 Comandos úteis:"
echo "   Ver logs:     docker-compose logs -f"
echo "   Parar:        docker-compose down"
echo "   Reiniciar:    docker-compose restart"
echo "   Status:       docker-compose ps"
echo ""
echo "🔧 Para iniciar o simulador manualmente:"
echo "   docker-compose up simulador"
echo ""
echo "📝 Logs estão disponíveis em:"
echo "   logs/"

# Salvar informações de setup
cat > setup_info.txt << EOF
HealthGo - Informações de Setup
===============================

Data: $(date)
Versão: 1.0.0

URLs dos Serviços:
- Frontend: http://localhost:3000
- Backend API: http://localhost:8080
- Prometheus: http://localhost:9090
- Grafana: http://localhost:3001

Credenciais:
- Grafana: admin/admin

Comandos Úteis:
- Ver logs: docker-compose logs -f
- Parar: docker-compose down
- Reiniciar: docker-compose restart
- Status: docker-compose ps

Arquivos de Configuração:
- docker-compose.yml: Configuração principal
- .env: Variáveis de ambiente
- logs/: Diretório de logs

EOF

echo "📄 Informações salvas em setup_info.txt" 