import { DadosPaciente, Estatisticas } from '../types';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';
const WS_URL = process.env.REACT_APP_WS_URL || 'ws://localhost:8080/ws';

export class ApiService {
  private static async request<T>(
    endpoint: string,
    options: RequestInit = {}
  ): Promise<T> {
    const url = `${API_BASE_URL}${endpoint}`;
    const config: RequestInit = {
      headers: {
        'Content-Type': 'application/json',
        ...options.headers,
      },
      ...options,
    };

    try {
      const response = await fetch(url, config);
      
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      
      return await response.json();
    } catch (error) {
      console.error('API request failed:', error);
      throw error;
    }
  }

  static async getDadosRecentes(): Promise<DadosPaciente[]> {
    return this.request<DadosPaciente[]>('/api/pacientes/dados/recentes');
  }

  static async getDadosPorPaciente(pacienteId: string): Promise<DadosPaciente[]> {
    return this.request<DadosPaciente[]>(`/api/pacientes/${pacienteId}/dados`);
  }

  static async getEstatisticas(): Promise<Estatisticas> {
    return this.request<Estatisticas>('/api/pacientes/estatisticas');
  }

  static async healthCheck(): Promise<{ status: string }> {
    return this.request<{ status: string }>('/api/pacientes/health');
  }
}

export class WebSocketService {
  private ws: WebSocket | null = null;
  private reconnectAttempts = 0;
  private maxReconnectAttempts = 5;
  private reconnectDelay = 1000;

  constructor(private url: string = WS_URL) {}

  connect(onMessage: (data: DadosPaciente) => void, onError?: (error: Event) => void): void {
    try {
      this.ws = new WebSocket(this.url);
      
      this.ws.onopen = () => {
        console.log('WebSocket conectado');
        this.reconnectAttempts = 0;
      };

      this.ws.onmessage = (event) => {
        try {
          const data: DadosPaciente = JSON.parse(event.data);
          onMessage(data);
        } catch (error) {
          console.error('Erro ao processar mensagem WebSocket:', error);
        }
      };

      this.ws.onclose = () => {
        console.log('WebSocket desconectado');
        this.attemptReconnect(onMessage, onError);
      };

      this.ws.onerror = (error) => {
        console.error('Erro no WebSocket:', error);
        if (onError) onError(error);
      };
    } catch (error) {
      console.error('Erro ao conectar WebSocket:', error);
    }
  }

  private attemptReconnect(
    onMessage: (data: DadosPaciente) => void,
    onError?: (error: Event) => void
  ): void {
    if (this.reconnectAttempts < this.maxReconnectAttempts) {
      this.reconnectAttempts++;
      console.log(`Tentativa de reconexão ${this.reconnectAttempts}/${this.maxReconnectAttempts}`);
      
      setTimeout(() => {
        this.connect(onMessage, onError);
      }, this.reconnectDelay * this.reconnectAttempts);
    } else {
      console.error('Máximo de tentativas de reconexão atingido');
    }
  }

  disconnect(): void {
    if (this.ws) {
      this.ws.close();
      this.ws = null;
    }
  }

  isConnected(): boolean {
    return this.ws?.readyState === WebSocket.OPEN;
  }
}

export const formatTimestamp = (timestamp: string): string => {
  try {
    const date = new Date(timestamp);
    return date.toLocaleTimeString('pt-BR');
  } catch {
    return timestamp;
  }
};

export const formatStatus = (status: string): string => {
  return status === 'NORMAL' ? 'Normal' : 'Alerta';
};

export const getStatusColor = (status: string): string => {
  return status === 'NORMAL' ? 'success' : 'error';
}; 