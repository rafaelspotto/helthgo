package br.com.healthgo.controller;

import br.com.healthgo.model.DadosPaciente;
import br.com.healthgo.service.DadosPacienteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
@CrossOrigin(origins = "*")
public class DadosPacienteController {

    private static final Logger logger = LoggerFactory.getLogger(DadosPacienteController.class);
    
    @Autowired
    private DadosPacienteService dadosPacienteService;

    /**
     * Busca todos os dados de todos os pacientes
     */
    @GetMapping("/dados")
    public ResponseEntity<List<DadosPaciente>> buscarTodosDados() {
        try {
            List<DadosPaciente> dados = dadosPacienteService.buscarTodos();
            logger.info("📊 Retornados {} registros", dados.size());
            return ResponseEntity.ok(dados);
        } catch (Exception e) {
            logger.error("❌ Erro ao buscar todos os dados: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Busca dados de um paciente específico
     */
    @GetMapping("/{pacienteId}/dados")
    public ResponseEntity<List<DadosPaciente>> buscarDadosPorPaciente(@PathVariable String pacienteId) {
        try {
            List<DadosPaciente> dados = dadosPacienteService.buscarPorPaciente(pacienteId);
            logger.info("🔍 Retornados {} registros para paciente: {}", dados.size(), pacienteId);
            return ResponseEntity.ok(dados);
        } catch (Exception e) {
            logger.error("❌ Erro ao buscar dados do paciente {}: {}", pacienteId, e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Busca dados recentes de todos os pacientes
     */
    @GetMapping("/dados/recentes")
    public ResponseEntity<List<DadosPaciente>> buscarDadosRecentes() {
        try {
            List<DadosPaciente> dadosRecentes = dadosPacienteService.buscarDadosRecentes();
            logger.info("📊 Retornados dados recentes de {} pacientes", dadosRecentes.size());
            return ResponseEntity.ok(dadosRecentes);
        } catch (Exception e) {
            logger.error("❌ Erro ao buscar dados recentes: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Busca dados por ID específico
     */
    @GetMapping("/dados/{id}")
    public ResponseEntity<DadosPaciente> buscarPorId(@PathVariable Long id) {
        try {
            return dadosPacienteService.buscarPorId(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            logger.error("❌ Erro ao buscar dados por ID {}: {}", id, e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Busca dados por status
     */
    @GetMapping("/dados/status/{status}")
    public ResponseEntity<List<DadosPaciente>> buscarPorStatus(@PathVariable String status) {
        try {
            List<DadosPaciente> dados = dadosPacienteService.buscarPorStatus(status);
            logger.info("🔍 Retornados {} registros com status: {}", dados.size(), status);
            return ResponseEntity.ok(dados);
        } catch (Exception e) {
            logger.error("❌ Erro ao buscar dados por status {}: {}", status, e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Salva novos dados de paciente
     */
    @PostMapping("/dados")
    public ResponseEntity<DadosPaciente> salvarDados(@RequestBody DadosPaciente dadosPaciente) {
        try {
            DadosPaciente dadosSalvos = dadosPacienteService.salvarDados(dadosPaciente);
            logger.info("💾 Dados salvos para paciente: {}", dadosPaciente.getPacienteId());
            return ResponseEntity.ok(dadosSalvos);
        } catch (Exception e) {
            logger.error("❌ Erro ao salvar dados: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Deleta dados de um paciente específico
     */
    @DeleteMapping("/{pacienteId}/dados")
    public ResponseEntity<Void> deletarDadosPorPaciente(@PathVariable String pacienteId) {
        try {
            dadosPacienteService.deletarPorPaciente(pacienteId);
            logger.info("🗑️ Dados deletados para paciente: {}", pacienteId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("❌ Erro ao deletar dados do paciente {}: {}", pacienteId, e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Retorna estatísticas do sistema
     */
    @GetMapping("/estatisticas")
    public ResponseEntity<EstatisticasResponse> buscarEstatisticas() {
        try {
            long totalRegistros = dadosPacienteService.contarRegistros();
            long registrosNormais = dadosPacienteService.buscarPorStatus("NORMAL").size();
            long registrosAlertas = dadosPacienteService.buscarPorStatus("ALERTA").size();
            
            EstatisticasResponse estatisticas = new EstatisticasResponse(
                totalRegistros, registrosNormais, registrosAlertas
            );
            
            logger.info("📊 Estatísticas: Total={}, Normais={}, Alertas={}", 
                totalRegistros, registrosNormais, registrosAlertas);
            
            return ResponseEntity.ok(estatisticas);
        } catch (Exception e) {
            logger.error("❌ Erro ao buscar estatísticas: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("🏥 HealthGo Backend funcionando!");
    }

    // Classe interna para resposta de estatísticas
    public static class EstatisticasResponse {
        private long totalRegistros;
        private long registrosNormais;
        private long registrosAlertas;

        public EstatisticasResponse(long totalRegistros, long registrosNormais, long registrosAlertas) {
            this.totalRegistros = totalRegistros;
            this.registrosNormais = registrosNormais;
            this.registrosAlertas = registrosAlertas;
        }

        // Getters
        public long getTotalRegistros() { return totalRegistros; }
        public long getRegistrosNormais() { return registrosNormais; }
        public long getRegistrosAlertas() { return registrosAlertas; }
    }
} 