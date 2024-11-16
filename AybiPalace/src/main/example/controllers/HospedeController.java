package com.example.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.DAOs.HospedeDAO;
import com.example.entities.Hospede;

import javax.sql.DataSource;
import java.util.List;

@Controller
public class HospedeController {

    private final HospedeDAO hospedeDAO;

    public HospedeController(DataSource dataSource) {
        this.hospedeDAO = new HospedeDAO(dataSource);
    }

    @GetMapping("/hospedes")
    public String listarHospedes(
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "pendente", required = false) Boolean pendente,
            Model model) {
        if (nome != null && !nome.isEmpty()) {
            List<Object[]> hospedes = hospedeDAO.buscarHospedesPorNome(nome);
            model.addAttribute("hospedes", hospedes);
            model.addAttribute("buscando", true); 
        } else if (pendente != null && pendente) {
            List<Object[]> hospedes = hospedeDAO.buscarHospedesComPagamentoPendente();
            model.addAttribute("hospedes", hospedes);
            model.addAttribute("buscando", false);
            model.addAttribute("filtrandoPendentes", true); 
        } else {
            List<Object[]> hospedes = hospedeDAO.buscarHospedesComPagamento();
            model.addAttribute("hospedes", hospedes);
            model.addAttribute("buscando", false);
            model.addAttribute("filtrandoPendentes", false);
        }

        return "hospedes"; 
    }





    
    @GetMapping("/hospede/{cpf}")
    public String detalhesHospede(@PathVariable String cpf, Model model) {
        Hospede hospede = hospedeDAO.buscarHospedePorCpf(cpf);
        List<Object[]> quartosAlugados = hospedeDAO.buscarQuartosAlugadosPorHospede(cpf);

        model.addAttribute("hospede", hospede);
        model.addAttribute("quartosAlugados", quartosAlugados);

        return "detalhesHospede";
    }


    


}