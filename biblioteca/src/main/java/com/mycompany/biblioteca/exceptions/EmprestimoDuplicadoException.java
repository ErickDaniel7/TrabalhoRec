/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.biblioteca.exceptions;

/**
 *
 * @author Erick Daniel
 */

public class EmprestimoDuplicadoException extends RuntimeException{
    
    public EmprestimoDuplicadoException(String mensagem){
        super(mensagem);
    }
    
}
