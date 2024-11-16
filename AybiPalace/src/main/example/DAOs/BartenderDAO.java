package com.example.DAOs;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.example.entities.Bartender;

public class BartenderDAO {
    private final DataSource dataSource;

    public BartenderDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // Buscar os detalhes do Bartender pelo CPF
    public Bartender buscarPorCpf(String cpf) {
        String sql = "SELECT * FROM Bartender WHERE cpf = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, cpf);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Bartender(
                            resultSet.getString("cpf"),
                            resultSet.getString("nome"),
                            resultSet.getInt("idade"),
                            resultSet.getString("cep"),
                            resultSet.getString("rua"),
                            resultSet.getString("bairro"),
                            resultSet.getInt("numero"),
                            resultSet.getString("email"),
                            resultSet.getDouble("salario")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Retorna null se não encontrar o bartender
    }

    // Buscar os pedidos atendidos pelo Bartender
    public List<Object[]> buscarPedidosPorBartender(String cpfBartender) {
        String sql = """
                SELECT h.nome AS hospede, p.data AS data, s.tarifa AS tarifa
                FROM pedido_bartender_hospede_servico p
                JOIN Hospede h ON p.Fk_Hospede_cpf = h.cpf
                JOIN Servico s ON p.Fk_Servico_codigo = s.codigo
                WHERE p.Fk_Bartender_cpf = ?
                ORDER BY p.data DESC
                """;

        List<Object[]> pedidos = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, cpfBartender);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    pedidos.add(new Object[]{
                            resultSet.getString("hospede"), // Nome do Hóspede
                            resultSet.getDate("data"),      // Data do Pedido
                            resultSet.getDouble("tarifa")  // Tarifa do Serviço
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pedidos;
    }
}
