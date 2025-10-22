package com.deliverytech.delivery_api.service.dtos;

import java.util.List;

import com.deliverytech.delivery_api.entity.Cliente;

public record ClienteResponseDTO(
    Long id,
    String nome,
    String email,
    String telefone,
    boolean ativo, 
    List<EnderecoResponseDTO> endereco) { 

     /**
     * Converte uma entidade Cliente para ClienteResponseDTO
     */
    public static ClienteResponseDTO fromEntity(Cliente cliente) {
        if (cliente == null) {
            return null;
        }

        return new ClienteResponseDTO(
            cliente.getId(),
            cliente.getNome(),
            cliente.getEmail(),
            cliente.getTelefone(),
            cliente.isAtivo(),
            EnderecoResponseDTO.fromEntities(cliente.getEnderecos())
        );
    }
    
      /**
     * Converte uma lista de Cliente para uma lista de ClienteResponseDTO
     */
    public static java.util.List<ClienteResponseDTO> fromEntities(java.util.List<Cliente> itens) {
        if (itens == null || itens.isEmpty()) {
            return java.util.List.of();
        }

        return itens.stream()
                .map(ClienteResponseDTO::fromEntity)
                .toList();
    }
}