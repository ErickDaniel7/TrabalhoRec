package com.mycompany.biblioteca.exceptions;

/**
 *
 * @author Erick Daniel
 */

public class LimiteDeLivrosAlcancadoException extends RuntimeException{
    public LimiteDeLivrosAlcancadoException(String mensagem){
        super(mensagem);
    }
}
