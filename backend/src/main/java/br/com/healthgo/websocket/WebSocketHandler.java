package br.com.healthgo.websocket;

import br.com.healthgo.model.DadosPaciente;
import br.com.healthgo.service.DadosPacienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
public class WebSocketHandler implements org.springframework.web.socket.WebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);
    
    private static final CopyOnWriteArraySet<WebSocketSession> sessoesWeb = new CopyOnWriteArraySet<>();
    private static final CopyOnWriteArraySet<WebSocketSession> sessoesDesktop = new CopyOnWriteArraySet<>();
    
    @Autowired
    private DadosPacienteService dadosPacienteService;
    
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userAgent = session.getHandshakeHeaders().getFirst("User-Agent");
        
        if (userAgent != null && userAgent.contains("Desktop")) {
            sessoesDesktop.add(session);
            logger.info("🖥️ Simulador Desktop conectado: {}", session.getId());
        } else {
            sessoesWeb.add(session);
            logger.info("🌐 Cliente Web conectado: {}", session.getId());
        }
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String payload = message.getPayload().toString();
        logger.info("📨 Mensagem recebida: {}", payload);
        
        try {
            // Deserializa os dados do paciente
            DadosPaciente dadosPaciente = objectMapper.readValue(payload, DadosPaciente.class);
            
            // Salva no banco de dados
            dadosPacienteService.salvarDados(dadosPaciente);
            
            // Broadcast para todos os clientes web
            broadcastParaClientesWeb(dadosPaciente);
            
            logger.info("✅ Dados do paciente {} processados e enviados", dadosPaciente.getPacienteId());
            
        } catch (Exception e) {
            logger.error("❌ Erro ao processar mensagem: {}", e.getMessage());
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        logger.error("❌ Erro de transporte na sessão {}: {}", session.getId(), exception.getMessage());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        sessoesWeb.remove(session);
        sessoesDesktop.remove(session);
        logger.info("🔌 Sessão desconectada: {} - Status: {}", session.getId(), closeStatus);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    // Envia dados para todos os clientes web
    private void broadcastParaClientesWeb(DadosPaciente dadosPaciente) {
        try {
            String jsonDados = objectMapper.writeValueAsString(dadosPaciente);
            TextMessage mensagem = new TextMessage(jsonDados);
            
            for (WebSocketSession sessao : sessoesWeb) {
                if (sessao.isOpen()) {
                    sessao.sendMessage(mensagem);
                }
            }
            
            logger.info("📡 Dados enviados para {} clientes web", sessoesWeb.size());
            
        } catch (IOException e) {
            logger.error("❌ Erro ao enviar dados para clientes web: {}", e.getMessage());
        }
    }

    // Método para enviar mensagem específica para um cliente
    public void enviarMensagemParaCliente(WebSocketSession session, String mensagem) {
        try {
            session.sendMessage(new TextMessage(mensagem));
        } catch (IOException e) {
            logger.error("❌ Erro ao enviar mensagem para cliente: {}", e.getMessage());
        }
    }

    // Retorna o número de clientes conectados
    public int getNumeroClientesWeb() {
        return sessoesWeb.size();
    }

    public int getNumeroClientesDesktop() {
        return sessoesDesktop.size();
    }
} 