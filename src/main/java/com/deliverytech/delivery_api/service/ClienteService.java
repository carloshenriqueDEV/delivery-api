package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.entity.Cliente; 
import com.deliverytech.delivery_api.repository.ClienteRepository;
import com.deliverytech.delivery_api.service.dtos.ClienteDTO;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Service; 
import org.springframework.transaction.annotation.Transactional; 
 
import java.util.List; 
import java.util.Optional; 
 
@Service 
@Transactional 
public class ClienteService { 
 
    @Autowired 
    private ClienteRepository clienteRepository; 
 
    /** 
     * Cadastrar novo cliente 
     */ 
    public Cliente cadastrar(Cliente cliente) { 
        // Validar email único 
        if (clienteRepository.existsByEmail(cliente.getEmail())) { 
            throw new IllegalArgumentException("Email já cadastrado: " + cliente.getEmail()); 
        } 
 
        // Validações de negócio 
        validarDadosCliente(cliente); 
 
        // Definir como a vo por padrão 
        cliente.setAtivo(true); 
 
        return clienteRepository.save(cliente); 
    } 
 
    /** 
     * Buscar cliente por ID 
     */ 
    @Transactional(readOnly = true) 
    public Optional<ClienteDTO> buscarPorId(Long id) { 
        return clienteRepository.findById(id)
                .map(c -> new ClienteDTO(
                    c.getId(),
                    c.getNome(),
                    c.getEmail(),
                    c.getTelefone(),
                    c.isAtivo() 
                )); 
    } 
 
    /** 
     * Buscar cliente por email 
     */ 
    @Transactional(readOnly = true) 
    public Optional<Cliente> buscarPorEmail(String email) { 
        return clienteRepository.findByEmail(email); 
    } 
 
    /** 
     * Listar todos os clientes a vos 
     */ 
    @Transactional(readOnly = true) 
    public List<ClienteDTO> listarAtivos() {
        return clienteRepository.findByAtivoTrue()
                .stream()
                .map(c -> new ClienteDTO(
                    c.getId(),
                    c.getNome(),
                    c.getEmail(),
                    c.getTelefone(),
                    c.isAtivo() 
                ))
                .toList();
    }

 
    /** 
     * Atualizar dados do cliente 
     */ 
    public Cliente atualizar(Long id, Cliente clienteAtualizado) { 
        Cliente cliente = clienteRepository.findById(id) 
            .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado: " + id)); 
 
        // Verificar se email não está sendo usado por outro cliente 
        if (!cliente.getEmail().equals(clienteAtualizado.getEmail()) && 
            clienteRepository.existsByEmail(clienteAtualizado.getEmail())) { 
            throw new IllegalArgumentException("Email já cadastrado: " + 
            clienteAtualizado.getEmail()); 
        } 
 
        // Atualizar campos 
        cliente.setNome(clienteAtualizado.getNome()); 
        cliente.setEmail(clienteAtualizado.getEmail()); 
        cliente.setTelefone(clienteAtualizado.getTelefone()); 
        cliente.setEndereco(clienteAtualizado.getEndereco()); 
 
        return clienteRepository.save(cliente); 
    } 
 
    /** 
     * Ina var cliente (so delete) 
     */ 
    public void inativar(Long id) { 
        Cliente cliente = clienteRepository.findById(id) 
            .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado: " + id)); 
 
        cliente.inativar(); 
        clienteRepository.save(cliente); 
    } 
 
    /** 
     * Buscar clientes por nome 
     */ 
    @Transactional(readOnly = true) 
    public List<Cliente> buscarPorNome(String nome) { 
        return clienteRepository.findByNomeContainingIgnoreCase(nome); 
    } 
 
    /** 
     * Validações de negócio 
     */ 
    private void validarDadosCliente(Cliente cliente) { 
        if (cliente.getNome() == null || cliente.getNome().trim().isEmpty()) { 
            throw new IllegalArgumentException("Nome é obrigatório"); 
        } 
 
        if (cliente.getEmail() == null || cliente.getEmail().trim().isEmpty()) { 
            throw new IllegalArgumentException("Email é obrigatório"); 
        } 
 
        if (cliente.getNome().length() < 2) { 
            throw new IllegalArgumentException("Nome deve ter pelo menos 2 caracteres"); 
        } 
    } 
}
