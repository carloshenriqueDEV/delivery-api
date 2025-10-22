package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.enums.StatusPedido; 
import com.deliverytech.delivery_api.service.PedidoService;
import com.deliverytech.delivery_api.service.dtos.ItemPedidoDTO;
import com.deliverytech.delivery_api.service.dtos.PedidoDTO;
import com.deliverytech.delivery_api.service.dtos.PedidoResponseDTO;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.http.HttpStatus; 
import org.springframework.http.ResponseEntity; 
import org.springframework.web.bind.annotation.*; 
 
import java.util.List; 
 
@RestController 
@RequestMapping("/api/pedidos") 
@CrossOrigin(origins = "*") 
public class PedidoController { 
 
    @Autowired 
    private PedidoService pedidoService; 
 
    /** 
     * Criar novo pedido 
     */ 
    @PostMapping 
    public ResponseEntity<PedidoResponseDTO> criarPedido(@RequestBody @Valid PedidoDTO pedidoDTO) {       
            PedidoResponseDTO pedido = pedidoService.criarPedido(pedidoDTO); 
            return ResponseEntity.status(HttpStatus.CREATED).body(pedido); 
    } 
 
    /** 
     * Adicionar item ao pedido 
     */ 
    @PostMapping("/{pedidoId}/itens") 
    public ResponseEntity<PedidoResponseDTO> adicionarItem(@PathVariable Long pedidoId, 
                                         @Valid ItemPedidoDTO itemPedidoDTO) { 
        
        PedidoResponseDTO pedido = pedidoService.adicionarItem(pedidoId, itemPedidoDTO.produtoId(), itemPedidoDTO.quantidade()); 
        return ResponseEntity.ok(pedido); 
       
    } 
 
    /** 
     * Confirmar pedido 
     */ 
    @PatchMapping("/{pedidoId}/confirmar") 
    public ResponseEntity<PedidoResponseDTO> confirmarPedido(@PathVariable Long pedidoId) { 
  
        PedidoResponseDTO pedido = pedidoService.atualizarStatus(pedidoId, StatusPedido.CONFIRMADO, null); 
        return ResponseEntity.ok(pedido); 
    } 
 
    /** 
     * Buscar pedido por ID 
     */ 
    @GetMapping("/{id}") 
    public ResponseEntity<PedidoResponseDTO> buscarPorId(@PathVariable Long id) { 
        PedidoResponseDTO pedido = pedidoService.buscarPorId(id); 
        return ResponseEntity.ok(pedido); 
    } 
 
    /** 
     * Listar pedidos por cliente 
     */ 
    @GetMapping("/{clienteId}") 
    public ResponseEntity<List<PedidoResponseDTO>> listarPorCliente(@PathVariable Long clienteId) { 
        List<PedidoResponseDTO> pedidos = pedidoService.buscarPorCliente(clienteId); 
        return ResponseEntity.ok(pedidos); 
    } 
 
    /** 
     * Buscar pedido por número 
     */ 
    @GetMapping("/{numeroPedido}") 
    public ResponseEntity<PedidoResponseDTO> buscarPorNumero(@PathVariable String numeroPedido) { 
        PedidoResponseDTO pedido = pedidoService.buscarPorNumero(numeroPedido); 
        return ResponseEntity.ok(pedido);
  
    } 
 
    /** 
     * Atualizar status do pedido 
     */ 
    @PatchMapping("/{pedidoId}/status") 
    public ResponseEntity<PedidoResponseDTO> atualizarStatus(@PathVariable Long pedidoId, 
                                            @RequestParam StatusPedido status, @RequestParam String motivo) { 
        PedidoResponseDTO pedido = pedidoService.atualizarStatus(pedidoId, status, motivo); 
        return ResponseEntity.ok(pedido); 
   
    } 
 
    /** 
     * Cancelar pedido 
     */ 
    @PatchMapping("/{pedidoId}/cancelar") 
    public ResponseEntity<PedidoResponseDTO> cancelarPedido(@PathVariable Long pedidoId, 
                                           @RequestParam(required = false) String motivo) { 
        PedidoResponseDTO pedido = pedidoService.atualizarStatus(pedidoId,  StatusPedido.CANCELADO, motivo);  
        
        return ResponseEntity.ok(pedido); 
    } 

    @PostMapping("/valor-total-pedido")
    public ResponseEntity<PedidoResponseDTO> calcularTotalPedido(@RequestBody @Valid PedidoDTO pedidoDTO){
        PedidoResponseDTO pedido = pedidoService.calcularTotalPedido(pedidoDTO); 
        return ResponseEntity.status(HttpStatus.OK).body(pedido); 
    }
      
}