package com.example.daos;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class FuncionarioDAO {
    private final DataSource dataSource;

    public FuncionarioDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Object[]> buscarTodosFuncionarios() {
        String sql = """
                SELECT cpf, nome, 'Faxineiro' AS tipo, salario
                FROM Faxineiro
                UNION
                SELECT cpf, nome, 'Instrutor' AS tipo, salario
                FROM Instrutor
                UNION
                SELECT cpf, nome, 'Bartender' AS tipo, salario
                FROM Bartender
                ORDER BY nome;
                """;

        List<Object[]> funcionarios = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                funcionarios.add(new Object[]{
                        resultSet.getString("cpf"),    // CPF
                        resultSet.getString("nome"),   // Nome
                        resultSet.getString("tipo"),   // Tipo de Funcionário
                        resultSet.getDouble("salario") // Salário
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return funcionarios;
    }
    
    public List<Object[]> buscarFuncionariosPorNome(String nome) {
        String sql = """
                SELECT cpf, nome, 'Faxineiro' AS tipo, salario
                FROM Faxineiro
                WHERE nome LIKE ?
                UNION
                SELECT cpf, nome, 'Instrutor' AS tipo, salario
                FROM Instrutor
                WHERE nome LIKE ?
                UNION
                SELECT cpf, nome, 'Bartender' AS tipo, salario
                FROM Bartender
                WHERE nome LIKE ?
                ORDER BY nome;
                """;

        List<Object[]> funcionarios = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            String likeName = "%" + nome + "%";
            statement.setString(1, likeName);
            statement.setString(2, likeName);
            statement.setString(3, likeName);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    funcionarios.add(new Object[]{
                            resultSet.getString("cpf"),    // CPF
                            resultSet.getString("nome"),   // Nome
                            resultSet.getString("tipo"),   // Tipo de Funcionário
                            resultSet.getDouble("salario") // Salário
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return funcionarios;
    }
    
    public List<Object[]> buscarFuncionariosPorNomeETipo(String nome, String tipo) {
        String sql = """
                SELECT cpf, nome, 'Faxineiro' AS tipo, salario
                FROM Faxineiro
                WHERE (? IS NULL OR nome LIKE ?) AND (? IS NULL OR 'Faxineiro' = ?)
                UNION
                SELECT cpf, nome, 'Instrutor' AS tipo, salario
                FROM Instrutor
                WHERE (? IS NULL OR nome LIKE ?) AND (? IS NULL OR 'Instrutor' = ?)
                UNION
                SELECT cpf, nome, 'Bartender' AS tipo, salario
                FROM Bartender
                WHERE (? IS NULL OR nome LIKE ?) AND (? IS NULL OR 'Bartender' = ?)
                ORDER BY nome;
                """;

        List<Object[]> funcionarios = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            String likeName = (nome != null && !nome.isEmpty()) ? "%" + nome + "%" : null;

            // Preenche os parâmetros do filtro
            for (int i = 1; i <= 12; i += 4) {
                statement.setString(i, likeName);          // Nome (LIKE)
                statement.setString(i + 1, likeName);      // Nome (LIKE)
                statement.setString(i + 2, tipo);          // Tipo
                statement.setString(i + 3, tipo);          // Tipo
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    funcionarios.add(new Object[]{
                            resultSet.getString("cpf"),    // CPF
                            resultSet.getString("nome"),   // Nome
                            resultSet.getString("tipo"),   // Tipo de Funcionário
                            resultSet.getDouble("salario") // Salário
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return funcionarios;
    }


}
