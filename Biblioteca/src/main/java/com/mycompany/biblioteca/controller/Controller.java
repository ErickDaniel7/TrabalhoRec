/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.biblioteca.controller;

import com.mycompany.biblioteca.model.Cliente;
import java.util.ArrayList;

/**
 *
 * @author aluno
 */
public class Controller {
    
    public static ArrayList<Cliente> listaCliente;
    
    static {
        listaCliente = new ArrayList<>();
    }

    // Método para adicionar um cliente à lista
    public static void adicionarCliente(Cliente cliente) {
        listaCliente.add(cliente);
    }
    
    //Método para obter a lista de clientes
    public static ArrayList<Cliente> getListaCliente() {
        return listaCliente;
        
    }
}
 