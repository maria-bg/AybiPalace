package com.example.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.DAOs.InstrutorDAO;
import com.example.entities.Instrutor;

import javax.sql.DataSource;
import java.util.List;

@Controller
public class InstrutorController {
    private final InstrutorDAO instrutorDAO;

    public InstrutorController(DataSource dataSource) {
        // Instanciando o DAO manualmente com o DataSource
        this.instrutorDAO = new InstrutorDAO(dataSource);
    }

    @GetMapping("/instrutor/{cpf}")
    public String detalhesInstrutor(@PathVariable String cpf, Model model) {
        // Busca os detalhes do instrutor
        Instrutor instrutor = instrutorDAO.buscarPorCpf(cpf);

        // Busca as pessoas instruídas pelo instrutor
        List<Object[]> pessoasInstruidas = instrutorDAO.buscarPessoasInstruidasPorInstrutor(cpf);

        // Adiciona os dados ao modelo
        model.addAttribute("instrutor", instrutor);
        model.addAttribute("pessoasInstruidas", pessoasInstruidas);

        return "detalhesInstrutor";
    }
}
