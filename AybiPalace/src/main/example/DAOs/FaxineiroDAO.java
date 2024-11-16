package com.example.DAOs;

import javax.sql.DataSource;

import com.example.entities.Faxineiro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FaxineiroDAO {
    private final DataSource dataSource;

    public FaxineiroDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Faxineiro> listarFaxineiros() {
        String sql = "SELECT * FROM Faxineiro";
        List<Faxineiro> faxineiros = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Faxineiro faxineiro = new Faxineiro(
                        resultSet.getString("cpf"),
                        resultSet.getString("nome"),
                        resultSet.getInt("idade"),
                        resultSet.getString("cep"),
                        resultSet.getString("rua"),
                        resultSet.getString("bairro"),
                        resultSet.getInt("numero"),
                        resultSet.getString("email"),
                        resultSet.getFloat("salario"),
                        resultSet.getString("FaxineiroGerente")
                );
                faxineiros.add(faxineiro);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return faxineiros;
    }
    
    public Faxineiro buscarPorCpf(String cpf) {
        String sql = "SELECT * FROM Faxineiro WHERE cpf = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, cpf);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Faxineiro(
                            resultSet.getString("cpf"),
                            resultSet.getString("nome"),
                            resultSet.getInt("idade"),
                            resultSet.getString("cep"),
                            resultSet.getString("rua"),
                            resultSet.getString("bairro"),
                            resultSet.getInt("numero"),
                            resultSet.getString("email"),
                            resultSet.getFloat("salario"),
                            resultSet.getString("FaxineiroGerente")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Object[]> buscarQuartosLimpadosComHorario(String cpfFaxineiro) {
        String sql = "SELECT Fk_Quarto_numero, DataHoraLimpeza FROM Limpa WHERE Fk_Faxineiro_cpf = ?";
        List<Object[]> quartosLimpados = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, cpfFaxineiro);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int numeroQuarto = resultSet.getInt("Fk_Quarto_numero");
                    Timestamp dataHora = resultSet.getTimestamp("DataHoraLimpeza");
                    quartosLimpados.add(new Object[]{numeroQuarto, dataHora});
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quartosLimpados;
    }


}
