package br.com.healthgo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "dados_pacientes")
public class DadosPaciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "timestamp")
    private String timestamp;

    @Column(name = "paciente_id")
    private String pacienteId;

    @Column(name = "paciente_nome")
    private String pacienteNome;

    @Column(name = "paciente_cpf")
    private String pacienteCpf;

    @Column(name = "freq_cardiaca")
    private Integer freqCardiaca;

    @Column(name = "saturacao_o2")
    private Integer saturacaoO2;

    @Column(name = "pressao_sistolica")
    private Integer pressaoSistolica;

    @Column(name = "pressao_diastolica")
    private Integer pressaoDiastolica;

    @Column(name = "temperatura")
    private Double temperatura;

    @Column(name = "freq_respiratoria")
    private Integer freqRespiratoria;

    @Column(name = "status")
    private String status;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    // Construtores
    public DadosPaciente() {
        this.dataCriacao = LocalDateTime.now();
    }

    public DadosPaciente(String timestamp, String pacienteId, String pacienteNome, String pacienteCpf,
                        Integer freqCardiaca, Integer saturacaoO2, Integer pressaoSistolica,
                        Integer pressaoDiastolica, Double temperatura, Integer freqRespiratoria, String status) {
        this();
        this.timestamp = timestamp;
        this.pacienteId = pacienteId;
        this.pacienteNome = pacienteNome;
        this.pacienteCpf = pacienteCpf;
        this.freqCardiaca = freqCardiaca;
        this.saturacaoO2 = saturacaoO2;
        this.pressaoSistolica = pressaoSistolica;
        this.pressaoDiastolica = pressaoDiastolica;
        this.temperatura = temperatura;
        this.freqRespiratoria = freqRespiratoria;
        this.status = status;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(String pacienteId) {
        this.pacienteId = pacienteId;
    }

    public String getPacienteNome() {
        return pacienteNome;
    }

    public void setPacienteNome(String pacienteNome) {
        this.pacienteNome = pacienteNome;
    }

    public String getPacienteCpf() {
        return pacienteCpf;
    }

    public void setPacienteCpf(String pacienteCpf) {
        this.pacienteCpf = pacienteCpf;
    }

    public Integer getFreqCardiaca() {
        return freqCardiaca;
    }

    public void setFreqCardiaca(Integer freqCardiaca) {
        this.freqCardiaca = freqCardiaca;
    }

    public Integer getSaturacaoO2() {
        return saturacaoO2;
    }

    public void setSaturacaoO2(Integer saturacaoO2) {
        this.saturacaoO2 = saturacaoO2;
    }

    public Integer getPressaoSistolica() {
        return pressaoSistolica;
    }

    public void setPressaoSistolica(Integer pressaoSistolica) {
        this.pressaoSistolica = pressaoSistolica;
    }

    public Integer getPressaoDiastolica() {
        return pressaoDiastolica;
    }

    public void setPressaoDiastolica(Integer pressaoDiastolica) {
        this.pressaoDiastolica = pressaoDiastolica;
    }

    public Double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(Double temperatura) {
        this.temperatura = temperatura;
    }

    public Integer getFreqRespiratoria() {
        return freqRespiratoria;
    }

    public void setFreqRespiratoria(Integer freqRespiratoria) {
        this.freqRespiratoria = freqRespiratoria;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
} 