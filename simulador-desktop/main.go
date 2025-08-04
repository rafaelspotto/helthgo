package main

import (
	"encoding/csv"
	"encoding/json"
	"fmt"
	"log"
	"os"
	"strconv"
	"time"

	"github.com/gorilla/websocket"
)

// DadosPaciente representa os dados vitais de um paciente
type DadosPaciente struct {
	Timestamp    string  `json:"timestamp"`
	PacienteID   string  `json:"paciente_id"`
	PacienteNome string  `json:"paciente_nome"`
	PacienteCPF  string  `json:"paciente_cpf"`
	FreqCardiaca int     `json:"freq_cardiaca"`
	SaturacaoO2  int     `json:"saturacao_o2"`
	PressaoSist  int     `json:"pressao_sistolica"`
	PressaoDiast int     `json:"pressao_diastolica"`
	Temperatura  float64 `json:"temperatura"`
	FreqResp     int     `json:"freq_respiratoria"`
	Status       string  `json:"status"`
}

// ConfiguracaoSimulador contém as configurações do simulador
type ConfiguracaoSimulador struct {
	URLBackend     string
	URLWebSocket   string
	IntervaloEnvio time.Duration
}

func main() {
	log.Println("🏥 Iniciando Simulador HealthGo Desktop...")

	config := ConfiguracaoSimulador{
		URLBackend:     "http://localhost:8080",
		URLWebSocket:   "ws://localhost:8080/ws",
		IntervaloEnvio: 200 * time.Millisecond,
	}

	// Lista de arquivos CSV dos pacientes
	arquivosPacientes := []string{
		"dados/dados_pac001.csv",
		"dados/dados_pac002.csv",
		"dados/dados_pac003.csv",
	}

	// Inicia simulação para cada paciente em goroutines separadas
	for i, arquivo := range arquivosPacientes {
		go simularPaciente(arquivo, config, i+1)
	}

	// Mantém o programa rodando
	select {}
}

// simularPaciente simula o envio de dados de um paciente
func simularPaciente(arquivoCSV string, config ConfiguracaoSimulador, numeroPaciente int) {
	log.Printf("📊 Iniciando simulação do paciente %d: %s", numeroPaciente, arquivoCSV)

	// Conecta ao WebSocket
	conn, err := websocket.Dial(config.URLWebSocket, "", "")
	if err != nil {
		log.Printf("❌ Erro ao conectar WebSocket para paciente %d: %v", numeroPaciente, err)
		return
	}
	defer conn.Close()

	// Abre o arquivo CSV
	arquivo, err := os.Open(arquivoCSV)
	if err != nil {
		log.Printf("❌ Erro ao abrir arquivo %s: %v", arquivoCSV, err)
		return
	}
	defer arquivo.Close()

	// Lê o CSV
	leitor := csv.NewReader(arquivo)
	registros, err := leitor.ReadAll()
	if err != nil {
		log.Printf("❌ Erro ao ler CSV %s: %v", arquivoCSV, err)
		return
	}

	// Pula o cabeçalho se existir
	inicio := 0
	if len(registros) > 0 && registros[0][0] == "timestamp" {
		inicio = 1
	}

	log.Printf("📈 Enviando %d registros do paciente %d", len(registros)-inicio, numeroPaciente)

	// Processa cada linha do CSV
	for i, registro := range registros[inicio:] {
		dados := parsearDadosPaciente(registro)

		// Envia dados via WebSocket
		if err := enviarDadosWebSocket(conn, dados); err != nil {
			log.Printf("❌ Erro ao enviar dados do paciente %d: %v", numeroPaciente, err)
			continue
		}

		log.Printf("✅ Paciente %d - Registro %d/%d enviado: %s",
			numeroPaciente, i+1, len(registros)-inicio, dados.Timestamp)

		// Aguarda o intervalo configurado
		time.Sleep(config.IntervaloEnvio)
	}

	log.Printf("🎯 Simulação do paciente %d concluída", numeroPaciente)
}

// parsearDadosPaciente converte uma linha do CSV em DadosPaciente
func parsearDadosPaciente(registro []string) DadosPaciente {
	freqCardiaca, _ := strconv.Atoi(registro[4])
	saturacaoO2, _ := strconv.Atoi(registro[5])
	pressaoSist, _ := strconv.Atoi(registro[6])
	pressaoDiast, _ := strconv.Atoi(registro[7])
	temperatura, _ := strconv.ParseFloat(registro[8], 64)
	freqResp, _ := strconv.Atoi(registro[9])

	return DadosPaciente{
		Timestamp:    registro[0],
		PacienteID:   registro[1],
		PacienteNome: registro[2],
		PacienteCPF:  registro[3],
		FreqCardiaca: freqCardiaca,
		SaturacaoO2:  saturacaoO2,
		PressaoSist:  pressaoSist,
		PressaoDiast: pressaoDiast,
		Temperatura:  temperatura,
		FreqResp:     freqResp,
		Status:       registro[10],
	}
}

// enviarDadosWebSocket envia dados via WebSocket
func enviarDadosWebSocket(conn *websocket.Conn, dados DadosPaciente) error {
	jsonDados, err := json.Marshal(dados)
	if err != nil {
		return fmt.Errorf("erro ao serializar dados: %v", err)
	}

	err = conn.WriteMessage(websocket.TextMessage, jsonDados)
	if err != nil {
		return fmt.Errorf("erro ao enviar mensagem WebSocket: %v", err)
	}

	return nil
}
