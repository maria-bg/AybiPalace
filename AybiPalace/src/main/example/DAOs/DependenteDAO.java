package com.example.DAOs;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.example.entities.Dependente;
import com.example.entities.Hospede;

public class DependenteDAO {
    private final DataSource dataSource;

    public DependenteDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // Buscar os detalhes do Dependente pelo CPF
    public Dependente buscarPorCpf(String cpf) {
        String sql = "SELECT * FROM Dependente WHERE cpf = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, cpf);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Dependente(
                            resultSet.getString("cpf"),
                            resultSet.getString("nome"),
                            resultSet.getInt("idade"),
                            resultSet.getString("Fk_Hospede_cpf")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Retorna null se não encontrar o dependente
    }

    // Buscar os hóspedes associados ao Dependente
    public List<Hospede> buscarHospedesPorDependente(String cpfDependente) {
        String sql = """
                SELECT h.cpf, h.nome, h.idade, h.cep, h.rua, h.bairro, h.numero, h.email
                FROM Hospede h
                JOIN Dependente d ON d.Fk_Hospede_cpf = h.cpf
                WHERE d.cpf = ?
                """;

        List<Hospede> hospedes = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, cpfDependente);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    hospedes.add(new Hospede(
                            resultSet.getString("cpf"),
                            resultSet.getString("nome"),
                            resultSet.getInt("idade"),
                            resultSet.getString("cep"),
                            resultSet.getString("rua"),
                            resultSet.getString("bairro"),
                            resultSet.getInt("numero"),
                            resultSet.getString("email")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hospedes;
    }
}
