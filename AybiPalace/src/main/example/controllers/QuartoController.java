package com.example.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.daos.QuartoDAO;
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
    public String listarQuartos(@RequestParam(value = "filtro", required = false) String filtro, Model model) {
        List<Object[]> quartosComDisponibilidade = quartoDAO.buscarQuartosComDisponibilidade();

        if ("disponivel".equalsIgnoreCase(filtro)) {
            quartosComDisponibilidade.removeIf(quarto -> !"Disponível".equalsIgnoreCase((String) quarto[3]));
        } else if ("indisponivel".equalsIgnoreCase(filtro)) {
            quartosComDisponibilidade.removeIf(quarto -> !"Indisponível".equalsIgnoreCase((String) quarto[3]));
        }

        model.addAttribute("quartos", quartosComDisponibilidade);
        model.addAttribute("filtro", filtro != null ? filtro.trim() : "");
        return "quartos";
    }




    @GetMapping("/quarto/{numero}")
    public String detalhesQuarto(@PathVariable int numero, Model model) {
        // Busca os detalhes do quarto
        Quarto quarto = quartoDAO.buscarQuartoPorNumero(numero);

        // Determina a disponibilidade do quarto
        String disponibilidade = "Disponível";
        List<Object[]> quartosComDisponibilidade = quartoDAO.buscarQuartosComDisponibilidade();
        for (Object[] q : quartosComDisponibilidade) {
            if ((int) q[0] == numero) { // Verifica se o número do quarto corresponde
                disponibilidade = (String) q[3]; // Pega a disponibilidade
                break;
            }
        }

        // Busca o histórico de limpeza do quarto
        List<Object[]> historicoLimpeza = quartoDAO.buscarHistoricoLimpezaPorQuarto(numero);

        // Busca os clientes que alugaram o quarto
        List<Object[]> clientes = quartoDAO.buscarClientesPorQuarto(numero);

        // Adiciona os dados ao modelo
        model.addAttribute("quarto", quarto);
        model.addAttribute("disponibilidade", disponibilidade);
        model.addAttribute("historicoLimpeza", historicoLimpeza);
        model.addAttribute("clientes", clientes);

        return "detalhesQuarto";
    }




    
}
