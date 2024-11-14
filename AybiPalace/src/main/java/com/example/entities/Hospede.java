package com.example.entities;

import jakarta.persistence.*;

@Entity
public class Hospede {

    @Id
    private String cpf;

    @Column(nullable = false)
    private String nome;

    private int idade;
    private String cep;
    private String rua;
    private String bairro;
    private int numero;
    private String email;

    // Getters e setters
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public int getIdade() { return idade; }
    public void setIdade(int idade) { this.idade = idade; }

    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }

    public String getRua() { return rua; }
    public void setRua(String rua) { this.rua = rua; }

    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }

    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
