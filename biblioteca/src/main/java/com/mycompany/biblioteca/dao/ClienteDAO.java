package com.mycompany.biblioteca.dao;

import com.mycompany.biblioteca.models.Cliente;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

/**
 *
 * @author Erick Daniel
 */

public class ClienteDAO {
    
    private List<Cliente> clientes = new ArrayList<>();
    
    public ClienteDAO(){
        
    }
    
    public List<Cliente> listar (){
        return clientes;
    }
    
    public Cliente buscarPorId(Integer id){
        for (Cliente cliente:clientes){
            if (cliente.getId().equals(id)) return cliente;
        }
        return null;
    }
    
    
    public List<Cliente> buscarClientePorNome(String nome){
        List<Cliente> filtrados = new ArrayList();
        for (Cliente cliente:clientes){
            if (cliente.getNome().toLowerCase().contains(nome.toLowerCase())) {
                filtrados.add(cliente);
            }
        }
        return filtrados;
    }
    
    public List<Cliente> buscarClientePorSobrenome(String sobrenome){
        List<Cliente> filtrados = new ArrayList();
        for (Cliente cliente:clientes){
            if (cliente.getSobrenome().toLowerCase().contains(sobrenome.toLowerCase())) {
                filtrados.add(cliente);
            }
        }
        return filtrados;
    }
    
    public List<Cliente> buscarClientePorTelefone(String telefone){
        List<Cliente> filtrados = new ArrayList();
        for (Cliente cliente:clientes){
            if (cliente.getTelefoneDeContato().toLowerCase().contains(telefone.toLowerCase())) {
                filtrados.add(cliente);
            }
        }
        return filtrados;
    }
    
    public List<Cliente> buscarClientePorEndereco(String endereco){
        List<Cliente> filtrados = new ArrayList();
        for (Cliente cliente:clientes){
            if (cliente.getEndereco().toLowerCase().contains(endereco.toLowerCase())) {
                filtrados.add(cliente);
            }
        }
        return filtrados;
    }
    
    /*Inserir novo cliente com id unico e ordenado*/
    public void inserir(Cliente novoCliente){
        int novoId = 1;  // ID inicial se a lista estiver vazia
        if (!clientes.isEmpty()) {
            Cliente ultimoCliente = clientes.get(clientes.size() - 1);
            novoId = ultimoCliente.getId() + 1;
        }
        novoCliente.setId(novoId);
        System.out.println("Novo id:"+novoId);
        clientes.add(novoCliente);
        
    }
    
   public void atualizar(Cliente clienteAtualizado) {
       for (int i = 0; i < clientes.size(); i++) {
            Cliente cliente = clientes.get(i);
            if (cliente.getId().equals(clienteAtualizado.getId())) {
                clientes.set(i, clienteAtualizado);
                return;
            }
        }
    }

    public Boolean deletar(Integer id) {
        return clientes.removeIf(cliente -> cliente.getId().equals(id));
    }
    
}
