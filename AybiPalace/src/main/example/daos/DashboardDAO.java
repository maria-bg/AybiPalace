package com.example.daos;

import javax.sql.DataSource;
import java.sql.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class DashboardDAO {
    private final DataSource dataSource;

    public DashboardDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public double calcularMediaTempoHospedes() {
        String sql = """
                SELECT AVG(DATEDIFF(a.data_checkOut, a.data_checkin)) AS media_tempo
                FROM Alugou a
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getDouble("media_tempo");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0.0;
    }

    public Map<String, Object> buscarQuartoMaisReservado() {
        String sql = """
                SELECT a.Fk_Quarto_numero AS numero, COUNT(*) AS total_reservas
                FROM Alugou a
                GROUP BY a.Fk_Quarto_numero
                ORDER BY total_reservas DESC
                LIMIT 1
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                Map<String, Object> result = new HashMap<>();
                result.put("numero", resultSet.getInt("numero"));
                result.put("total_reservas", resultSet.getInt("total_reservas"));
                return result;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Map<String, Object> buscarQuartoMenosReservado() {
        String sql = """
                SELECT a.Fk_Quarto_numero AS numero, COUNT(*) AS total_reservas
                FROM Alugou a
                GROUP BY a.Fk_Quarto_numero
                ORDER BY total_reservas ASC
                LIMIT 1
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                Map<String, Object> result = new HashMap<>();
                result.put("numero", resultSet.getInt("numero"));
                result.put("total_reservas", resultSet.getInt("total_reservas"));
                return result;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Map<String, Object> buscarServicoMaisUtilizado() {
        String sql = """
                SELECT s.Servico_TIPO AS tipo_servico, COUNT(*) AS total_utilizacoes
                FROM ministra_servico_instrutor_hospede m
                JOIN Servico s ON m.Fk_Servico_codigo = s.codigo
                GROUP BY s.Servico_TIPO
                ORDER BY total_utilizacoes DESC
                LIMIT 1
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                Map<String, Object> result = new HashMap<>();
                result.put("tipo_servico", resultSet.getInt("tipo_servico"));
                result.put("total_utilizacoes", resultSet.getInt("total_utilizacoes"));
                return result;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    
    public Map<String, Integer> contarReservasPorTipoDeQuarto() {
        String sql = """
                SELECT
                    CASE
                        WHEN q.numero BETWEEN 1 AND 200 THEN 'Toby'
                        WHEN q.numero BETWEEN 201 AND 300 THEN 'Pipoca'
                        WHEN q.numero BETWEEN 301 AND 350 THEN 'Ayla'
                        ELSE 'Outro'
                    END AS tipo_quarto,
                    COUNT(*) AS total_reservas
                FROM Alugou a
                JOIN Quarto q ON a.Fk_Quarto_numero = q.numero
                GROUP BY tipo_quarto
                """;

        Map<String, Integer> reservasPorTipo = new HashMap<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                reservasPorTipo.put(
                        resultSet.getString("tipo_quarto"),
                        resultSet.getInt("total_reservas")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservasPorTipo;
    }
    
    public Map<String, Integer> contarReservasPorMes() {
        String sql = """
                SELECT MONTH(data_checkin) AS mes, MONTHNAME(data_checkin) AS mes_nome, COUNT(*) AS total_reservas
                FROM Alugou
                GROUP BY MONTH(data_checkin), MONTHNAME(data_checkin)
                ORDER BY MONTH(data_checkin);
                """;

        // Mapa com todos os meses e valores iniciais como 0
        Map<String, Integer> reservasPorMes = new LinkedHashMap<>();
        reservasPorMes.put("January", 0);
        reservasPorMes.put("February", 0);
        reservasPorMes.put("March", 0);
        reservasPorMes.put("April", 0);
        reservasPorMes.put("May", 0);
        reservasPorMes.put("June", 0);
        reservasPorMes.put("July", 0);
        reservasPorMes.put("August", 0);
        reservasPorMes.put("September", 0);
        reservasPorMes.put("October", 0);
        reservasPorMes.put("November", 0);
        reservasPorMes.put("December", 0);

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                reservasPorMes.put(
                    resultSet.getString("mes_nome"),
                    resultSet.getInt("total_reservas")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservasPorMes;
    }



}