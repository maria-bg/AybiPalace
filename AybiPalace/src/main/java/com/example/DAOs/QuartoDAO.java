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

}
