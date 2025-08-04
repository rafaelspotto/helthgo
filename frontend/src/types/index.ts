export interface DadosPaciente {
  id: number;
  timestamp: string;
  pacienteId: string;
  pacienteNome: string;
  pacienteCpf: string;
  freqCardiaca: number;
  saturacaoO2: number;
  pressaoSistolica: number;
  pressaoDiastolica: number;
  temperatura: number;
  freqRespiratoria: number;
  status: 'NORMAL' | 'ALERTA';
  dataCriacao: string;
}

export interface Estatisticas {
  totalRegistros: number;
  registrosNormais: number;
  registrosAlertas: number;
}

export interface WebSocketMessage {
  type: 'data' | 'status' | 'error';
  payload: DadosPaciente | string;
}

export interface ChartData {
  labels: string[];
  datasets: {
    label: string;
    data: number[];
    borderColor: string;
    backgroundColor: string;
    tension: number;
  }[];
}

export interface ChartOptions {
  responsive: boolean;
  plugins: {
    legend: {
      position: 'top' | 'bottom' | 'left' | 'right';
    };
    title: {
      display: boolean;
      text: string;
    };
  };
  scales: {
    y: {
      beginAtZero: boolean;
    };
  };
} 