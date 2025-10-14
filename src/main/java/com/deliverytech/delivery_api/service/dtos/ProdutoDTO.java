package com.deliverytech.delivery_api.service.dtos;

import java.math.BigDecimal;

public record ProdutoDTO(
    Long id, 
    String nome, 
    String descricao, 
    BigDecimal preco, 
    String categoria, 
    boolean disponivel,
    RestauranteDTO restaurante
) {}