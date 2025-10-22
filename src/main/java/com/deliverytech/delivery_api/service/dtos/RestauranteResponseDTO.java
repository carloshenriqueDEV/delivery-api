package com.deliverytech.delivery_api.service.dtos;

import java.math.BigDecimal;
import java.util.List;

import com.deliverytech.delivery_api.entity.Restaurante;

public record RestauranteResponseDTO(
    Long id, 
   String nome, 
   String categoria, 
   EnderecoResponseDTO endereco, 
   String telefone, 
   BigDecimal taxaEntrega,
   boolean ativo, 
   Float avaliacao,
   List<ProdutoResponseDTO> produtos
) {
        /**
     * Converte uma entidade Restaurante em RestauranteResponseDTO
     */
    public static RestauranteResponseDTO fromEntity(Restaurante restaurante) {
        if (restaurante == null) {
            return null;
        }

        List<ProdutoResponseDTO> produtosDTO = ProdutoResponseDTO.fromEntities(restaurante.getProdutos());

        return new RestauranteResponseDTO(
            restaurante.getId(),
            restaurante.getNome(),
            restaurante.getCategoria(),
            EnderecoResponseDTO.fromEntity(restaurante.getEndereco()),
            restaurante.getTelefone(),
            restaurante.getTaxaEntrega(),
            restaurante.isAtivo(),
            restaurante.getAvaliacao(),
            produtosDTO
        );
    }

    /**
     * Converte uma lista de Restaurante para uma lista de RestauranteResponseDTO
     */
    public static List<RestauranteResponseDTO> fromEntities(List<Restaurante> restaurantes) {
        if (restaurantes == null || restaurantes.isEmpty()) {
            return List.of();
        }

        return restaurantes.stream()
                .map(RestauranteResponseDTO::fromEntity)
                .toList();
    }

}
