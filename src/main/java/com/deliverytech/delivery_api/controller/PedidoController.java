package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.enums.StatusPedido; 
import com.deliverytech.delivery_api.service.PedidoService; 
import com.deliverytech.delivery_api.service.dtos.PedidoDTO;

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
    public ResponseEntity<?> criarPedido(@RequestBody @Valid PedidoDTO pedidoDTO) {       
            PedidoDTO pedido = pedidoService.criarPedido(pedidoDTO); 
            return ResponseEntity.status(HttpStatus.CREATED).body(pedido); 
    } 
 
    /** 
     * Adicionar item ao pedido 
     */ 
    @PostMapping("/{pedidoId}/itens") 
    public ResponseEntity<?> adicionarItem(@PathVariable Long pedidoId, 
                                          @RequestParam Long produtoId, 
                                          @RequestParam Integer quantidade) { 
        
        PedidoDTO pedido = pedidoService.adicionarItem(pedidoId, produtoId, quantidade); 
        return ResponseEntity.ok(pedido); 
       
    } 
 
    /** 
     * Confirmar pedido 
     */ 
    @PatchMapping("/{pedidoId}/confirmar") 
    public ResponseEntity<?> confirmarPedido(@PathVariable Long pedidoId) { 
  
        PedidoDTO pedido = pedidoService.atualizarStatus(pedidoId, StatusPedido.CONFIRMADO, null); 
        return ResponseEntity.ok(pedido); 
    } 
 
    /** 
     * Buscar pedido por ID 
     */ 
    @GetMapping("/{id}") 
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) { 
        PedidoDTO pedido = pedidoService.buscarPorId(id); 
        return ResponseEntity.ok(pedido); 
    } 
 
    /** 
     * Listar pedidos por cliente 
     */ 
    @GetMapping("/{clienteId}") 
    public ResponseEntity<List<PedidoDTO>> listarPorCliente(@PathVariable Long clienteId) { 
        List<PedidoDTO> pedidos = pedidoService.buscarPorCliente(clienteId); 
        return ResponseEntity.ok(pedidos); 
    } 
 
    /** 
     * Buscar pedido por número 
     */ 
    @GetMapping("/numero/{numeroPedido}") 
    public ResponseEntity<?> buscarPorNumero(@PathVariable String numeroPedido) { 
        PedidoDTO pedido = pedidoService.buscarPorNumero(numeroPedido); 
        return ResponseEntity.ok(pedido);
  
    } 
 
    /** 
     * Atualizar status do pedido 
     */ 
    @PatchMapping("/{pedidoId}/status") 
    public ResponseEntity<?> atualizarStatus(@PathVariable Long pedidoId, 
                                            @RequestParam StatusPedido status, @RequestParam String motivo) { 
        PedidoDTO pedido = pedidoService.atualizarStatus(pedidoId, status, motivo); 
        return ResponseEntity.ok(pedido); 
   
    } 
 
    /** 
     * Cancelar pedido 
     */ 
    @PatchMapping("/{pedidoId}/cancelar") 
    public ResponseEntity<?> cancelarPedido(@PathVariable Long pedidoId, 
                                           @RequestParam(required = false) String motivo) { 
        PedidoDTO pedido = pedidoService.atualizarStatus(pedidoId,  StatusPedido.CANCELADO, motivo);  
        
        return ResponseEntity.ok(pedido); 
    } 

    @PostMapping("/valor-total-pedido")
    public ResponseEntity<?> calcularTotalPedido(@Valid @RequestBody PedidoDTO pedidoDTO){
        PedidoDTO pedido = pedidoService.calcularTotalPedido(pedidoDTO); 
        return ResponseEntity.status(HttpStatus.OK).body(pedido); 
    }
      
}