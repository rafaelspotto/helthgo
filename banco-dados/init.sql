-- Script de inicialização do banco de dados HealthGo
-- PostgreSQL 15

-- Criar banco de dados (se não existir)
-- CREATE DATABASE healthgo;

-- Conectar ao banco
\c healthgo;

-- Criar tabela de dados dos pacientes
CREATE TABLE IF NOT EXISTS dados_pacientes (
    id BIGSERIAL PRIMARY KEY,
    timestamp VARCHAR(20) NOT NULL,
    paciente_id VARCHAR(20) NOT NULL,
    paciente_nome VARCHAR(100) NOT NULL,
    paciente_cpf VARCHAR(20) NOT NULL,
    freq_cardiaca INTEGER NOT NULL,
    saturacao_o2 INTEGER NOT NULL,
    pressao_sistolica INTEGER NOT NULL,
    pressao_diastolica INTEGER NOT NULL,
    temperatura DOUBLE PRECISION NOT NULL,
    freq_respiratoria INTEGER NOT NULL,
    status VARCHAR(10) NOT NULL,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Criar índices para performance
CREATE INDEX IF NOT EXISTS idx_paciente_id ON dados_pacientes(paciente_id);
CREATE INDEX IF NOT EXISTS idx_data_criacao ON dados_pacientes(data_criacao);
CREATE INDEX IF NOT EXISTS idx_status ON dados_pacientes(status);

-- Criar tabela de auditoria de acessos
CREATE TABLE IF NOT EXISTS auditoria_acessos (
    id BIGSERIAL PRIMARY KEY,
    usuario VARCHAR(100) NOT NULL,
    acao VARCHAR(50) NOT NULL,
    recurso VARCHAR(200) NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ip_origem VARCHAR(45),
    user_agent TEXT
);

-- Criar índices para auditoria
CREATE INDEX IF NOT EXISTS idx_auditoria_usuario ON auditoria_acessos(usuario);
CREATE INDEX IF NOT EXISTS idx_auditoria_timestamp ON auditoria_acessos(timestamp);

-- Criar tabela de incidentes de segurança
CREATE TABLE IF NOT EXISTS incidentes_seguranca (
    id BIGSERIAL PRIMARY KEY,
    tipo_incidente VARCHAR(50) NOT NULL,
    descricao TEXT NOT NULL,
    data_ocorrencia TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    medidas_adotadas TEXT,
    status VARCHAR(20) DEFAULT 'ABERTO'
);

-- Criar índices para incidentes
CREATE INDEX IF NOT EXISTS idx_incidente_tipo ON incidentes_seguranca(tipo_incidente);
CREATE INDEX IF NOT EXISTS idx_incidente_status ON incidentes_seguranca(status);

-- Inserir dados de exemplo (opcional)
INSERT INTO dados_pacientes (timestamp, paciente_id, paciente_nome, paciente_cpf, freq_cardiaca, saturacao_o2, pressao_sistolica, pressao_diastolica, temperatura, freq_respiratoria, status) VALUES
('12:00:01.20', 'PAC001', 'João Silva', '123.456.789-00', 87, 96, 130, 85, 36.7, 18, 'NORMAL'),
('12:00:01.20', 'PAC002', 'Maria Santos', '987.654.321-00', 72, 98, 120, 80, 36.5, 16, 'NORMAL'),
('12:00:01.20', 'PAC003', 'Pedro Oliveira', '456.789.123-00', 105, 92, 140, 95, 37.2, 24, 'ALERTA');

-- Criar view para dados recentes
CREATE OR REPLACE VIEW dados_recentes AS
SELECT DISTINCT ON (paciente_id) *
FROM dados_pacientes
ORDER BY paciente_id, data_criacao DESC;

-- Criar função para limpeza automática de dados antigos
CREATE OR REPLACE FUNCTION limpar_dados_antigos()
RETURNS INTEGER AS $$
DECLARE
    registros_deletados INTEGER;
BEGIN
    DELETE FROM dados_pacientes 
    WHERE data_criacao < CURRENT_TIMESTAMP - INTERVAL '5 years';
    
    GET DIAGNOSTICS registros_deletados = ROW_COUNT;
    RETURN registros_deletados;
END;
$$ LANGUAGE plpgsql;

-- Configurar usuário e permissões
GRANT ALL PRIVILEGES ON DATABASE healthgo TO healthgo_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO healthgo_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO healthgo_user;

-- Comentários sobre as tabelas
COMMENT ON TABLE dados_pacientes IS 'Tabela principal para armazenar dados de monitoramento dos pacientes';
COMMENT ON TABLE auditoria_acessos IS 'Tabela para auditoria de acessos ao sistema';
COMMENT ON TABLE incidentes_seguranca IS 'Tabela para registro de incidentes de segurança';

COMMENT ON COLUMN dados_pacientes.paciente_cpf IS 'CPF pseudoanonimizado do paciente';
COMMENT ON COLUMN dados_pacientes.status IS 'Status do paciente: NORMAL ou ALERTA';
COMMENT ON COLUMN auditoria_acessos.ip_origem IS 'IP de origem do acesso para auditoria LGPD'; 