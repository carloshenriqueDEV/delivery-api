package com.deliverytech.delivery_api.service.interfaces;

import java.util.List;

import com.deliverytech.delivery_api.enums.StatusPedido;
import com.deliverytech.delivery_api.service.dtos.PedidoDTO;

public interface PedidoServiceInterface {
   PedidoDTO criarPedido(PedidoDTO dto);
   PedidoDTO buscarPorId(Long id);
   List<PedidoDTO> buscarPorCliente(Long clienteId) ;
   PedidoDTO adicionarItem(Long pedidoId, Long produtoId, Integer quantidade);
   PedidoDTO buscarPorNumero(String numeroPedido);
   PedidoDTO atualizarStatus(Long pedidoId, StatusPedido status, String motivo);
   PedidoDTO calcularTotalPedido(PedidoDTO pedidoDTO);
}
