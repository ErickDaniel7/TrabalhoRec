/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.biblioteca.model;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author erixk
 */
public class Biblioteca {
    private List<Cliente> clientes;

    public Biblioteca() {
        this.clientes = new ArrayList<>();
    }

    public void adicionarCliente(Cliente cliente) {
        clientes.add(cliente);
    }

    // Outros métodos, se necessário
}
