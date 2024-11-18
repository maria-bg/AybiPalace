package com.example.entities;

public class Dependente {
    private String cpf;
    private String nome;
    private int idade;
    private String fkHospedeCpf; // CPF do hóspede principal do qual depende

    // Construtor
    public Dependente(String cpf, String nome, int idade, String fkHospedeCpf) {
        this.cpf = cpf;
        this.nome = nome;
        this.idade = idade;
        this.fkHospedeCpf = fkHospedeCpf;
    }

    // Getters e Setters
    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public String getFkHospedeCpf() {
        return fkHospedeCpf;
    }

    public void setFkHospedeCpf(String fkHospedeCpf) {
        this.fkHospedeCpf = fkHospedeCpf;
    }

    @Override
    public String toString() {
        return "Dependente{" +
                "cpf='" + cpf + '\'' +
                ", nome='" + nome + '\'' +
                ", idade=" + idade +
                ", fkHospedeCpf='" + fkHospedeCpf + '\'' +
                '}';
    }
}
