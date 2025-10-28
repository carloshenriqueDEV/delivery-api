package com.deliverytech.delivery_api.service.interfaces;

import java.time.LocalDateTime;
import java.util.List;

import com.deliverytech.delivery_api.enums.StatusPedido;
import com.deliverytech.delivery_api.service.dtos.PedidoDTO;
import com.deliverytech.delivery_api.service.dtos.PedidoResponseDTO;

public interface PedidoServiceInterface {
   PedidoResponseDTO criarPedido(PedidoDTO dto);
   PedidoResponseDTO buscarPorId(Long id);
   List<PedidoResponseDTO> buscarPorCliente(Long clienteId) ;
   PedidoResponseDTO adicionarItem(Long pedidoId, Long produtoId, Integer quantidade);
   PedidoResponseDTO buscarPorNumero(String numeroPedido);
   PedidoResponseDTO atualizarStatus(Long pedidoId, StatusPedido status, String motivo);
   PedidoResponseDTO calcularTotalPedido(PedidoDTO pedidoDTO);
   List<PedidoResponseDTO> listaPedidos(StatusPedido status, LocalDateTime inicio, LocalDateTime fim );
   List<PedidoResponseDTO> buscarPedidosPorRestaurante(Long restauranteId, StatusPedido status);
}
