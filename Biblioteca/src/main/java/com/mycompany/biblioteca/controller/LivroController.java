/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.biblioteca.controller;

import com.mycompany.biblioteca.model.Livro;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author erixk
 */
public class LivroController {
    private static List<Livro> listaLivros = new ArrayList<>();
    
    public static void cadastrarLivro(Livro livro) {
        listaLivros.add(livro);
    }
    
    public static List<Livro> getListaLivros() {
        return listaLivros;
    }
    
}
