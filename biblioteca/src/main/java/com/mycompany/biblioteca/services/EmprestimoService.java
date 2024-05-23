package com.mycompany.biblioteca.services;

import com.mycompany.biblioteca.dao.EmprestimoDAO;
import com.mycompany.biblioteca.exceptions.EmprestimoDuplicadoException;
import com.mycompany.biblioteca.exceptions.EmprestimoFinalizadoException;
import com.mycompany.biblioteca.exceptions.LimiteDeLivrosAlcancadoException;
import com.mycompany.biblioteca.exceptions.OrdenacaoEntreDatasIncompativel;
import com.mycompany.biblioteca.models.Emprestimo;
import com.mycompany.biblioteca.models.Livro;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Erick Daniel
 */

public class EmprestimoService {
    private EmprestimoDAO dao = new EmprestimoDAO();
    
    private LivroService livroService;
    
    public EmprestimoService(LivroService livroService){
        this.livroService = livroService;
    }
    
    public void emprestar(Emprestimo emprestimo) {
        validarDatas(emprestimo);
        validarEmprestimoNaoDuplicado(emprestimo);
        Livro livroTestado = livroService.obterLivroPorId(emprestimo.getLivro().getId());
        Integer exemplares = livroTestado.getExemplaresDisponiveis();
        if (exemplares >= 1){ 
            livroTestado.setExemplaresDisponiveis(exemplares -1);
            livroService.atualizar(livroTestado);
            emprestimo.setFinalizado(false);
            dao.inserir(emprestimo);
        }else {
            throw new LimiteDeLivrosAlcancadoException("Não há livros disponíveis para empréstimo");
        }
        
    }
    
    public void devolver(Emprestimo emprestimo){
        validarDatas(emprestimo);
        validarStatusEmprestimo(emprestimo);
        Livro livroTestado = livroService.obterLivroPorId(emprestimo.getLivro().getId());
        Integer exemplares = livroTestado.getExemplaresDisponiveis();
        livroTestado.setExemplaresDisponiveis(exemplares + 1);
        livroService.atualizar(livroTestado);
        emprestimo.setFinalizado(true);
        dao.atualizar(emprestimo);
    }
    
    public List<Emprestimo> buscarEmprestimoPorClienteID(Integer id){
        return dao.buscarPorClienteID(id);
    }
    
    public Emprestimo buscarEmprestimoPorId(Integer id){
        return dao.buscarEmprestimoPorId(id);
    }
    
    public List<Emprestimo> buscarEmprestimoPorTituloLivro(String titulo){
        return dao.buscarEmprestimoPorTituloLivro(titulo);
    }
    
    public List<Emprestimo> buscarEmprestimoPorDataSaida(LocalDate dataSaida){
        return dao.buscarPorDataSaida(dataSaida);
    }
    
    public List<Emprestimo> buscarEmprestimoPorDataDevolucao(LocalDate dataDevolucao){
        return dao.buscarPorDataDevolucao(dataDevolucao);
    }
    
    public List<Emprestimo> listar(){
        return dao.listar();
    }
    
    /*Verificar se data de entrada é menor que data de saída */
    private void validarDatas(Emprestimo emprestimo){
        if (!emprestimo.getDataDevolucao().isAfter(emprestimo.getDataEmprestimo())) 
            throw new OrdenacaoEntreDatasIncompativel("Devolução não pode ser antes da data de empréstimo!");
    }
    
    /*Verifica se existe emprestimo do mesmo livro que ainda não foi finalizado*/
    private void validarEmprestimoNaoDuplicado(Emprestimo emprestimo){
        List<Emprestimo> emprestimos = dao.buscarPorClienteID(emprestimo.getCliente().getId());
        for (Emprestimo e: emprestimos){
            if( e.getLivro().getId().equals(emprestimo.getLivro().getId()) && e.isFinalizado() ==false){
                throw new EmprestimoDuplicadoException("Já consta um emrpréstimo desse livro aberto para o cliente");
            } 
        }
    }
    
    

    private void validarStatusEmprestimo(Emprestimo emprestimo){
        if (emprestimo.isFinalizado()) throw new EmprestimoFinalizadoException("Esse empréstimo já foi finalizado");
    }
}
