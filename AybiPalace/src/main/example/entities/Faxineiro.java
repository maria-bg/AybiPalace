package com.example.entities;

public class Faxineiro {
    private String cpf;
    private String nome;
    private int idade;
    private String cep;
    private String rua;
    private String bairro;
    private int numero;
    private String email;
    private float salario;
    private String faxineiroGerente;

    public Faxineiro() {}

    public Faxineiro(String cpf, String nome, int idade, String cep, String rua, String bairro, int numero, String email, float salario, String faxineiroGerente) {
        this.cpf = cpf;
        this.nome = nome;
        this.idade = idade;
        this.cep = cep;
        this.rua = rua;
        this.bairro = bairro;
        this.numero = numero;
        this.email = email;
        this.salario = salario;
        this.faxineiroGerente = faxineiroGerente;
    }

    @Override
    public String toString() {
        return "Faxineiro{" +
                "cpf='" + cpf + '\'' +
                ", nome='" + nome + '\'' +
                ", idade=" + idade +
                ", cep='" + cep + '\'' +
                ", rua='" + rua + '\'' +
                ", bairro='" + bairro + '\'' +
                ", numero=" + numero +
                ", email='" + email + '\'' +
                ", salario=" + salario +
                ", faxineiroGerente='" + faxineiroGerente + '\'' +
                '}';
    }

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

    public float getSalario() { return salario; }
    public void setSalario(float salario) { this.salario = salario; }

    public String getFaxineiroGerente() { return faxineiroGerente; }
    public void setFaxineiroGerente(String faxineiroGerente) { this.faxineiroGerente = faxineiroGerente; }
}