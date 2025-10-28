package com.deliverytech.delivery_api.service.dtos;

import java.math.BigDecimal;
import java.util.List;

import com.deliverytech.delivery_api.entity.Produto;
import com.deliverytech.delivery_api.entity.Restaurante;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProdutoDTO(
    Long id, 
    @NotBlank(message = "Nome é obrigatório.")
     @Size(min = 2, max = 50, message = "Nome deve ter entre 2 e 50 caracteres")
    String nome, 
    @NotBlank(message = "Descrição é obrigatória") 
    @Size(min = 10, max = 500, message = "Descrição deve ter entre 10 e 500 caracteres")
    String descricao, 
    @NotNull(message = "Preço é obrigatório") 
    @DecimalMin(value = "0.01", message = "Preço deve ser maior que zero") 
    @DecimalMax(value = "500.00", message = "Preço não pode exceder R$ 500,00") 

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