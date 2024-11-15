package com.example.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.DAOs.QuartoDAO;
import com.example.entities.Quarto;

import javax.sql.DataSource;
import java.util.List;

@Controller
public class QuartoController {

    private final QuartoDAO quartoDAO;

    public QuartoController(DataSource dataSource) {
        this.quartoDAO = new QuartoDAO(dataSource);
    }

    @GetMapping("/quartos")
    public String listarQuartos(Model model) {
        // Busca todos os quartos com disponibilidade
        List<Object[]> quartos = quartoDAO.buscarQuartosComDisponibilidade();

        // Adiciona os quartos ao modelo
        model.addAttribute("quartos", quartos);

        return "quartos"; // Nome do arquivo HTML
    }

}
