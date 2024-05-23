
package com.mycompany.biblioteca.exceptions;

/**
 *
 * @author Erick Daniel
 */

public class EmprestimoFinalizadoException extends RuntimeException{
    public EmprestimoFinalizadoException(String mensagem){
        super(mensagem);
    }
}
