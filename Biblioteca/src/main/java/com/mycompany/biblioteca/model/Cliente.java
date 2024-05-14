/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.biblioteca.model;
import java.io.Serializable;

/**
 *
 * @author erixk
 */

public class Cliente implements Serializable {
    private String nome;
    private String sobrenome;
    private int idade;
    private String numeroContato;
    private String endereco;

    // Construtor
    public Cliente(String nome, String sobrenome, int idade, String numeroContato, String endereco) {
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.idade = idade;
        this.numeroContato = numeroContato;
        this.endereco = endereco;
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

   public String getNomeCompleto() {
    return nome + " " + sobrenome;
    }
}

