/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.biblioteca;

import com.mycompany.biblioteca.view.FrameCadastroCliente;
import com.mycompany.biblioteca.view.FrameCadastroLivro;
import com.mycompany.biblioteca.view.FrameControleEmprestimo;
import com.mycompany.biblioteca.models.Livro;
import com.mycompany.biblioteca.services.ClienteService;
import com.mycompany.biblioteca.services.EmprestimoService;
import com.mycompany.biblioteca.services.LivroService;

import java.util.List;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author Erick Daniel
 */
public class Biblioteca extends JFrame {

    public Biblioteca() {

        ClienteService clienteService = new ClienteService();
        LivroService livroService = new LivroService();
        EmprestimoService emprestimoService = new EmprestimoService(livroService);

        setTitle("Sistema de Biblioteca");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Criar um JTabbedPane para organizar os painéis
        JTabbedPane tabbedPane = new JTabbedPane();

        // Adicionar os painéis ao JTabbedPane
        tabbedPane.addTab("Controle de Empréstimos", new FrameControleEmprestimo(emprestimoService, livroService, clienteService));
        tabbedPane.addTab("Cadastro de Livros", new FrameCadastroLivro(livroService));
        tabbedPane.addTab("Cadastro de Clientes", new FrameCadastroCliente(clienteService));

        // Adicionar o JTabbedPane ao frame
        add(tabbedPane);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Biblioteca().setVisible(true);
            }
        });

    }
}
