package com.example.entities;

public class Quarto {
    private int numero;
    private double tarifa;
    private int camas;

    public Quarto(int numero, double tarifa, int camas) {
        this.numero = numero;
        this.tarifa = tarifa;
        this.camas = camas;
    }

    // Getters e Setters
    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }

    public double getTarifa() { return tarifa; }
    public void setTarifa(double tarifa) { this.tarifa = tarifa; }

    public int getCamas() { return camas; }
    public void setCamas(int camas) { this.camas = camas; }
}
