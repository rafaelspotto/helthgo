#!/bin/bash

# Script para parar os serviços do HealthGo

echo "🛑 Parando serviços do HealthGo..."
echo "=================================="

# Parar todos os containers
docker-compose down

echo "✅ Serviços parados com sucesso!"
echo ""
echo "Para iniciar novamente, execute:"
echo "   docker-compose up -d"
echo ""
echo "Para ver logs:"
echo "   docker-compose logs" 