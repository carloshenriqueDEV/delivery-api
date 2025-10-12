package com.deliverytech.delivery_api.service.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.deliverytech.delivery_api.enums.StatusPedido;


public record PedidoDTO(
    Long id,
    String numeroPedido,
    LocalDateTime dataPedido,
    StatusPedido status,
    BigDecimal valorTotal,
    String observacoes,
    Long clienteId,
    Long restauranteId 
) {}