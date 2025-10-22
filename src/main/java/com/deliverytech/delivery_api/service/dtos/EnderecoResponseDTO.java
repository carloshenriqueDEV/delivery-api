package com.deliverytech.delivery_api.service.dtos;

import com.deliverytech.delivery_api.entity.Endereco;

public record EnderecoResponseDTO(   
    Long id, 
    String logradouro,
    String numero,
    String bairro,
    String cidade,
    String estado,
    String cep
) {
      /**
     * Converte uma entidade Endereco para EnderecoResponseDTO
     */
    public static EnderecoResponseDTO fromEntity(Endereco endereco) {
        if (endereco == null) {
            return null; 
        }

        return new EnderecoResponseDTO(
            endereco.getId(),
            endereco.getLogradouro(),
            endereco.getNumero(),
            endereco.getBairro(),
            endereco.getCidade(),
            endereco.getEstado(),
            endereco.getCep()
        );
    }
    
      /**
     * Converte uma lista de Endereco para uma lista de EnderecoResponseDTO
     */
    public static java.util.List<EnderecoResponseDTO> fromEntities(java.util.List<Endereco> itens) {
        if (itens == null || itens.isEmpty()) {
            return java.util.List.of();
        }

        return itens.stream()
                .map(EnderecoResponseDTO::fromEntity)
                .toList();
    }
    
}
