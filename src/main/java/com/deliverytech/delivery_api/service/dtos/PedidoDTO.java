package com.deliverytech.delivery_api.service.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.deliverytech.delivery_api.enums.StatusPedido;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PedidoDTO(
    Long id,
    String numeroPedido,
    LocalDateTime dataPedido,
    StatusPedido status,
    BigDecimal valorTotal,
    String observacoes,
    @NotBlank(message = "Endereço de entrega é obrigatório") 
    @Size(max = 200, message = "Endereço deve ter no máximo 200 caracteres") 
    String EnderecoDeEntrega,
    @NotNull(message = "Cliente é obrigatório") 
    Long clienteId,
    @NotNull(message = "Restaurante é obrigatório") 
    Long restauranteId, 
    @NotNull(message = "Itens do pedido não informados.")
    @NotEmpty(message = "Pedido deve ter pelo menos um item") 
    @Valid
    List<ItemPedidoDTO> itens
) {}