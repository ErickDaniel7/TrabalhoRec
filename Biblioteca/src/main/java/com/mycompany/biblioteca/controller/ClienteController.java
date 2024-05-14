/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.biblioteca.controller;
import com.mycompany.biblioteca.model.Cliente;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author erixk
 */
public class ClienteController {
    private static List<Cliente> listaClientes = new ArrayList<>();

    public static void cadastrarCliente(Cliente cliente) {
        listaClientes.add(cliente);
    }

    public static List<Cliente> getListaCliente() {
        return listaClientes;
    }
}
