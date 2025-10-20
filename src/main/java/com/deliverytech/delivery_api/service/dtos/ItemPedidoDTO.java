package com.deliverytech.delivery_api.service.dtos;

import java.math.BigDecimal;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ItemPedidoDTO(
    Long id,
    @NotNull(message = "Quantidade é obrigatória") 
    @Min(value = 1, message = "Quantidade deve ser pelo menos 1") 
    @Max(value = 10, message = "Quantidade máxima é 10") 
    int quantidade, 
    BigDecimal precoUnitario,
    BigDecimal subtotal,
    @NotNull(message = "Produto é obrigatório") 
    Long produtoId
) {} 