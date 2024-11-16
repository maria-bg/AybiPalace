package com.example.DAOs;

import javax.sql.DataSource;

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
                SELECT a.Fk_Quarto_numero AS numeroQuarto,
                       a.data_checkin AS dataCheckIn,
                       a.data_checkOut AS dataCheckOut,
                       CASE
                           WHEN a.pago = 1 THEN 'Realizado'
                           ELSE 'Pendente'
                       END AS statusPagamento
                FROM Alugou a
                WHERE a.Fk_Hospede_cpf = ?
                ORDER BY a.data_checkin DESC
                """;

        List<Object[]> quartosAlugados = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, cpfHospede);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Object[] aluguel = new Object[]{
                            resultSet.getInt("numeroQuarto"),      
                            resultSet.getDate("dataCheckIn"),      
                            resultSet.getDate("dataCheckOut"),     
                            resultSet.getString("statusPagamento") 
                    };
                    quartosAlugados.add(aluguel);
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


}
