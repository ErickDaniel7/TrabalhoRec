package com.mycompany.biblioteca.models;

import java.time.LocalDate;

/**
 *
 * @author Erick Daniel
 */

public class Emprestimo {
    
    private Integer id;
    private Cliente cliente;
    private Livro livro;
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucao;
    private Boolean finalizado;

    // Construtor
    public Emprestimo(Integer id, Cliente cliente, Livro livro, LocalDate dataEmprestimo) {
        this.id = id;
        this.cliente = cliente;
        this.livro = livro;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucao = dataEmprestimo.plusDays(14); // prazo de devolução de 14 dias
    }
    
    public Emprestimo(Integer id, Cliente cliente, Livro livro, LocalDate dataEmprestimo, LocalDate dataDevolucao) {
        this.id = id;
        this.cliente = cliente;
        this.livro = livro;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucao = dataDevolucao; // prazo estipulado pelo usuário
    }

    public Emprestimo(){}
    
    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Livro getLivro() {
        return livro;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(LocalDate dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(LocalDate dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    // Método para verificar se o empréstimo está atrasado
    public boolean isAtrasado() {
        return (!isFinalizado() && LocalDate.now().isAfter(dataDevolucao));
    }
    
    public Boolean isFinalizado(){
        return this.finalizado;
    }
    
    public void setFinalizado(Boolean finalizado){
        this.finalizado = finalizado;
    }

    @Override
    public String toString() {
        return "Emprestimo{" +
                "id=" + id +
                ", cliente=" + cliente.getNome() + " " + cliente.getSobrenome() +
                ", livro=" + livro.getTitulo() +
                ", dataEmprestimo=" + dataEmprestimo +
                ", dataDevolucao=" + dataDevolucao +
                ", finalizado=" + finalizado +
                '}';
    }
}