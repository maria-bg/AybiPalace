package com.example.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
        List<Object[]> quartos = quartoDAO.buscarQuartosComDisponibilidade();

        // Adiciona os quartos ao modelo
        model.addAttribute("quartos", quartos);

        return "quartos"; // Nome do arquivo HTML
    }

    @GetMapping("/quarto/{numero}")
    public String detalhesQuarto(@PathVariable int numero, Model model) {
        // Busca os detalhes do quarto
        Quarto quarto = quartoDAO.buscarQuartoPorNumero(numero);

        // Busca o histórico de limpeza do quarto
        List<Object[]> historicoLimpeza = quartoDAO.buscarHistoricoLimpezaPorQuarto(numero);

        // Busca os clientes que alugaram o quarto
        List<Object[]> clientes = quartoDAO.buscarClientesPorQuarto(numero);

        // Adiciona os dados ao modelo
        model.addAttribute("quarto", quarto);
        model.addAttribute("historicoLimpeza", historicoLimpeza);
        model.addAttribute("clientes", clientes);

        return "detalhesQuarto";
    }



    
}
