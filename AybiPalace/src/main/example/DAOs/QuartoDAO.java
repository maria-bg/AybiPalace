package com.example.DAOs;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.example.entities.Quarto;

public class QuartoDAO {

    private final DataSource dataSource;

    public QuartoDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Object[]> buscarQuartosComDisponibilidade() {
        String sql = """
                SELECT q.numero, q.tarifa, q.camas,
                       CASE
                           WHEN EXISTS (
                               SELECT *
                               FROM Alugou a
                               WHERE a.Fk_Quarto_numero = q.numero
                               AND CURRENT_DATE BETWEEN a.data_checkin AND a.data_checkOut
                           )
                           THEN 'Indisponível'
                           ELSE 'Disponível'
                       END AS disponibilidade
                FROM Quarto q
                """;

        List<Object[]> quartosComDisponibilidade = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Object[] quarto = new Object[]{
                        resultSet.getInt("numero"),
                        resultSet.getDouble("tarifa"),       
                        resultSet.getInt("camas"),           
                        resultSet.getString("disponibilidade")
                };
                quartosComDisponibilidade.add(quarto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return quartosComDisponibilidade;
    }
    
    public Quarto buscarQuartoPorNumero(int numero) {
        String sql = "SELECT * FROM Quarto WHERE numero = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, numero);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Quarto(
                            resultSet.getInt("numero"),
                            resultSet.getDouble("tarifa"),
                            resultSet.getInt("camas")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    
    public List<Object[]> buscarHistoricoLimpezaPorQuarto(int numeroQuarto) {
        String sql = """
                SELECT f.nome AS faxineiro, l.DataHoraLimpeza AS dataHora, f.cpf AS cpfFaxineiro
                FROM Limpa l
                JOIN Faxineiro f ON l.Fk_Faxineiro_cpf = f.cpf
                WHERE l.Fk_Quarto_numero = ?
                ORDER BY l.DataHoraLimpeza DESC
                """;

        List<Object[]> historicoLimpeza = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, numeroQuarto);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    historicoLimpeza.add(new Object[]{
                            resultSet.getString("faxineiro"),    // Nome do Faxineiro
                            resultSet.getTimestamp("dataHora"), // Data e Hora da Limpeza
                            resultSet.getString("cpfFaxineiro") // CPF do Faxineiro
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return historicoLimpeza;
    }

    public List<Object[]> buscarClientesPorQuarto(int numeroQuarto) {
        String sql = """
                SELECT h.nome AS hospede, a.data_checkin AS dataCheckIn, 
                       a.data_checkOut AS dataCheckOut,
                       CASE
                           WHEN a.pago = 1 THEN 'Realizado'
                           ELSE 'Pendente'
                       END AS statusPagamento
                FROM Alugou a
                JOIN Hospede h ON a.Fk_Hospede_cpf = h.cpf
                WHERE a.Fk_Quarto_numero = ?
                ORDER BY a.data_checkin DESC
                """;

        List<Object[]> clientes = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, numeroQuarto);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    clientes.add(new Object[]{
                            resultSet.getString("hospede"),       
                            resultSet.getDate("dataCheckIn"),    
                            resultSet.getDate("dataCheckOut"),  
                            resultSet.getString("statusPagamento") 
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clientes;
    }


}
