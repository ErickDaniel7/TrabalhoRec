package com.mycompany.biblioteca.services;

import com.mycompany.biblioteca.dao.ClienteDAO;
import com.mycompany.biblioteca.models.Cliente;
import java.util.List;

/**
 *
 * @author Erick Daniel
 */

public class ClienteService {
    
    ClienteDAO dao = new ClienteDAO();
    
    public void salvar(Cliente cliente){
        dao.inserir(cliente);
    }
    
    public void atualizar(Cliente cliente){
        dao.atualizar(cliente);
    }
    
    public Boolean remover(Integer id){
        return dao.deletar(id);
    }
    
    public Cliente obterClientePorID(String id){
        
        return dao.buscarPorId(Integer.parseInt(id));
    }
    
    public List<Cliente> obterClientePorNome(String nome){
        return dao.buscarClientePorNome(nome);
    }
    
    public List<Cliente> obterClientePorSobrenome(String sobrenome){
        return dao.buscarClientePorSobrenome(sobrenome);
    }
    
    public List<Cliente> obterClientePorTelefone(String telefone){
        return dao.buscarClientePorTelefone(telefone);
    }
    
    public List<Cliente> obterClientePorEndereco(String endereco){
        return dao.buscarClientePorEndereco(endereco);
    }
    
    public List<Cliente> listarClientes(){
        return dao.listar();
    }
    
}
