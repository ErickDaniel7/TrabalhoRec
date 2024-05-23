package com.mycompany.biblioteca.dao;

import com.mycompany.biblioteca.models.Livro;
import java.util.ArrayList;
import java.util.List;

/**
 *S
 * @author Erick Daniel
 */

public class LivroDAO {
    
    private List<Livro> livros = new ArrayList<>();
    
    public LivroDAO(){
        
    }
    
    public List<Livro> listar(){
        return livros;
    }
    
     /*Inserir novo livro com id unico e ordenado*/
    public void inserir(Livro novoLivro){
        int novoId = 1;  // ID inicial se a lista estiver vazia
        if (!livros.isEmpty()) {
            Livro ultimoLivro = livros.get(livros.size() - 1);
            novoId = ultimoLivro.getId() + 1;
        }
        novoLivro.setId(novoId);
        livros.add(novoLivro);
        
    }
    
    public Boolean deletar(Integer id) {
        return livros.removeIf(livro -> livro.getId().equals(id));
    }
    
    public Livro buscarPorId(Integer id){
         for (Livro livro:livros){
            if (livro.getId().equals(id)) return livro;
        }
        return null;
    }
    
    public List<Livro> buscarPorTitulo(String titulo){
        List<Livro> filtrados = new ArrayList();
        for (Livro livro:livros){
            if (livro.getTitulo().toLowerCase().contains(titulo.toLowerCase())) {
                filtrados.add(livro);
            }
        }
        return filtrados;
    }
    
    public List<Livro> buscarPorAutor(String autor){
        List<Livro> filtrados = new ArrayList();
        for (Livro livro:livros){
            if (livro.getAutor().toLowerCase().contains(autor.toLowerCase())) {
                filtrados.add(livro);
            }
        }
        return filtrados;
    }
    
    public List<Livro> buscarPorEditora(String editora){
        List<Livro> filtrados = new ArrayList();
        for (Livro livro:livros){
            if (livro.getEditora().toLowerCase().contains(editora.toLowerCase())) {
                filtrados.add(livro);
            }
        }
        return filtrados;
    }
    /*Pesquisa se livro se encontra na lista e o substitui quando for encontrado*/
    public void atualizar(Livro livroAtualizado){
         for (int i = 0; i < livros.size(); i++) {
            Livro livro = livros.get(i);
            if (livro.getId().equals(livroAtualizado.getId())) {
                livros.set(i, livroAtualizado);
                return;
            }
        }
    }
}
