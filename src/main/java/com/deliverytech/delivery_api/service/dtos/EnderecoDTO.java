package com.deliverytech.delivery_api.service.dtos;

import com.deliverytech.delivery_api.entity.Endereco;
import com.deliverytech.delivery_api.validation.ValidCEP;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EnderecoDTO(
    Long id,
    @NotBlank(message = "Logradouro é obrigatório") 
    @Size(max = 200, message = "Logradouro deve ter no máximo 200 caracteres") 
    String logradouro,
    @NotBlank(message = "Número é obrigatório")
    String numero,
    String bairro,
    String cidade,
    String estado,
    @NotBlank(message = "CEP é obrigatório")
    @ValidCEP
    String cep
) {
      /**
     * Converte uma entidade EnderecoResponseDTO para  Endereco
     */
    public static Endereco fromEntity(EnderecoDTO endereco) {
        if (endereco == null) {
            return null; 
        }

        return new Endereco(
            endereco.logradouro,
            endereco.numero,
            endereco.bairro,
            endereco.cidade,
            endereco.estado,
            endereco.cep
        );
    }
    
      /**
     * Converte uma lista de EnderecoResponseDTO para uma lista de Endereco
     */
    public static java.util.List<Endereco> fromEntities(java.util.List<EnderecoDTO> itens) {
        if (itens == null || itens.isEmpty()) {
            return java.util.List.of();
        }

        return itens.stream()
                .map(EnderecoDTO::fromEntity)
                .toList();
    }
}
