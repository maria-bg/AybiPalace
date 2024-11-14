package com.example.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.entities.Hospede;
import com.example.repositories.HospedeRepository;

@Controller
@RequestMapping("/clientes")
public class HospedeController {

    private final HospedeRepository clienteRepository;

    @Autowired
    public HospedeController(HospedeRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @GetMapping("/cadastro")
    public String exibirFormularioCadastro() {
        return "cadastro_cliente";
    }

    @PostMapping("/salvar")
    public String salvarCliente(
            @RequestParam("cpf") String cpf,
            @RequestParam("nome") String nome,
            @RequestParam("idade") int idade,
            @RequestParam("cep") String cep,
            @RequestParam("rua") String rua,
            @RequestParam("bairro") String bairro,
            @RequestParam("numero") int numero,
            @RequestParam("email") String email,
            Model model) {
        
    	Hospede cliente = new Hospede();
        cliente.setCpf(cpf);
        cliente.setNome(nome);
        cliente.setIdade(idade);
        cliente.setCep(cep);
        cliente.setRua(rua);
        cliente.setBairro(bairro);
        cliente.setNumero(numero);
        cliente.setEmail(email);
        
        clienteRepository.save(cliente);

        model.addAttribute("mensagem", "Cliente salvo com sucesso!");
        return "cadastro_cliente";
    }
}

