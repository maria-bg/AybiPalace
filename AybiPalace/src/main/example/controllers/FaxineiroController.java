package com.example.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.daos.FaxineiroDAO;
import com.example.entities.Faxineiro;

import javax.sql.DataSource;
import java.util.List;

@Controller
public class FaxineiroController {

    private final FaxineiroDAO faxineiroDAO;

    public FaxineiroController(DataSource dataSource) {
        this.faxineiroDAO = new FaxineiroDAO(dataSource);
    }

    @GetMapping("/faxineiros")
    public String listarFaxineiros(Model model) {
        List<Faxineiro> faxineiros = faxineiroDAO.listarFaxineiros();
        model.addAttribute("faxineiros", faxineiros);
        return "faxineiros";
    }
    
    @GetMapping("/faxineiro/{cpf}")
    public String detalhesFaxineiro(@PathVariable String cpf, Model model) {
        Faxineiro faxineiro = faxineiroDAO.buscarPorCpf(cpf);
        List<Object[]> quartosLimpados = faxineiroDAO.buscarQuartosLimpadosComHorario(cpf);
        model.addAttribute("faxineiro", faxineiro);
        model.addAttribute("quartosLimpados", quartosLimpados);

        return "detalhesFaxineiro";
    }



}
