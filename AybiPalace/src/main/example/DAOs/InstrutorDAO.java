package com.example.DAOs;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.example.entities.Instrutor;

public class InstrutorDAO {
    private final DataSource dataSource;

    public InstrutorDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // Buscar os detalhes do Instrutor pelo CPF
    public Instrutor buscarPorCpf(String cpf) {
        String sql = "SELECT * FROM Instrutor WHERE cpf = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, cpf);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Instrutor(
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

        return null; // Retorna null se não encontrar o instrutor
    }

    // Buscar as pessoas instruídas pelo Instrutor e a data
    public List<Object[]> buscarPessoasInstruidasPorInstrutor(String cpfInstrutor) {
        String sql = """
                SELECT h.nome AS hospede, m.data AS data
                FROM ministra_servico_instrutor_hospede m
                JOIN Hospede h ON m.Fk_Hospede_cpf = h.cpf
                WHERE m.Fk_Instrutor_cpf = ?
                ORDER BY m.data DESC
                """;

        List<Object[]> pessoasInstruidas = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, cpfInstrutor);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    pessoasInstruidas.add(new Object[]{
                            resultSet.getString("hospede"), // Nome do Hóspede
                            resultSet.getDate("data")      // Data da Instrução
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pessoasInstruidas;
    }
}
