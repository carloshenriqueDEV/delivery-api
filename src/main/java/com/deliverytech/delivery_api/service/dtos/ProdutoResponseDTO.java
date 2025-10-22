package com.deliverytech.delivery_api.service.dtos;

import java.math.BigDecimal;

import com.deliverytech.delivery_api.entity.Produto;

public record ProdutoResponseDTO(
    Long id,
    String nome, 
    String descricao, 
    BigDecimal preco, 
    String categoria, 
    boolean disponivel,
    RestauranteResponseDTO restaurante
) {
    /**
     * Converte uma entidade Produto em ProdutoResponseDTO
     */
    public static ProdutoResponseDTO fromEntity(Produto produto) {
        if (produto == null) {
            return null;
        }

        return new ProdutoResponseDTO(
            produto.getId(),
            produto.getNome(),
            produto.getDescricao(),
            produto.getPreco(),
            produto.getCategoria(),
            produto.getDisponivel(),
            RestauranteResponseDTO.fromEntity(produto.getRestaurante())
        );
    }

     /**
     * Converte uma lista de Produto para uma lista de ProdutoResponseDTO
     */
    public static java.util.List<ProdutoResponseDTO> fromEntities(java.util.List<Produto> itens) {
        if (itens == null || itens.isEmpty()) {
            return java.util.List.of();
        }

        return itens.stream()
                .map(ProdutoResponseDTO::fromEntity)
                .toList();
    }
}
