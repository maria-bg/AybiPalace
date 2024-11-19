package com.example.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.daos.BartenderDAO;
import com.example.entities.Bartender;

import javax.sql.DataSource;
import java.util.List;

@Controller
public class BartenderController {
    private final BartenderDAO bartenderDAO;

    public BartenderController(DataSource dataSource) {
        // Instanciando o DAO manualmente com o DataSource
        this.bartenderDAO = new BartenderDAO(dataSource);
    }

    @GetMapping("/bartender/{cpf}")
    public String detalhesBartender(@PathVariable String cpf, Model model) {
        // Busca os detalhes do bartender
        Bartender bartender = bartenderDAO.buscarPorCpf(cpf);

        // Busca os pedidos atendidos pelo bartender
        List<Object[]> pedidos = bartenderDAO.buscarPedidosPorBartender(cpf);

        // Adiciona os dados ao modelo
        model.addAttribute("bartender", bartender);
        model.addAttribute("pedidos", pedidos);

        return "detalhesBartender";
    }
}
