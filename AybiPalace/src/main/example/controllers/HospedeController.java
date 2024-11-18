package com.example.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.DAOs.HospedeDAO;
import com.example.entities.Dependente;
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
        List<Object[]> hospedes;

        if (nome != null && !nome.isEmpty()) {
            // Busca por nome
            hospedes = hospedeDAO.buscarHospedesPorNome(nome);
            model.addAttribute("buscando", true);
            model.addAttribute("filtrandoPendentes", false);
        } else if (pendente != null && pendente) {
            // Filtra por pagamentos pendentes
            hospedes = hospedeDAO.buscarHospedesComPagamentoPendente();
            model.addAttribute("buscando", false);
            model.addAttribute("filtrandoPendentes", true);
        } else {
            // Exibe todos os hóspedes
            hospedes = hospedeDAO.buscarHospedesComPagamento();
            model.addAttribute("buscando", false);
            model.addAttribute("filtrandoPendentes", false);
        }

        // Adiciona a lista de hóspedes ao modelo
        model.addAttribute("hospedes", hospedes);
        return "hospedes"; 
    }


    
    @GetMapping("/hospede/{cpf}")
    public String detalhesHospede(@PathVariable String cpf, Model model) {
        // Buscar o hóspede
        Hospede hospede = hospedeDAO.buscarHospedePorCpf(cpf);

        // Buscar os quartos alugados e serviços do hóspede
        List<Object[]> quartosComServicosEBares = hospedeDAO.buscarQuartosComServicosEBaresPorHospede(cpf);

        // Buscar os dependentes do hóspede
        List<Dependente> dependentes = hospedeDAO.buscarDependentesPorHospede(cpf);

        // Adicionar os dados ao modelo
        model.addAttribute("hospede", hospede);
        model.addAttribute("quartosComServicosEBares", quartosComServicosEBares);
        model.addAttribute("dependentes", dependentes);

        return "detalhesHospede";
    }





    


}
