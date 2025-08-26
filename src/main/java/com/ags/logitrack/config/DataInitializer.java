package com.ags.logitrack.config;

import com.ags.logitrack.model.EntregaSimulada;
import com.ags.logitrack.model.EventoSensorial;
import com.ags.logitrack.model.RoboLogistico;
import com.ags.logitrack.model.Usuario;
import com.ags.logitrack.repository.EntregaSimuladaRepository;
import com.ags.logitrack.repository.EventoSensorialRepository;
import com.ags.logitrack.repository.RoboLogisticoRepository;
import com.ags.logitrack.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoboLogisticoRepository roboLogisticoRepository;

    @Autowired
    private EventoSensorialRepository eventoSensorialRepository;

    @Autowired
    private EntregaSimuladaRepository entregaSimuladaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Random random = new Random();

    @Override
    public void run(String... args) {
        // Criar usuários padrão primeiro
        criarUsuariosPadrao();

        // Verifica se o banco de dados está vazio
        if (roboLogisticoRepository.count() == 0) {
            System.out.println("Inicializando banco de dados com dados de exemplo...");

            // Lista de robôs logísticos para inserção
            List<RoboLogistico> robos = Arrays.asList(
                    new RoboLogistico("RB-001", "TransportBot 2000", "ATIVO", 87.5, "Armazém A - Corredor 3"),
                    new RoboLogistico("RB-002", "CargoMaster X1", "ATIVO", 92.3, "Doca de Carregamento 2"),
                    new RoboLogistico("RB-003", "LogiDroid 3.0", "INATIVO", 12.8, "Estação de Recarga 1"),
                    new RoboLogistico("RB-004", "TransportBot 2000", "MANUTENÇÃO", 45.2, "Oficina Técnica"),
                    new RoboLogistico("RB-005", "CargoMaster X1", "ATIVO", 78.9, "Armazém B - Corredor 7"),
                    new RoboLogistico("RB-006", "LogiDroid 3.0", "ATIVO", 65.4, "Área de Separação"),
                    new RoboLogistico("RB-007", "HeavyLifter Pro", "ATIVO", 81.2, "Armazém C - Setor Pesado"),
                    new RoboLogistico("RB-008", "TransportBot 2000", "INATIVO", 5.3, "Estação de Recarga 3"),
                    new RoboLogistico("RB-009", "CargoMaster X2", "ATIVO", 94.7, "Doca de Expedição 1"),
                    new RoboLogistico("RB-010", "LogiDroid 3.0", "MANUTENÇÃO", 0.0, "Oficina Técnica"),
                    new RoboLogistico("RB-011", "HeavyLifter Pro", "ATIVO", 72.5, "Armazém A - Setor Pesado"),
                    new RoboLogistico("RB-012", "TransportBot 3000", "ATIVO", 88.6, "Armazém B - Corredor 2"),
                    new RoboLogistico("RB-013", "CargoMaster X2", "ATIVO", 67.8, "Área de Consolidação"),
                    new RoboLogistico("RB-014", "LogiDroid 4.0", "ATIVO", 91.3, "Armazém C - Corredor 5"),
                    new RoboLogistico("RB-015", "HeavyLifter Pro", "INATIVO", 23.1, "Estação de Recarga 2"),
                    new RoboLogistico("RB-016", "TransportBot 3000", "ATIVO", 82.9, "Armazém A - Corredor 8"),
                    new RoboLogistico("RB-017", "CargoMaster X2", "MANUTENÇÃO", 34.6, "Oficina Técnica"),
                    new RoboLogistico("RB-018", "LogiDroid 4.0", "ATIVO", 79.2, "Doca de Recebimento 3"),
                    new RoboLogistico("RB-019", "HeavyLifter Pro", "ATIVO", 86.7, "Armazém B - Setor Pesado"),
                    new RoboLogistico("RB-020", "TransportBot 3000", "ATIVO", 90.1, "Área de Picking"));

            // Salva todos os robôs no banco de dados
            List<RoboLogistico> robosSalvos = roboLogisticoRepository.saveAll(robos);

            System.out.println("Banco de dados inicializado com " + robos.size() + " robôs logísticos.");

            // Criar eventos sensoriais
            List<EventoSensorial> eventosSensoriais = new ArrayList<>();
            String[] tiposSensores = { "Proximidade", "Temperatura", "Umidade", "Movimento", "Peso", "Obstáculo",
                    "Luminosidade" };
            String[] statusSensor = { "NORMAL", "ALERTA", "CRÍTICO", "MANUTENÇÃO" };

            for (RoboLogistico robo : robosSalvos) {
                // Cada robô terá entre 3 e 8 eventos sensoriais
                int numEventos = 3 + random.nextInt(6);

                for (int i = 0; i < numEventos; i++) {
                    String tipoSensor = tiposSensores[random.nextInt(tiposSensores.length)];
                    String leitura = "";

                    // Gerar leituras específicas para cada tipo de sensor
                    switch (tipoSensor) {
                        case "Proximidade":
                            leitura = (random.nextInt(200) + 5) + " cm";
                            break;
                        case "Temperatura":
                            leitura = String.format("%.1f °C", (20 + random.nextDouble() * 30));
                            break;
                        case "Umidade":
                            leitura = (30 + random.nextInt(60)) + "%";
                            break;
                        case "Movimento":
                            leitura = random.nextBoolean() ? "Detectado" : "Não detectado";
                            break;
                        case "Peso":
                            leitura = (random.nextInt(500) + 1) + " kg";
                            break;
                        case "Obstáculo":
                            leitura = random.nextBoolean() ? "Detectado" : "Caminho livre";
                            break;
                        case "Luminosidade":
                            leitura = (random.nextInt(1000) + 50) + " lux";
                            break;
                    }

                    // Gerar data/hora aleatória nos últimos 7 dias
                    LocalDateTime dataHora = LocalDateTime.now().minusDays(random.nextInt(7))
                            .minusHours(random.nextInt(24))
                            .minusMinutes(random.nextInt(60));

                    // Status do sensor
                    String status = statusSensor[random.nextInt(statusSensor.length)];

                    EventoSensorial evento = new EventoSensorial(
                            robo,
                            tipoSensor,
                            leitura,
                            dataHora,
                            robo.getLocalizacao(),
                            status);

                    eventosSensoriais.add(evento);
                }
            }

            // Salvar eventos sensoriais
            eventoSensorialRepository.saveAll(eventosSensoriais);
            System.out.println("Banco de dados inicializado com " + eventosSensoriais.size() + " eventos sensoriais.");

            // Criar entregas simuladas
            List<EntregaSimulada> entregas = new ArrayList<>();
            String[] origens = { "Armazém A", "Armazém B", "Armazém C", "Doca de Recebimento", "Área de Consolidação" };
            String[] destinos = { "Área de Picking", "Doca de Expedição", "Área de Embalagem", "Área de Separação",
                    "Cliente Final" };
            String[] statusEntrega = { "PENDENTE", "EM_ANDAMENTO", "CONCLUÍDA", "CANCELADA", "ATRASADA" };

            // Apenas robôs ativos podem fazer entregas
            List<RoboLogistico> robosAtivos = robosSalvos.stream()
                    .filter(r -> "ATIVO".equals(r.getStatus()))
                    .toList();

            for (RoboLogistico robo : robosAtivos) {
                // Cada robô ativo terá entre 1 e 5 entregas
                int numEntregas = 1 + random.nextInt(5);

                for (int i = 0; i < numEntregas; i++) {
                    String origem = origens[random.nextInt(origens.length)];
                    String destino = destinos[random.nextInt(destinos.length)];

                    // Garantir que origem e destino sejam diferentes
                    while (origem.equals(destino)) {
                        destino = destinos[random.nextInt(destinos.length)];
                    }

                    // Gerar código de pedido
                    String codigoPedido = "PED-" + (10000 + random.nextInt(90000));

                    // Gerar datas de início e fim
                    LocalDateTime dataInicio = LocalDateTime.now().minusDays(random.nextInt(30))
                            .minusHours(random.nextInt(24));

                    String status = statusEntrega[random.nextInt(statusEntrega.length)];

                    // Se a entrega estiver concluída, terá data de fim
                    LocalDateTime dataFim = null;
                    if ("CONCLUÍDA".equals(status) || "CANCELADA".equals(status)) {
                        dataFim = dataInicio.plusHours(random.nextInt(5) + 1)
                                .plusMinutes(random.nextInt(60));
                    }

                    // Distância percorrida (apenas para entregas concluídas)
                    Double distancia = "CONCLUÍDA".equals(status) ? (random.nextDouble() * 10) + 0.5 : null;

                    // Observações
                    String observacoes = "";
                    if ("CANCELADA".equals(status)) {
                        String[] motivos = {
                                "Cliente cancelou o pedido",
                                "Produto indisponível",
                                "Problema técnico no robô",
                                "Rota bloqueada",
                                "Bateria insuficiente"
                        };
                        observacoes = motivos[random.nextInt(motivos.length)];
                    } else if ("ATRASADA".equals(status)) {
                        String[] motivos = {
                                "Tráfego intenso no armazém",
                                "Desvio de rota necessário",
                                "Aguardando liberação de acesso",
                                "Parada para recarga",
                                "Prioridade alterada"
                        };
                        observacoes = motivos[random.nextInt(motivos.length)];
                    } else if ("CONCLUÍDA".equals(status)) {
                        if (random.nextBoolean()) {
                            String[] comentarios = {
                                    "Entrega realizada com sucesso",
                                    "Cliente satisfeito",
                                    "Entrega antes do prazo",
                                    "Rota otimizada",
                                    "Sem intercorrências"
                            };
                            observacoes = comentarios[random.nextInt(comentarios.length)];
                        }
                    }

                    EntregaSimulada entrega = new EntregaSimulada(
                            robo,
                            codigoPedido,
                            origem,
                            destino,
                            dataInicio,
                            dataFim,
                            status,
                            distancia,
                            observacoes);

                    entregas.add(entrega);
                }
            }

            // Salvar entregas simuladas
            entregaSimuladaRepository.saveAll(entregas);
            System.out.println("Banco de dados inicializado com " + entregas.size() + " entregas simuladas.");

        } else {
            System.out.println("Banco de dados já contém dados. Pulando inicialização.");
        }
    }

    private void criarUsuariosPadrao() {
        // Criar usuário administrador padrão se não existir
        if (!usuarioRepository.existsByUsername("admin")) {
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setEmail("admin@logitrack.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setNomeCompleto("Administrador");
            admin.setTelefone("(11) 99999-9999");
            admin.setTipoUsuario(Usuario.TipoUsuario.ADMIN);
            admin.setAtivo(true);
            admin.setDataCriacao(LocalDateTime.now());

            usuarioRepository.save(admin);
            System.out.println("Usuário administrador criado: admin / admin123");
        }

        // Criar usuário padrão se não existir
        if (!usuarioRepository.existsByUsername("user")) {
            Usuario user = new Usuario();
            user.setUsername("user");
            user.setEmail("user@logitrack.com");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setNomeCompleto("Usuário Padrão");
            user.setTelefone("(11) 88888-8888");
            user.setTipoUsuario(Usuario.TipoUsuario.USUARIO);
            user.setAtivo(true);
            user.setDataCriacao(LocalDateTime.now());

            usuarioRepository.save(user);
            System.out.println("Usuário padrão criado: user / user123");
        }
    }
}
