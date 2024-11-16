package com.example.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.DAOs.FuncionarioDAO;

import javax.sql.DataSource;
import java.util.List;

@Controller
public class FuncionarioController {
    private final FuncionarioDAO funcionarioDAO;

    public FuncionarioController(DataSource dataSource) {
        this.funcionarioDAO = new FuncionarioDAO(dataSource);
    }

    @GetMapping("/funcionarios")
    public String listarFuncionarios(
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "tipo", required = false) String tipo,
            Model model) {

        List<Object[]> funcionarios;

        if ((nome != null && !nome.isEmpty()) || (tipo != null && !tipo.isEmpty())) {
            // Filtrar por nome e/ou tipo
            funcionarios = funcionarioDAO.buscarFuncionariosPorNomeETipo(nome, tipo);
        } else {
            // Buscar todos os funcionários
            funcionarios = funcionarioDAO.buscarTodosFuncionarios();
        }

        // Adiciona os dados ao modelo
        model.addAttribute("funcionarios", funcionarios);

        return "funcionarios";
    }

}
