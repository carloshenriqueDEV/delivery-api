package com.deliverytech.delivery_api.service.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.deliverytech.delivery_api.entity.Pedido;
import com.deliverytech.delivery_api.enums.StatusPedido;

public record PedidoResponseDTO(
     Long id,
    String numeroPedido,
    LocalDateTime dataPedido,
    StatusPedido status,
    BigDecimal valorTotal,
    String observacoes,
    EnderecoResponseDTO EnderecoDeEntrega,
    ClienteResponseDTO cliente,
    RestauranteResponseDTO restaurante, 
    List<ItemPedidoResponseDTO> itens
) {
    /**
     * Converte uma entidade Pedido para PedidoResponseDTO
     */
    public static PedidoResponseDTO fromEntity(Pedido pedido) {
        if (pedido == null) {
            return null;
        }

        List<ItemPedidoResponseDTO> itensDTO = ItemPedidoResponseDTO.fromEntities(pedido.getItens());

        return new PedidoResponseDTO(
            pedido.getId(),
            pedido.getNumeroPedido(),
            pedido.getDataPedido(),
            pedido.getStatus(),
            pedido.getValorTotal(),
            pedido.getObservacoes(),
            EnderecoResponseDTO.fromEntity(pedido.getEnderecoDeEntrega()),
            ClienteResponseDTO.fromEntity(pedido.getCliente()),
            RestauranteResponseDTO.fromEntity(pedido.getRestaurante()),
            itensDTO
        );
    }

    /**
     * Converte uma lista de Pedido para uma lista de PedidoResponseDTO
     */
    public static List<PedidoResponseDTO> fromEntities(List<Pedido> pedidos) {
        if (pedidos == null || pedidos.isEmpty()) {
            return List.of();
        }

        return pedidos.stream()
                .map(PedidoResponseDTO::fromEntity)
                .toList();
    }
} 