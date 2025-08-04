# 🔐 Conformidade LGPD - HealthGo

## 📋 Visão Geral

Este documento detalha as medidas implementadas no sistema HealthGo para garantir conformidade com a **Lei Geral de Proteção de Dados (LGPD)** - Lei nº 13.709/2018.

## 🎯 Princípios da LGPD Aplicados

### 1. Finalidade (Art. 6º, I)
**Implementação**: O sistema coleta dados apenas para monitoramento multiparamétrico de pacientes em ambiente hospitalar.

**Justificativa**: 
- Interesse legítimo do hospital (Art. 7º, IX)
- Execução de contrato (Art. 7º, V)
- Proteção ao crédito (Art. 7º, X)

### 2. Adequação (Art. 6º, II)
**Implementação**: Apenas dados necessários para monitoramento são coletados.

**Dados Coletados**:
- Identificação básica (ID, nome)
- Sinais vitais (frequência cardíaca, pressão, etc.)
- Status de saúde (NORMAL/ALERTA)

### 3. Necessidade (Art. 6º, III)
**Implementação**: Minimização de dados - apenas o essencial é coletado.

**Exclusões**:
- Dados pessoais desnecessários
- Informações sensíveis não relacionadas ao monitoramento
- Histórico médico completo

### 4. Livre Acesso (Art. 6º, IV)
**Implementação**: API REST para consulta de dados próprios.

**Endpoints**:
```
GET /api/pacientes/{id}/dados
GET /api/pacientes/dados/recentes
```

### 5. Qualidade dos Dados (Art. 6º, V)
**Implementação**: Validação e atualização em tempo real.

**Medidas**:
- Validação de entrada
- Verificação de integridade
- Atualização automática

### 6. Transparência (Art. 6º, VI)
**Implementação**: Política de privacidade clara e acessível.

**Informações Disponibilizadas**:
- Finalidade da coleta
- Base legal
- Direitos do titular
- Contato do DPO

### 7. Segurança (Art. 6º, VII)
**Implementação**: Medidas técnicas e organizacionais robustas.

### 8. Não Discriminação (Art. 6º, VIII)
**Implementação**: Tratamento igualitário de todos os pacientes.

### 9. Responsabilização (Art. 6º, IX)
**Implementação**: Documentação completa das medidas adotadas.

## 🔒 Medidas de Segurança Implementadas

### 1. Criptografia de Dados

#### Em Repouso
```java
// Exemplo de criptografia AES-256
@Column(name = "paciente_cpf")
@Convert(converter = CriptografiaConverter.class)
private String pacienteCpf;
```

#### Em Trânsito
```yaml
# HTTPS/WSS obrigatório
server.ssl.enabled=true
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password=${SSL_PASSWORD}
```

### 2. Pseudoanonimização

#### Implementação
```java
@Component
public class PseudoanonimizacaoService {
    
    public String pseudoanonimizarCPF(String cpf) {
        String salt = System.getenv("PSEUDOANONIMIZACAO_SALT");
        return DigestUtils.sha256Hex(cpf + salt);
    }
    
    public String pseudoanonimizarNome(String nome) {
        // Gera código único baseado no nome
        return "PAC" + Math.abs(nome.hashCode()) % 1000;
    }
}
```

### 3. Controle de Acesso

#### Autenticação JWT
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/pacientes/**").hasRole("MEDICO")
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2.jwt());
        return http.build();
    }
}
```

#### Roles Implementadas
- **ADMIN**: Acesso total ao sistema
- **MEDICO**: Acesso aos dados dos pacientes
- **ENFERMEIRO**: Acesso limitado aos dados

### 4. Auditoria Completa

#### Logs de Auditoria
```java
@Entity
@Table(name = "auditoria_acessos")
public class AuditoriaAcesso {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "usuario")
    private String usuario;
    
    @Column(name = "acao")
    private String acao;
    
    @Column(name = "recurso")
    private String recurso;
    
    @Column(name = "timestamp")
    private LocalDateTime timestamp;
    
    @Column(name = "ip_origem")
    private String ipOrigem;
}
```

#### Implementação
```java
@Aspect
@Component
public class AuditoriaAspect {
    
    @Around("@annotation(Auditavel)")
    public Object auditar(ProceedingJoinPoint joinPoint) throws Throwable {
        // Registra acesso antes da execução
        registrarAcesso(joinPoint);
        
        Object result = joinPoint.proceed();
        
        // Registra resultado após execução
        registrarResultado(joinPoint, result);
        
        return result;
    }
}
```

## 📊 Base Legal para Tratamento

### 1. Interesse Legítimo (Art. 7º, IX)
**Justificativa**: Monitoramento de saúde em ambiente hospitalar.

**Documentação**:
- Avaliação de impacto
- Medidas de proteção
- Direitos dos titulares

### 2. Execução de Contrato (Art. 7º, V)
**Justificativa**: Contrato de prestação de serviços médicos.

**Documentação**:
- Termo de consentimento
- Contrato de prestação de serviços
- Política de privacidade

### 3. Proteção ao Crédito (Art. 7º, X)
**Justificativa**: Verificação de capacidade de pagamento.

**Documentação**:
- Política de cobrança
- Procedimentos de verificação
- Direitos do titular

## 🛡️ Direitos dos Titulares

### 1. Confirmação de Existência (Art. 9º, I)
**Implementação**: Endpoint para verificar se dados existem.

```java
@GetMapping("/api/pacientes/{id}/existe")
public ResponseEntity<Boolean> verificarExistencia(@PathVariable String id) {
    boolean existe = dadosPacienteService.existePorId(id);
    return ResponseEntity.ok(existe);
}
```

### 2. Acesso aos Dados (Art. 9º, II)
**Implementação**: API para consulta de dados próprios.

```java
@GetMapping("/api/pacientes/{id}/dados")
public ResponseEntity<List<DadosPaciente>> acessarDados(@PathVariable String id) {
    List<DadosPaciente> dados = dadosPacienteService.buscarPorPaciente(id);
    return ResponseEntity.ok(dados);
}
```

### 3. Correção (Art. 9º, III)
**Implementação**: Endpoint para correção de dados.

```java
@PutMapping("/api/pacientes/{id}/dados")
public ResponseEntity<DadosPaciente> corrigirDados(
    @PathVariable String id, 
    @RequestBody DadosPaciente dados) {
    DadosPaciente corrigido = dadosPacienteService.corrigirDados(id, dados);
    return ResponseEntity.ok(corrigido);
}
```

### 4. Anonimização (Art. 9º, IV)
**Implementação**: Processo de anonimização automática.

```java
@Service
public class AnonimizacaoService {
    
    public void anonimizarDadosAntigos() {
        LocalDateTime limite = LocalDateTime.now().minusYears(5);
        List<DadosPaciente> dadosAntigos = dadosPacienteRepository
            .findByDataCriacaoBefore(limite);
        
        for (DadosPaciente dados : dadosAntigos) {
            dados.setPacienteNome("ANONIMIZADO");
            dados.setPacienteCpf("ANONIMIZADO");
            dadosPacienteRepository.save(dados);
        }
    }
}
```

### 5. Portabilidade (Art. 9º, V)
**Implementação**: Exportação de dados em formato padrão.

```java
@GetMapping("/api/pacientes/{id}/exportar")
public ResponseEntity<String> exportarDados(@PathVariable String id) {
    String dadosExportados = dadosPacienteService.exportarDados(id);
    return ResponseEntity.ok(dadosExportados);
}
```

### 6. Eliminação (Art. 9º, VI)
**Implementação**: Exclusão definitiva de dados.

```java
@DeleteMapping("/api/pacientes/{id}/dados")
public ResponseEntity<Void> eliminarDados(@PathVariable String id) {
    dadosPacienteService.eliminarDados(id);
    return ResponseEntity.ok().build();
}
```

## 📋 Política de Retenção

### 1. Critérios de Retenção
- **Dados ativos**: 5 anos
- **Dados inativos**: 2 anos
- **Logs de auditoria**: 10 anos
- **Backups**: 7 anos

### 2. Processo de Limpeza
```java
@Scheduled(cron = "0 0 2 * * ?") // Diariamente às 2h
public void limparDadosAntigos() {
    LocalDateTime limite = LocalDateTime.now().minusYears(5);
    dadosPacienteRepository.deleteByDataCriacaoBefore(limite);
}
```

### 3. Notificação de Exclusão
```java
@Service
public class NotificacaoService {
    
    public void notificarExclusao(String pacienteId) {
        // Envia e-mail notificando sobre exclusão
        emailService.enviarNotificacaoExclusao(pacienteId);
    }
}
```

## 🔍 Avaliação de Impacto à Proteção de Dados (AIPD)

### 1. Identificação dos Riscos
- **Alto Risco**: Vazamento de dados médicos
- **Médio Risco**: Acesso não autorizado
- **Baixo Risco**: Perda de dados

### 2. Medidas de Mitigação
- **Criptografia**: Proteção contra vazamento
- **Controle de Acesso**: Prevenção de acesso não autorizado
- **Backup**: Proteção contra perda de dados

### 3. Monitoramento Contínuo
- **Logs de Segurança**: Análise de tentativas de acesso
- **Alertas**: Notificação de atividades suspeitas
- **Relatórios**: Análise mensal de conformidade

## 📞 Contato do DPO

### Informações de Contato
- **E-mail**: dpo@healthgo.com.br
- **Telefone**: (11) 99999-9999
- **Endereço**: Rua Example, 123 - São Paulo/SP

### Responsabilidades
- Orientar funcionários sobre práticas de proteção de dados
- Receber comunicações dos titulares de dados
- Orientar a organização sobre suas obrigações
- Atuar como canal de comunicação entre a organização e a ANPD

## 📊 Relatórios de Conformidade

### 1. Relatório Mensal
- Número de acessos aos dados
- Solicitações de titulares
- Incidentes de segurança
- Medidas implementadas

### 2. Relatório Anual
- Avaliação de impacto
- Atualizações na política de privacidade
- Treinamentos realizados
- Melhorias implementadas

## 🚨 Incidentes de Segurança

### 1. Procedimento de Resposta
1. **Identificação**: Detecção do incidente
2. **Contenção**: Isolamento do problema
3. **Eradicação**: Remoção da causa
4. **Recuperação**: Restauração dos sistemas
5. **Análise**: Documentação e aprendizado

### 2. Notificação
- **ANPD**: Em até 72 horas
- **Titulares**: Quando aplicável
- **Autoridades**: Conforme necessário

### 3. Documentação
```java
@Entity
@Table(name = "incidentes_seguranca")
public class IncidenteSeguranca {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "tipo_incidente")
    private String tipoIncidente;
    
    @Column(name = "descricao")
    private String descricao;
    
    @Column(name = "data_ocorrencia")
    private LocalDateTime dataOcorrencia;
    
    @Column(name = "medidas_adotadas")
    private String medidasAdotadas;
    
    @Column(name = "status")
    private String status;
}
```

## 📚 Treinamento e Conscientização

### 1. Programa de Treinamento
- **Funcionários**: Anual
- **Desenvolvedores**: Semestral
- **Gestores**: Trimestral

### 2. Conteúdo Abordado
- Princípios da LGPD
- Medidas de segurança
- Procedimentos de incidentes
- Direitos dos titulares

### 3. Avaliação
- Testes de conhecimento
- Simulações de incidentes
- Feedback dos participantes

## 🔄 Revisão e Atualização

### 1. Revisão Periódica
- **Política de Privacidade**: Anual
- **Medidas de Segurança**: Semestral
- **Procedimentos**: Trimestral

### 2. Atualizações
- Mudanças na legislação
- Novas tecnologias
- Incidentes de segurança
- Feedback dos titulares

---

**Este documento deve ser revisado e atualizado regularmente para garantir conformidade contínua com a LGPD.** 