package com.deliverytech.delivery_api.service.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.deliverytech.delivery_api.entity.Endereco;
import com.deliverytech.delivery_api.enums.StatusPedido;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record PedidoDTO(
    Long id,
    String numeroPedido,
    LocalDateTime dataPedido,
    StatusPedido status,
    BigDecimal valorTotal,
    String observacoes,
    @NotEmpty(message = "Endereço é obrigatório")   
    @Valid
    EnderecoDTO enderecoDeEntrega,
    @NotNull(message = "Cliente é obrigatório") 
    Long clienteId,
    @NotNull(message = "Restaurante é obrigatório") 
    Long restauranteId, 
    @NotNull(message = "Itens do pedido não informados.")
    @NotEmpty(message = "Pedido deve ter pelo menos um item") 
    @Valid
    List<ItemPedidoDTO> itens
) {
    public Endereco getEnderecoEntreDeEntraga(){
        return EnderecoDTO.fromEntity(enderecoDeEntrega);
    }
}