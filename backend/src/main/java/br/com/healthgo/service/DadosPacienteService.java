package br.com.healthgo.service;

import br.com.healthgo.model.DadosPaciente;
import br.com.healthgo.repository.DadosPacienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DadosPacienteService {

    private static final Logger logger = LoggerFactory.getLogger(DadosPacienteService.class);
    
    @Autowired
    private DadosPacienteRepository dadosPacienteRepository;

    /**
     * Salva os dados de um paciente no banco de dados
     */
    public DadosPaciente salvarDados(DadosPaciente dadosPaciente) {
        try {
            DadosPaciente dadosSalvos = dadosPacienteRepository.save(dadosPaciente);
            logger.info("💾 Dados salvos para paciente: {} - ID: {}", 
                dadosPaciente.getPacienteId(), dadosSalvos.getId());
            return dadosSalvos;
        } catch (Exception e) {
            logger.error("❌ Erro ao salvar dados do paciente {}: {}", 
                dadosPaciente.getPacienteId(), e.getMessage());
            throw e;
        }
    }

    /**
     * Busca todos os dados de um paciente específico
     */
    public List<DadosPaciente> buscarPorPaciente(String pacienteId) {
        try {
            List<DadosPaciente> dados = dadosPacienteRepository.findByPacienteIdOrderByDataCriacaoDesc(pacienteId);
            logger.info("🔍 Buscados {} registros para paciente: {}", dados.size(), pacienteId);
            return dados;
        } catch (Exception e) {
            logger.error("❌ Erro ao buscar dados do paciente {}: {}", pacienteId, e.getMessage());
            throw e;
        }
    }

    /**
     * Busca os dados mais recentes de todos os pacientes
     */
    public List<DadosPaciente> buscarDadosRecentes() {
        try {
            List<DadosPaciente> dadosRecentes = dadosPacienteRepository.findDadosRecentes();
            logger.info("📊 Buscados dados recentes de {} pacientes", dadosRecentes.size());
            return dadosRecentes;
        } catch (Exception e) {
            logger.error("❌ Erro ao buscar dados recentes: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Busca dados de um paciente por ID específico
     */
    public Optional<DadosPaciente> buscarPorId(Long id) {
        try {
            Optional<DadosPaciente> dados = dadosPacienteRepository.findById(id);
            if (dados.isPresent()) {
                logger.info("🔍 Dados encontrados para ID: {}", id);
            } else {
                logger.warn("⚠️ Dados não encontrados para ID: {}", id);
            }
            return dados;
        } catch (Exception e) {
            logger.error("❌ Erro ao buscar dados por ID {}: {}", id, e.getMessage());
            throw e;
        }
    }

    /**
     * Busca todos os dados de todos os pacientes
     */
    public List<DadosPaciente> buscarTodos() {
        try {
            List<DadosPaciente> todosDados = dadosPacienteRepository.findAll();
            logger.info("📋 Total de registros: {}", todosDados.size());
            return todosDados;
        } catch (Exception e) {
            logger.error("❌ Erro ao buscar todos os dados: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Deleta dados de um paciente específico
     */
    public void deletarPorPaciente(String pacienteId) {
        try {
            List<DadosPaciente> dadosParaDeletar = dadosPacienteRepository.findByPacienteId(pacienteId);
            dadosPacienteRepository.deleteAll(dadosParaDeletar);
            logger.info("🗑️ Deletados {} registros do paciente: {}", dadosParaDeletar.size(), pacienteId);
        } catch (Exception e) {
            logger.error("❌ Erro ao deletar dados do paciente {}: {}", pacienteId, e.getMessage());
            throw e;
        }
    }

    /**
     * Conta o número total de registros
     */
    public long contarRegistros() {
        try {
            long total = dadosPacienteRepository.count();
            logger.info("📊 Total de registros no banco: {}", total);
            return total;
        } catch (Exception e) {
            logger.error("❌ Erro ao contar registros: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Busca dados por status (NORMAL/ALERTA)
     */
    public List<DadosPaciente> buscarPorStatus(String status) {
        try {
            List<DadosPaciente> dados = dadosPacienteRepository.findByStatus(status);
            logger.info("🔍 Buscados {} registros com status: {}", dados.size(), status);
            return dados;
        } catch (Exception e) {
            logger.error("❌ Erro ao buscar dados por status {}: {}", status, e.getMessage());
            throw e;
        }
    }
} 