#!/bin/bash

# Script para visualizar logs dos serviços do HealthGo

echo "📋 Logs do HealthGo"
echo "==================="

# Verificar se há argumentos
if [ $# -eq 0 ]; then
    echo "Uso: $0 [servico]"
    echo ""
    echo "Serviços disponíveis:"
    echo "  backend     - Logs do backend Spring Boot"
    echo "  frontend    - Logs do frontend React"
    echo "  simulador   - Logs do simulador Go"
    echo "  postgres    - Logs do PostgreSQL"
    echo "  redis       - Logs do Redis"
    echo "  prometheus  - Logs do Prometheus"
    echo "  grafana     - Logs do Grafana"
    echo "  all         - Logs de todos os serviços"
    echo ""
    echo "Exemplo: $0 backend"
    exit 1
fi

SERVICO=$1

case $SERVICO in
    "backend")
        echo "📊 Logs do Backend..."
        docker-compose logs -f backend
        ;;
    "frontend")
        echo "🌐 Logs do Frontend..."
        docker-compose logs -f frontend
        ;;
    "simulador")
        echo "🖥️ Logs do Simulador..."
        docker-compose logs -f simulador
        ;;
    "postgres")
        echo "🗄️ Logs do PostgreSQL..."
        docker-compose logs -f postgres
        ;;
    "redis")
        echo "🔴 Logs do Redis..."
        docker-compose logs -f redis
        ;;
    "prometheus")
        echo "📈 Logs do Prometheus..."
        docker-compose logs -f prometheus
        ;;
    "grafana")
        echo "📊 Logs do Grafana..."
        docker-compose logs -f grafana
        ;;
    "all")
        echo "📋 Logs de todos os serviços..."
        docker-compose logs -f
        ;;
    *)
        echo "❌ Serviço '$SERVICO' não encontrado."
        echo "Use '$0' sem argumentos para ver as opções disponíveis."
        exit 1
        ;;
esac 