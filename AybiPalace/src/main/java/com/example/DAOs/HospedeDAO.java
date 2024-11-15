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
    
    public List<Hospede> buscarHospedesPorNome(String nome) {
        String sql = "SELECT * FROM Hospede WHERE nome LIKE ?";
        List<Hospede> hospedes = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, "%" + nome + "%");
            try (ResultSet resultSet = statement.executeQuery()) {
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
        String sql = "SELECT Fk_Quarto_numero, data_checkin, data_checkOut " +
                     "FROM Alugou " +
                     "WHERE Fk_Hospede_cpf = ? " +
                     "ORDER BY data_checkin DESC";

        List<Object[]> quartosAlugados = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, cpfHospede);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    quartosAlugados.add(new Object[]{
                            resultSet.getInt("Fk_Quarto_numero"), 
                            resultSet.getDate("data_checkin"),   
                            resultSet.getDate("data_checkOut")   
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
                        resultSet.getString("cpf"),           // CPF
                        resultSet.getString("nome"),          // Nome
                        resultSet.getString("status_pagamento") // Pagamento
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hospedes;
    }


}
