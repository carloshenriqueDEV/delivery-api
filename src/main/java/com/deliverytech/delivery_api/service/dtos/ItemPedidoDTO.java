package com.deliverytech.delivery_api.service.dtos;

import java.math.BigDecimal;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ItemPedidoDTO(
    Long id,
    @NotNull(message = "Quantidade é obrigatória") 
    @Min(value = 1, message = "Quantidade deve ser pelo menos 1") 
    @Max(value = 50, message = "Quantidade máxima é 50") 
    int quantidade, 
    BigDecimal precoUnitario,
    BigDecimal subtotal,
    @NotNull(message = "Produto é obrigatório") 
    @Positive(message = "Produto ID deve ser positivo") 
    Long produtoId,
    @Size(max = 200, message = "Observações não podem exceder 200 caracteres") 
    String observacoes
) {} 