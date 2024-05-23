package com.mycompany.biblioteca.services;

import com.mycompany.biblioteca.dao.LivroDAO;
import com.mycompany.biblioteca.models.Livro;

/**
 *
 * @author Erick Daniel
 */

import java.util.List;

public class LivroService {
    LivroDAO dao = new LivroDAO();
    
    public LivroService(){
    }

    public List<Livro> listarLivro(){
        return  dao.listar();
    }

    public void salvar(Livro livro){
        dao.inserir(livro);
    }

    public void atualizar(Livro livro){
        dao.atualizar(livro);
    }

    public Livro obterLivroPorId(Integer id){
        return dao.buscarPorId(id);
    }

    public List<Livro> obterLivroPorTitulo(String titulo){
        return dao.buscarPorTitulo(titulo);
    }
    
    public List<Livro> obterLivroPorAutor(String autor){
        return dao.buscarPorAutor(autor);
    }
    
    public List<Livro> obterLivroPorEditora(String editora){
        return dao.buscarPorEditora(editora);
    }
    
    public Boolean removerLivro(Integer id){
        return dao.deletar(id);
    }
}
