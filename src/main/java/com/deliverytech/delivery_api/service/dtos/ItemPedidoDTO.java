package com.deliverytech.delivery_api.service.dtos;

import java.math.BigDecimal;

public record ItemPedidoDTO(
    Long id,
    int quantidade, 
    BigDecimal precoUnitario,
    BigDecimal subtotal,
    Long produtoId
) {} 