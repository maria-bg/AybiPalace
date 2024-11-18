package com.example.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.DAOs.DependenteDAO;
import com.example.entities.Dependente;
import com.example.entities.Hospede;

import javax.sql.DataSource;
import java.util.List;

@Controller
public class DependenteController {
    private final DependenteDAO DependenteDAO;

    public DependenteController(DataSource dataSource) {
        this.DependenteDAO = new DependenteDAO(dataSource);
    }

    @GetMapping("/dependente/{cpf}")
    public String detalhesDependente(@PathVariable String cpf, Model model) {
        // Buscar o dependente
        Dependente dependente = DependenteDAO.buscarPorCpf(cpf);

        // Buscar os hóspedes dos quais ele depende
        List<Hospede> hospedes = DependenteDAO.buscarHospedesPorDependente(cpf);

        // Adicionar os dados ao modelo
        model.addAttribute("dependente", dependente);
        model.addAttribute("hospedes", hospedes);

        return "detalhesDependente";
    }
}
