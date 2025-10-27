package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.entity.Cliente;
import com.deliverytech.delivery_api.repository.ClienteRepository;
import com.deliverytech.delivery_api.service.dtos.ClienteDTO;
import com.deliverytech.delivery_api.service.dtos.ClienteResponseDTO;
import com.deliverytech.delivery_api.service.interfaces.ClienteServiceInterface;

import org.springframework.stereotype.Service; 
import org.springframework.transaction.annotation.Transactional; 
 
import java.util.List; 
import java.util.Optional; 
 
@Service
@Transactional 
public class ClienteService implements ClienteServiceInterface { 
 
    private ClienteRepository clienteRepository; 

    public ClienteService(ClienteRepository clienteRepository){
        this.clienteRepository = clienteRepository;
    }
 
    /** 
     * Cadastrar novo cliente 
     */ 
    @Override
    public ClienteResponseDTO cadastrar(ClienteDTO clienteDto) { 

        //valição contra o banco
        if (clienteRepository.existsByEmail(clienteDto.email())) { 
            throw new IllegalArgumentException("Email já cadastrado"); 
        } 
        
        Cliente cliente = new Cliente(clienteDto.nome(), clienteDto.email(), clienteDto.telefone(), clienteDto.getEndereco());
        
        
       cliente = clienteRepository.save(cliente);
        
        return ClienteResponseDTO.fromEntity(cliente);
    } 
 
    /** 
     * Buscar cliente por ID 
     */ 
    @Transactional(readOnly = true) 
    @Override
    public Optional<ClienteResponseDTO> buscarPorId(Long id) { 

        var cliente = clienteRepository.findById(id);
        if (cliente.isEmpty()) {
            throw new IllegalArgumentException("Cliente não encontrado: " + id);
        }

        return cliente
                .map(c -> ClienteResponseDTO.fromEntity(c)); 
    } 
 
    /** 
     * Buscar cliente por email 
     */ 
    @Transactional(readOnly = true) 
    @Override
    public Optional<ClienteResponseDTO> buscarPorEmail(String email) { 
        var cliente = clienteRepository.findByEmail(email);

        if (cliente.isEmpty()) {
            throw new IllegalArgumentException("Cliente não encontrado.");
        }
        return cliente
                .map(c -> ClienteResponseDTO.fromEntity(c)); 
    } 
 
    /** 
     * Listar todos os clientes a vos 
     */ 
    @Transactional(readOnly = true) 
    @Override
    public List<ClienteResponseDTO> listarAtivos() {
        return clienteRepository.findByAtivoTrue()
                .stream()
                .map(c -> ClienteResponseDTO.fromEntity(c))
                .toList();
    }

 
    /** 
     * Atualizar dados do cliente 
     */ 
    @Override
    public ClienteResponseDTO atualizar(Long id, ClienteDTO clienteAtualizado) { 
        Cliente cliente = clienteRepository.findById(id) 
            .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado: ")); 
 
        // Verificar se email não está sendo usado por outro cliente 
        if (!cliente.getEmail().equals(clienteAtualizado.email()) && 
            clienteRepository.existsByEmail(clienteAtualizado.email())) { 
            throw new IllegalArgumentException("Email já cadastrado: " + 
            clienteAtualizado.email()); 
        } 
 
        Cliente novaVersaoCliente = new Cliente(clienteAtualizado.nome(), clienteAtualizado.email(), clienteAtualizado.telefone(), clienteAtualizado.getEndereco());
        novaVersaoCliente.setId(id);
        
        clienteRepository.save(novaVersaoCliente); 

        return ClienteResponseDTO.fromEntity(novaVersaoCliente);
    } 
 
    /** 
     * Buscar clientes por nome 
     */ 
    @Transactional(readOnly = true) 
    @Override
    public List<ClienteResponseDTO> buscarPorNome(String nome) { 
        return clienteRepository.findByNomeContainingIgnoreCase(nome)
        .stream()
        .map(c -> ClienteResponseDTO.fromEntity(c)
        ).toList(); 
    } 

    /**
     * Ativar e Desativar cliente
     */
    @Override
    public void ativarDesativar(Long id, Boolean ativo) {
        Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado: " + id));

        cliente.setAtivo(ativo);

        clienteRepository.save(cliente);
    }
}
