package com.deliverytech.delivery_api.service.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.deliverytech.delivery_api.entity.Endereco;
import com.deliverytech.delivery_api.enums.StatusPedido;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record PedidoDTO(
    Long id,
    String numeroPedido,
    LocalDateTime dataPedido,
    StatusPedido status,
    BigDecimal valorTotal,
    @Size(max = 500, message = "Observações não podem exceder 500 caracteres")
    String observacoes,
    @NotNull(message = "Endereço é obrigatório")   
    @Valid
    EnderecoDTO enderecoDeEntrega,
    @NotNull(message = "Cliente é obrigatório") 
    @Positive(message = "Cliente ID deve ser positivo") 
    Long clienteId,
    @NotNull(message = "Restaurante é obrigatório") 
    @Positive(message = "Restaurante ID deve ser positivo") 
    Long restauranteId, 
    @NotNull(message = "Itens do pedido não informados.")
    @NotEmpty(message = "Pedido deve ter pelo menos um item") 
    @Valid
    List<ItemPedidoDTO> itens,
    @NotBlank(message = "Forma de pagamento é obrigatória") 
    @Pattern(regexp = "^(DINHEIRO|CARTAO_CREDITO|CARTAO_DEBITO|PIX)$", message = "Forma de pagamento deve ser: DINHEIRO, CARTAO_CREDITO, CARTAO_DEBITO ou PIX") 
    String formaPagamento
) {
    public Endereco getEnderecoEntreDeEntraga(){
        return EnderecoDTO.fromEntity(enderecoDeEntrega);
    }
}