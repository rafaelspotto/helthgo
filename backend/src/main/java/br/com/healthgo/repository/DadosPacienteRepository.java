package br.com.healthgo.repository;

import br.com.healthgo.model.DadosPaciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DadosPacienteRepository extends JpaRepository<DadosPaciente, Long> {

    /**
     * Busca todos os dados de um paciente ordenados por data de criação (mais recente primeiro)
     */
    List<DadosPaciente> findByPacienteIdOrderByDataCriacaoDesc(String pacienteId);

    /**
     * Busca todos os dados de um paciente
     */
    List<DadosPaciente> findByPacienteId(String pacienteId);

    /**
     * Busca dados por status
     */
    List<DadosPaciente> findByStatus(String status);

    /**
     * Busca os dados mais recentes de cada paciente
     */
    @Query("SELECT d FROM DadosPaciente d WHERE d.id IN " +
           "(SELECT MAX(d2.id) FROM DadosPaciente d2 GROUP BY d2.pacienteId)")
    List<DadosPaciente> findDadosRecentes();

    /**
     * Busca dados de um paciente em um período específico
     */
    @Query("SELECT d FROM DadosPaciente d WHERE d.pacienteId = ?1 AND d.dataCriacao BETWEEN ?2 AND ?3")
    List<DadosPaciente> findByPacienteIdAndPeriodo(String pacienteId, java.time.LocalDateTime inicio, java.time.LocalDateTime fim);

    /**
     * Conta registros por paciente
     */
    long countByPacienteId(String pacienteId);

    /**
     * Conta registros por status
     */
    long countByStatus(String status);
} 