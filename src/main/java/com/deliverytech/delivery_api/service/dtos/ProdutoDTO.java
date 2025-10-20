package com.deliverytech.delivery_api.service.dtos;

import java.math.BigDecimal;

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
) {}