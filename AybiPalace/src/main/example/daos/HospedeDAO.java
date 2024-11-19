package com.example.daos;

import javax.sql.DataSource;

import com.example.entities.Dependente;
import com.example.entities.Hospede;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HospedeDAO {

    private final DataSource dataSource;

    public HospedeDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Hospede> buscarTodosHospedes() {
        String sql = "SELECT * FROM Hospede";
        List<Hospede> hospedes = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Hospede hospede = new Hospede(
                        resultSet.getString("cpf"),
                        resultSet.getString("nome"),
                        resultSet.getInt("idade"),
                        resultSet.getString("cep"),
                        resultSet.getString("rua"),
                        resultSet.getString("bairro"),
                        resultSet.getInt("numero"),
                        resultSet.getString("email")
                );
                hospedes.add(hospede);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hospedes;
    }
    
    public List<Object[]> buscarHospedesPorNome(String nome) {
        String sql = """
                SELECT h.cpf, h.nome,
                       CASE
                           WHEN EXISTS (
                               SELECT 1
                               FROM Alugou a
                               WHERE a.Fk_Hospede_cpf = h.cpf
                               AND a.pago = 0
                           )
                           THEN 'Pendente'
                           ELSE 'Realizado'
                       END AS status_pagamento
                FROM Hospede h
                WHERE h.nome LIKE ?
                """;

        List<Object[]> hospedes = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, "%" + nome + "%");

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Object[] hospede = new Object[]{
                            resultSet.getString("cpf"),           // CPF
                            resultSet.getString("nome"),          // Nome
                            resultSet.getString("status_pagamento") // Pagamento
                    };
                    hospedes.add(hospede);
                    System.out.println("CPF: " + hospede[0] + ", Nome: " + hospede[1] + ", Pagamento: " + hospede[2]);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hospedes;
    }


    
    public Hospede buscarHospedePorCpf(String cpf) {
        String sql = "SELECT * FROM Hospede WHERE cpf = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, cpf);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Hospede(
                            resultSet.getString("cpf"),
                            resultSet.getString("nome"),
                            resultSet.getInt("idade"),
                            resultSet.getString("cep"),
                            resultSet.getString("rua"),
                            resultSet.getString("bairro"),
                            resultSet.getInt("numero"),
                            resultSet.getString("email")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Object[]> buscarQuartosAlugadosPorHospede(String cpfHospede) {
        String sql = """
                SELECT 
                    a.Fk_Quarto_numero AS numeroQuarto,
                    a.data_checkin AS checkin,
                    a.data_checkOut AS checkout,
                    a.pago AS pagamento
                FROM Alugou a
                WHERE a.Fk_Hospede_cpf = ?
                ORDER BY a.data_checkin DESC;
                """;

        List<Object[]> quartosAlugados = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, cpfHospede);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    quartosAlugados.add(new Object[]{
                            resultSet.getInt("numeroQuarto"),       // Número do quarto
                            resultSet.getDate("checkin"),          // Data de check-in
                            resultSet.getDate("checkout"),         // Data de check-out
                            resultSet.getInt("pagamento")          // Status do pagamento
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return quartosAlugados;
    }


    
    public int contarReservasPorHospede(String cpfHospede) {
        String sql = "SELECT COUNT(*) AS total_reservas FROM Alugou WHERE Fk_Hospede_cpf = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, cpfHospede);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("total_reservas"); 
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; 
    }

    public List<Object[]> buscarHospedesComPagamento() {
        String sql = """
                SELECT h.cpf, h.nome, 
                       CASE
                           WHEN EXISTS (
                               SELECT 1
                               FROM Alugou a
                               WHERE a.Fk_Hospede_cpf = h.cpf
                               AND a.pago = 0
                           )
                           THEN 'Pendente'
                           ELSE 'Realizado'
                       END AS status_pagamento
                FROM Hospede h
                """;

        List<Object[]> hospedes = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                hospedes.add(new Object[]{
                        resultSet.getString("cpf"),           
                        resultSet.getString("nome"),          
                        resultSet.getString("status_pagamento") 
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hospedes;
    }

    public List<Object[]> buscarHospedesComPagamentoPendente() {
        String sql = """
                SELECT h.cpf, h.nome,
                       'Pendente' AS status_pagamento
                FROM Hospede h
                WHERE EXISTS (
                    SELECT 1
                    FROM Alugou a
                    WHERE a.Fk_Hospede_cpf = h.cpf
                    AND a.pago = 0
                )
                """;

        List<Object[]> hospedes = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                hospedes.add(new Object[]{
                        resultSet.getString("cpf"),           
                        resultSet.getString("nome"),          
                        resultSet.getString("status_pagamento") 
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hospedes;
    }
    
    public List<Object[]> buscarQuartosComServicosEBaresPorHospede(String cpfHospede) {
        String sql = """
                SELECT 
                    a.Fk_Quarto_numero AS numeroQuarto,
                    a.data_checkin AS checkin,
                    a.data_checkOut AS checkout,
                    a.pago AS pagamento,
                    m.data AS dataServico,
                    i.nome AS instrutor,
                    s.Servico_TIPO AS tipoServico,
                    p.valor AS tarifaBar
                FROM Alugou a
                LEFT JOIN ministra_servico_instrutor_hospede m 
                    ON m.Fk_Hospede_cpf = a.Fk_Hospede_cpf
                    AND m.data >= a.data_checkin 
                    AND m.data <= a.data_checkOut
                LEFT JOIN Instrutor i ON m.Fk_Instrutor_cpf = i.cpf
                LEFT JOIN Servico s ON m.Fk_Servico_codigo = s.codigo
                LEFT JOIN pedido_bartender_hospede_servico p 
                    ON p.Fk_Hospede_cpf = a.Fk_Hospede_cpf
                    AND p.data >= a.data_checkin 
                    AND p.data <= a.data_checkOut
                WHERE a.Fk_Hospede_cpf = ?
                ORDER BY a.data_checkin DESC, m.data, p.data;
                """;

        List<Object[]> quartosComServicosEBares = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, cpfHospede);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    quartosComServicosEBares.add(new Object[]{
                            resultSet.getInt("numeroQuarto"),       // Número do quarto
                            resultSet.getDate("checkin"),          // Data de check-in
                            resultSet.getDate("checkout"),         // Data de check-out
                            resultSet.getInt("pagamento"),         // Status do pagamento
                            resultSet.getDate("dataServico"),      // Data do serviço
                            resultSet.getString("instrutor"),      // Nome do instrutor
                            resultSet.getInt("tipoServico"),       // Tipo do serviço
                            resultSet.getDouble("tarifaBar")       // Tarifa do bar como Double
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return quartosComServicosEBares;
    }

    public List<Dependente> buscarDependentesPorHospede(String cpfHospede) {
        String sql = """
                SELECT d.cpf, d.nome, d.idade, d.Fk_Hospede_cpf
                FROM Dependente d
                WHERE d.Fk_Hospede_cpf = ?
                """;

        List<Dependente> dependentes = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, cpfHospede);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    dependentes.add(new Dependente(
                            resultSet.getString("cpf"),
                            resultSet.getString("nome"),
                            resultSet.getInt("idade"),
                            resultSet.getString("Fk_Hospede_cpf")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dependentes;
    }



}