package com.mycompany.biblioteca.dao;

import com.mycompany.biblioteca.models.Cliente;
import com.mycompany.biblioteca.models.Emprestimo;
import com.mycompany.biblioteca.models.Livro;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Erick Daniel
 */

public class EmprestimoDAO {
    private List<Emprestimo> emprestimos = new ArrayList<>();
    
    
    public EmprestimoDAO(){
       
    }
    
    public void inserir(Emprestimo e){
        int novoId = 1;  // ID inicial se a lista estiver vazia
        if (!emprestimos.isEmpty()) {
            Emprestimo ultimo = emprestimos.get(emprestimos.size() - 1);
            novoId = ultimo.getId() + 1;
        }
        e.setId(novoId);
        emprestimos.add(e);
    }
    
    public List<Emprestimo> listar(){
        return emprestimos;
    }
    
    public List<Emprestimo> buscarPorClienteID(Integer id){
        return emprestimos.stream()
                .filter(e -> e.getCliente().getId().equals(id))
                      .collect(Collectors.toList());
    }
    
    
    public Emprestimo buscarEmprestimoPorId(Integer id){
        for (Emprestimo e: emprestimos){
            if (e.getId().equals(id)) return e;
        }
        return null;
    }
    
    public List<Emprestimo> buscarEmprestimoPorTituloLivro(String titulo){
        List<Emprestimo> emprestimosFiltrado = new ArrayList();
        if (titulo!=null){
            emprestimosFiltrado = emprestimos.stream()
                .filter(e -> e.getLivro().getTitulo().toLowerCase().contains(titulo.toLowerCase()))
                      .collect(Collectors.toList());
        }
        return emprestimosFiltrado;
    }
    
    public List<Emprestimo> buscarPorDataSaida(LocalDate data){
        
        return emprestimos.stream().
                    filter(e-> e.getDataEmprestimo().isEqual(data))
                    .collect(Collectors.toList());
    }
    
    public List<Emprestimo> buscarPorDataDevolucao(LocalDate data){
        return emprestimos.stream().
                    filter(e-> e.getDataDevolucao().isEqual(data))
                    .collect(Collectors.toList());
    }
    
    public void atualizar(Emprestimo emprestimoAtualizado) {
       for (int i = 0; i < emprestimos.size(); i++) {
            Emprestimo emprestimo = emprestimos.get(i);
            if (emprestimo.getId().equals(emprestimoAtualizado.getId())) {
                emprestimos.set(i, emprestimoAtualizado);
                return;
            }
        }
    }
}
