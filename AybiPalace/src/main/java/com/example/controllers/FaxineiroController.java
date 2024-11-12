package com.example.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.entities.Faxineiro;
import com.example.repositories.FaxineiroRepository;

import java.util.List;

@Controller
@RequestMapping("/faxineiros")
public class FaxineiroController {

    private final FaxineiroRepository faxineiroRepository;

    @Autowired
    public FaxineiroController(FaxineiroRepository faxineiroRepository) {
        this.faxineiroRepository = faxineiroRepository;
    }

    @GetMapping
    public String listarFaxineiros(Model model) {
        List<Faxineiro> faxineiros = faxineiroRepository.findAll();
        model.addAttribute("faxineiros", faxineiros);
        return "faxineiros"; // Nome do arquivo HTML (src/main/resources/templates/faxineiros.html)
    }
}

