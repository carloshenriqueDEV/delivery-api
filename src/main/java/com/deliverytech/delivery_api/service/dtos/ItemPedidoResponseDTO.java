package com.deliverytech.delivery_api.service.dtos;

import java.math.BigDecimal;

import com.deliverytech.delivery_api.entity.ItemPedido;

public record ItemPedidoResponseDTO(
    Long id,
    int quantidade, 
    BigDecimal precoUnitario,
    BigDecimal subtotal,
    ProdutoResponseDTO produto
) {
     /**
     * Converte uma entidade ItemPedido em ItemPedidoResponseDTO
     */
    public static ItemPedidoResponseDTO fromEntity(ItemPedido itemPedido) {
        if (itemPedido == null) {
            return null;
        }

        return new ItemPedidoResponseDTO(
            itemPedido.getId(),
            itemPedido.getQuantidade(),
            itemPedido.getPrecoUnitario(),
            itemPedido.getSubtotal(),
            ProdutoResponseDTO.fromEntity(itemPedido.getProduto())
        );
    }
    
    /**
     * Converte uma lista de ItemPedido para uma lista de ItemPedidoResponseDTO
     */
    public static java.util.List<ItemPedidoResponseDTO> fromEntities(java.util.List<ItemPedido> itens) {
        if (itens == null || itens.isEmpty()) {
            return java.util.List.of();
        }

        return itens.stream()
                .map(ItemPedidoResponseDTO::fromEntity)
                .toList();
    }
}  
