/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.biblioteca.models;

import java.time.LocalDate;
import java.time.Period;

/**
 *
 * @author Erick Daniel
 */

public class Cliente {
    private Integer id;
    private String nome;
    private String sobrenome;
    private String endereco;
    private String telefoneDeContato;
    private Integer idade;
    private LocalDate dataDeNascimento;

    public Cliente(Integer id, String nome, String sobrenome, String endereco, String telefoneDeContato, LocalDate dataDeNascimento) {
        this.id = id;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.endereco = endereco;
        this.telefoneDeContato = telefoneDeContato;
        this.dataDeNascimento = dataDeNascimento;
        setIdade(dataDeNascimento);
    }

    public Cliente(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefoneDeContato() {
        return telefoneDeContato;
    }

    public void setTelefoneDeContato(String telefoneDeContato) {
        this.telefoneDeContato = telefoneDeContato;
    }

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(LocalDate data) {
        
       if (data == null) {
            throw new IllegalArgumentException("Data de nascimento não pode ser nula.");
        }
        
        LocalDate dataAtual = LocalDate.now();
        this.idade = Period.between(data, dataAtual).getYears();
    }

    public LocalDate getDataDeNascimento() {
        return dataDeNascimento;
    }

    public void setDataDeNascimento(LocalDate dataDeNascimento) {
        this.dataDeNascimento = dataDeNascimento;
    }
}
