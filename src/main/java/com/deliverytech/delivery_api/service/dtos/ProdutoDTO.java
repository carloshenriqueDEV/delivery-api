package com.deliverytech.delivery_api.service.dtos;

import java.math.BigDecimal;
import java.util.List;

import com.deliverytech.delivery_api.entity.Produto;
import com.deliverytech.delivery_api.entity.Restaurante;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProdutoDTO(
    Long id, 
    @NotBlank(message = "Nome é obrigatório.")
    String nome, 
    String descricao, 
    BigDecimal preco, 
    @NotBlank(message = "Categoria é obrigatória.")
    String categoria, 
    boolean disponivel,
    @NotNull(message = "Restaurante não informados.")
    RestauranteDTO restaurante
) {
        /**
     * Converte uma entidade ProdutoDTO em Produto
     */
    public static Produto fromEntity(ProdutoDTO produto) {
        if (produto == null) {
            return null;
        }


        return new Produto(
            produto.nome(),
            produto.descricao(),
            produto.preco(),
            produto.categoria(),
            produto.disponivel(),
            RestauranteDTO.fromEntity(produto.restaurante())
        );
    }

    /**
     * Converte uma lista de ProdutoDTO para uma lista de Produto
     */
    public static List<Produto> fromEntities(List<ProdutoDTO> restaurantes) {
        if (restaurantes == null || restaurantes.isEmpty()) {
            return List.of();
        }

        return restaurantes.stream()
                .map(ProdutoDTO::fromEntity)
                .toList();
    }

    public Restaurante getRestauranteEntity(){
        return RestauranteDTO.fromEntity(restaurante);
    }
}