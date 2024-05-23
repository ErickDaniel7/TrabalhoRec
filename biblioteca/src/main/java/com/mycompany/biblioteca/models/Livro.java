package com.mycompany.biblioteca.models;

/**
 *
 * @author Erick Daniel
 */

public class Livro {
    private Integer id;
    private String titulo;
    private String autor;
    private String editora;
    private Integer ano;
    private Integer exemplaresDisponiveis;

    public Livro(Integer id, String titulo, String autor, String editora, Integer ano, Integer exemplaresDisponiveis) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.editora = editora;
        this.ano = ano;
        this.exemplaresDisponiveis = exemplaresDisponiveis;
    }

    public Livro(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getEditora() {
        return editora;
    }

    public void setEditora(String editora) {
        this.editora = editora;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public Integer getExemplaresDisponiveis() {
        return exemplaresDisponiveis;
    }

    public void setExemplaresDisponiveis(Integer exemplaresDisponiveis) {
        this.exemplaresDisponiveis = exemplaresDisponiveis;
    }
}


/*
* id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    autor VARCHAR(255),
    editora VARCHAR(100),
    ano_publicacao INT,
    exemplares_disponiveis INT
* */