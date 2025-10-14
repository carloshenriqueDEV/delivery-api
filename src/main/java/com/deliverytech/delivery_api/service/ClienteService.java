package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.entity.Cliente; 
import com.deliverytech.delivery_api.repository.ClienteRepository;
import com.deliverytech.delivery_api.service.dtos.ClienteDTO;
import com.deliverytech.delivery_api.service.interfaces.ClienteServiceInterface;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Service; 
import org.springframework.transaction.annotation.Transactional; 
 
import java.util.List; 
import java.util.Optional; 
 
@Service
@Transactional 
public class ClienteService implements ClienteServiceInterface { 
 
    @Autowired 
    private ClienteRepository clienteRepository; 
 
    /** 
     * Cadastrar novo cliente 
     */ 
    @Override
    public ClienteDTO cadastrar(ClienteDTO clienteDto) { 

        //valição contra o banco
        if (clienteRepository.existsByEmail(clienteDto.email())) { 
            throw new IllegalArgumentException("Email já cadastrado"); 
        } 

        var cliente = new Cliente(clienteDto.nome(), clienteDto.email(), clienteDto.telefone(), clienteDto.endereco());
        
        
       cliente = clienteRepository.save(cliente);
        
        return new ClienteDTO(
            cliente.getId(),
            cliente.getNome(),
            cliente.getEmail(),
            cliente.getTelefone(),
            cliente.isAtivo(),
            cliente.getEndereco()
        );
    } 
 
    /** 
     * Buscar cliente por ID 
     */ 
    @Transactional(readOnly = true) 
    @Override
    public Optional<ClienteDTO> buscarPorId(Long id) { 

        var cliente = clienteRepository.findById(id);
        if (cliente.isEmpty()) {
            throw new IllegalArgumentException("Cliente não encontrado: " + id);
        }

        return cliente
                .map(c -> new ClienteDTO(
                    c.getId(),
                    c.getNome(),
                    c.getEmail(),
                    c.getTelefone(),
                    c.isAtivo(),
                    c.getEndereco()
                )); 
    } 
 
    /** 
     * Buscar cliente por email 
     */ 
    @Transactional(readOnly = true) 
    @Override
    public Optional<ClienteDTO> buscarPorEmail(String email) { 
        var cliente = clienteRepository.findByEmail(email);

        if (cliente.isEmpty()) {
            throw new IllegalArgumentException("Cliente não encontrado.");
        }
        return cliente
                .map(c -> new ClienteDTO(
                    c.getId(),
                    c.getNome(),
                    c.getEmail(),
                    c.getTelefone(),
                    c.isAtivo(),
                    c.getEndereco()
                )); 
    } 
 
    /** 
     * Listar todos os clientes a vos 
     */ 
    @Transactional(readOnly = true) 
    @Override
    public List<ClienteDTO> listarAtivos() {
        return clienteRepository.findByAtivoTrue()
                .stream()
                .map(c -> new ClienteDTO(
                    c.getId(),
                    c.getNome(),
                    c.getEmail(),
                    c.getTelefone(),
                    c.isAtivo(),
                    c.getEndereco() 
                ))
                .toList();
    }

 
    /** 
     * Atualizar dados do cliente 
     */ 
    @Override
    public ClienteDTO atualizar(ClienteDTO clienteAtualizado) { 
        Cliente cliente = clienteRepository.findById(clienteAtualizado.id()) 
            .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado: ")); 
 
        // Verificar se email não está sendo usado por outro cliente 
        if (!cliente.getEmail().equals(clienteAtualizado.email()) && 
            clienteRepository.existsByEmail(clienteAtualizado.email())) { 
            throw new IllegalArgumentException("Email já cadastrado: " + 
            clienteAtualizado.email()); 
        } 
 
        // Atualizar campos 
        cliente.setNome(clienteAtualizado.nome()); 
        cliente.setEmail(clienteAtualizado.email()); 
        cliente.setTelefone(clienteAtualizado.telefone()); 
        cliente.setEndereco(clienteAtualizado.endereco()); 
        
        clienteRepository.save(cliente); 

        return clienteAtualizado;
    } 
 
    /** 
     * Buscar clientes por nome 
     */ 
    @Transactional(readOnly = true) 
    @Override
    public List<ClienteDTO> buscarPorNome(String nome) { 
        return clienteRepository.findByNomeContainingIgnoreCase(nome)
        .stream()
        .map(c -> new ClienteDTO( 
            c.getId(), 
            c.getNome(), 
            c.getEmail(), 
            c.getTelefone(), 
            c.isAtivo(),
            c.getEndereco() 
        )).toList(); 
    } 

    /**
     * Ativar e Desativar cliente
     */
    @Override
    public void ativarDesativar(Long id) {
        Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado: " + id));

        if (cliente.isAtivo()) {
            cliente.inativar();
        } else {
            cliente.setAtivo(true);
        }

        clienteRepository.save(cliente);
    }
}
