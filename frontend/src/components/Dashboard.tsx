import React, { useState, useEffect } from 'react';
import {
  Box,
  Grid,
  Card,
  CardContent,
  Typography,
  Chip,
  IconButton,
  AppBar,
  Toolbar,
  Container,
  Alert,
  Snackbar
} from '@mui/material';
import {
  Favorite,
  MonitorHeart,
  TrendingUp,
  Warning,
  Refresh,
  Visibility
} from '@mui/icons-material';
import { Line } from 'react-chartjs-2';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend
} from 'chart.js';

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend
);

interface DadosPaciente {
  id?: number;
  timestamp: string;
  paciente_id: string; // Mapeia para pacienteId
  paciente_nome: string; // Mapeia para pacienteNome
  paciente_cpf: string; // Mapeia para pacienteCpf
  freq_cardiaca: number; // Mapeia para freqCardiaca
  saturacao_o2: number; // Mapeia para saturacaoO2
  pressao_sistolica: number; // Mapeia para pressaoSistolica
  pressao_diastolica: number; // Mapeia para pressaoDiastolica
  temperatura: number;
  freq_respiratoria: number; // Mapeia para freqRespiratoria
  status: string;
  data_criacao?: string; // Mapeia para dataCriacao
}

const Dashboard: React.FC = () => {
  const [pacientes, setPacientes] = useState<DadosPaciente[]>([]);
  const [conectado, setConectado] = useState(false);
  const [mensagem, setMensagem] = useState('');
  const [tipoMensagem, setTipoMensagem] = useState<'success' | 'error'>('success');

  useEffect(() => {
    conectarWebSocket();
    carregarDadosRecentes();
    
    // Atualiza dados a cada 5 segundos
    const interval = setInterval(carregarDadosRecentes, 5000);
    
    return () => {
      clearInterval(interval);
    };
  }, []);

  const conectarWebSocket = () => {
    const ws = new WebSocket('ws://localhost:8080/ws');
    
    ws.onopen = () => {
      setConectado(true);
      mostrarMensagem('Conectado ao servidor em tempo real', 'success');
    };
    
    ws.onmessage = (event) => {
      const dados: DadosPaciente = JSON.parse(event.data);
      atualizarDadosPaciente(dados);
    };
    
    ws.onclose = () => {
      setConectado(false);
      mostrarMensagem('Desconectado do servidor', 'error');
    };
    
    ws.onerror = () => {
      setConectado(false);
      mostrarMensagem('Erro na conexão WebSocket', 'error');
    };
  };

  const carregarDadosRecentes = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/pacientes/dados/recentes');
      const dados = await response.json();
      setPacientes(dados);
    } catch (error) {
      console.error('Erro ao carregar dados:', error);
    }
  };

  const atualizarDadosPaciente = (novosDados: DadosPaciente) => {
    setPacientes(prev => {
      const index = prev.findIndex(p => p.paciente_id === novosDados.paciente_id);
      if (index >= 0) {
        const atualizados = [...prev];
        atualizados[index] = novosDados;
        return atualizados;
      } else {
        return [...prev, novosDados];
      }
    });
  };

  const mostrarMensagem = (texto: string, tipo: 'success' | 'error') => {
    setMensagem(texto);
    setTipoMensagem(tipo);
  };

  const obterCorStatus = (status: string) => {
    return status === 'NORMAL' ? 'success' : 'error';
  };

  const obterIconeStatus = (status: string) => {
    return status === 'NORMAL' ? <MonitorHeart /> : <Warning />;
  };

  const criarDadosGrafico = (paciente: DadosPaciente) => {
    return {
      labels: ['Freq. Cardíaca', 'Saturação O₂', 'Pressão Sist.', 'Pressão Diast.', 'Temperatura', 'Freq. Resp.'],
      datasets: [
        {
          label: 'Valores',
          data: [
            paciente.freq_cardiaca,
            paciente.saturacao_o2,
            paciente.pressao_sistolica,
            paciente.pressao_diastolica,
            paciente.temperatura * 10, // Multiplica por 10 para melhor visualização
            paciente.freq_respiratoria
          ],
          borderColor: 'rgb(75, 192, 192)',
          backgroundColor: 'rgba(75, 192, 192, 0.2)',
          tension: 0.1
        }
      ]
    };
  };

  const opcoesGrafico = {
    responsive: true,
    plugins: {
      legend: {
        position: 'top' as const,
      },
      title: {
        display: true,
        text: 'Sinais Vitais'
      }
    },
    scales: {
      y: {
        beginAtZero: true
      }
    }
  };

  return (
    <Box sx={{ flexGrow: 1 }}>
      <AppBar position="static">
        <Toolbar>
          <MonitorHeart sx={{ mr: 2 }} />
          <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
            HealthGo - Monitoramento Multiparamétrico
          </Typography>
          <Chip
            label={conectado ? 'Conectado' : 'Desconectado'}
            color={conectado ? 'success' : 'error'}
            size="small"
          />
          <IconButton color="inherit" onClick={carregarDadosRecentes}>
            <Refresh />
          </IconButton>
        </Toolbar>
      </AppBar>

      <Container maxWidth="xl" sx={{ mt: 4, mb: 4 }}>
        <Grid container spacing={3}>
          {pacientes.map((paciente) => (
            <Grid item xs={12} md={6} lg={4} key={paciente.paciente_id}>
              <Card>
                <CardContent>
                  <Box display="flex" justifyContent="space-between" alignItems="center" mb={2}>
                    <Typography variant="h6" component="div">
                      {paciente.paciente_nome}
                    </Typography>
                    <Chip
                      icon={obterIconeStatus(paciente.status)}
                      label={paciente.status}
                      color={obterCorStatus(paciente.status)}
                      size="small"
                    />
                  </Box>

                  <Grid container spacing={2} mb={2}>
                    <Grid item xs={6}>
                      <Box display="flex" alignItems="center">
                        <Favorite color="error" sx={{ mr: 1 }} />
                        <Typography variant="body2">
                          {paciente.freq_cardiaca} bpm
                        </Typography>
                      </Box>
                    </Grid>
                    <Grid item xs={6}>
                      <Box display="flex" alignItems="center">
                        <TrendingUp color="primary" sx={{ mr: 1 }} />
                        <Typography variant="body2">
                          {paciente.saturacao_o2}% O₂
                        </Typography>
                      </Box>
                    </Grid>
                    <Grid item xs={6}>
                      <Typography variant="body2">
                        Pressão: {paciente.pressao_sistolica}/{paciente.pressao_diastolica} mmHg
                      </Typography>
                    </Grid>
                    <Grid item xs={6}>
                      <Typography variant="body2">
                        Temp: {paciente.temperatura}°C
                      </Typography>
                    </Grid>
                  </Grid>

                  <Box height={200}>
                    <Line data={criarDadosGrafico(paciente)} options={opcoesGrafico} />
                  </Box>

                  <Typography variant="caption" color="text.secondary">
                    Última atualização: {new Date(paciente.timestamp).toLocaleTimeString()}
                  </Typography>
                </CardContent>
              </Card>
            </Grid>
          ))}
        </Grid>

        {pacientes.length === 0 && (
          <Box textAlign="center" mt={4}>
            <Typography variant="h6" color="text.secondary">
              Aguardando dados dos pacientes...
            </Typography>
          </Box>
        )}
      </Container>

      <Snackbar
        open={!!mensagem}
        autoHideDuration={6000}
        onClose={() => setMensagem('')}
      >
        <Alert severity={tipoMensagem} onClose={() => setMensagem('')}>
          {mensagem}
        </Alert>
      </Snackbar>
    </Box>
  );
};

export default Dashboard; 