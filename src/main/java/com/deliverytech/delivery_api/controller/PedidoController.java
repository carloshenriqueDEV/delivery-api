package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.entity.Pedido; 
import com.deliverytech.delivery_api.enums.StatusPedido; 
import com.deliverytech.delivery_api.service.PedidoService; 
import com.deliverytech.delivery_api.service.dtos.PedidoDTO;
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.http.HttpStatus; 
import org.springframework.http.ResponseEntity; 
import org.springframework.web.bind.annotation.*; 
 
import java.util.List; 
import java.util.Optional; 
 
@RestController 
@RequestMapping("/pedidos") 
@CrossOrigin(origins = "*") 
public class PedidoController { 
 
    @Autowired 
    private PedidoService pedidoService; 
 
    /** 
     * Criar novo pedido 
     */ 
    @PostMapping 
    public ResponseEntity<?> criarPedido(@RequestBody PedidoDTO pedidoDTO) { 
        try { 
            PedidoDTO pedido = pedidoService.criarPedido(pedidoDTO); 
            return ResponseEntity.status(HttpStatus.CREATED).body(pedido); 
        } catch (IllegalArgumentException e) { 
            if(e.getMessage().contains("não encontrado")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro: " + e.getMessage());
            }

            return ResponseEntity.badRequest().body("Erro: " + e.getMessage()); 
        } catch (Exception e) { 
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) 
                .body("Erro interno do servidor"); 
        } 
    } 
 
    /** 
     * Adicionar item ao pedido 
     */ 
    @PostMapping("/{pedidoId}/itens") 
    public ResponseEntity<?> adicionarItem(@PathVariable Long pedidoId, 
                                          @RequestParam Long produtoId, 
                                          @RequestParam Integer quantidade) { 
        try { 
            PedidoDTO pedido = pedidoService.adicionarItem(pedidoId, produtoId, quantidade); 
            return ResponseEntity.ok(pedido); 
        } catch (IllegalArgumentException e) { 
           
            if(e.getMessage().contains("não encontrado")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro: " + e.getMessage());
            }

            return ResponseEntity.badRequest().body("Erro: " + e.getMessage()); 
        } catch (Exception e) { 
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) 
                .body("Erro interno do servidor"); 
        } 
    } 
 
    /** 
     * Confirmar pedido 
     */ 
    @PutMapping("/{pedidoId}/confirmar") 
    public ResponseEntity<?> confirmarPedido(@PathVariable Long pedidoId) { 
        try { 
            PedidoDTO pedido = pedidoService.confirmarPedido(pedidoId); 
            return ResponseEntity.ok(pedido); 
        } catch (IllegalArgumentException e) { 

            if(e.getMessage().contains("não encontrado")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro: " + e.getMessage());
            }

            return ResponseEntity.badRequest().body("Erro: " + e.getMessage()); 
        } catch (Exception e) { 
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) 
                .body("Erro interno do servidor"); 
        } 
    } 
 
    /** 
     * Buscar pedido por ID 
     */ 
    @GetMapping("/{id}") 
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) { 
        Optional<Pedido> pedido = pedidoService.buscarPorId(id); 
 
        if (pedido.isPresent()) { 
            return ResponseEntity.ok(pedido.get()); 
        } else { 
            return ResponseEntity.notFound().build(); 
        } 
    } 
 
    /** 
     * Listar pedidos por cliente 
     */ 
    @GetMapping("/cliente/{clienteId}") 
    public ResponseEntity<List<PedidoDTO>> listarPorCliente(@PathVariable Long clienteId) { 
        List<PedidoDTO> pedidos = pedidoService.listarPorCliente(clienteId); 
        return ResponseEntity.ok(pedidos); 
    } 
 
    /** 
     * Buscar pedido por número 
     */ 
    @GetMapping("/numero/{numeroPedido}") 
    public ResponseEntity<?> buscarPorNumero(@PathVariable String numeroPedido) { 
        Optional<Pedido> pedido = pedidoService.buscarPorNumero(numeroPedido); 
 
        if (pedido.isPresent()) { 
            return ResponseEntity.ok(pedido.get()); 
        } else { 
            return ResponseEntity.notFound().build(); 
        } 
    } 
 
    /** 
     * Atualizar status do pedido 
     */ 
    @PutMapping("/{pedidoId}/status") 
    public ResponseEntity<?> atualizarStatus(@PathVariable Long pedidoId, 
                                            @RequestParam StatusPedido status, @RequestParam String motivo) { 
        try { 
            Pedido pedido = pedidoService.atualizarStatus(pedidoId, status, motivo); 
            return ResponseEntity.ok(new PedidoDTO( 
                pedido.getId(), 
                pedido.getNumeroPedido(), 
                pedido.getDataPedido(), 
                pedido.getStatus(), 
                pedido.getValorTotal(), 
                pedido.getObservacoes(), 
                pedido.getCliente().getId(), 
                pedido.getRestaurante().getId() 
            )); 
        } catch (IllegalArgumentException e) { 

            if(e.getMessage().contains("não encontrado")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro: " + e.getMessage());
            }

            return ResponseEntity.badRequest().body("Erro: " + e.getMessage()); 
        } catch (Exception e) { 
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) 
                .body("Erro interno do servidor"); 
        } 
    } 
 
    /** 
     * Cancelar pedido 
     */ 
    @PutMapping("/{pedidoId}/cancelar") 
    public ResponseEntity<?> cancelarPedido(@PathVariable Long pedidoId, 
                                           @RequestParam(required = false) String motivo) { 
        try { 
            PedidoDTO pedido = pedidoService.cancelarPedido(pedidoId, motivo); 
            return ResponseEntity.ok(pedido); 
        } catch (IllegalArgumentException e) { 

            if(e.getMessage().contains("não encontrado")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro: " + e.getMessage());
            }
            
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage()); 
        } catch (Exception e) { 
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) 
                .body("Erro interno do servidor"); 
        } 
    } 
}