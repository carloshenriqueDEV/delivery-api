package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.enums.StatusPedido; 
import com.deliverytech.delivery_api.service.PedidoService;
import com.deliverytech.delivery_api.service.dtos.ItemPedidoDTO;
import com.deliverytech.delivery_api.service.dtos.PedidoDTO;
import com.deliverytech.delivery_api.service.dtos.PedidoResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus; 
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement; 

import java.time.LocalDateTime;
import java.util.List; 

//Todo: incluir paginação nos endpoints que trazem uma grande massa de dados
//Todo: incluir api response wrapper para todos os endpoint
//Todo: remover endpoints redundantes
//Todo: incluir versionamento de api
@RestController 
@RequestMapping("/api/pedidos") 
@Tag(name = "Pedidos", description = "Operações relacionadas ao gerenciamento de pedidos")
@CrossOrigin(origins = "*") 
public class PedidoController { 
 
    @Autowired 
    private PedidoService pedidoService; 
 
    /** 
     * Criar novo pedido 
     */ 
    @PostMapping 
    @Operation(summary = "Criar pedido", 
        description = "Cria um novo pedido no sistema",                
        security = @SecurityRequirement(name = "Bearer Authentication"), 
        tags = {"Pedidos"} 
        ) 
    @ApiResponses({ 
        @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso"), 
        @ApiResponse(responseCode = "400", description = "Dados inválidos"), 
        @ApiResponse(responseCode = "404", description = "Cliente ou restaurante não encontrado"), 
        @ApiResponse(responseCode = "409", description = "Produto indisponível") 
    }) 
    @PreAuthorize("hasRole('CLIENTE') or hasRole('ADMIN')")
    public ResponseEntity<PedidoResponseDTO> criarPedido(@RequestBody @Valid 
            @io.swagger.v3.oas.annotations.parameters.RequestBody( 
                description = "Dados do pedido a ser criado" 
            ) PedidoDTO pedidoDTO) {       
            PedidoResponseDTO pedido = pedidoService.criarPedido(pedidoDTO); 
            return ResponseEntity.status(HttpStatus.CREATED).body(pedido); 
    } 
 
    /** 
     * Confirmar pedido 
     */ 
    @PatchMapping("/{pedidoId}/confirmar") 
    @Operation(summary = "Confirma recebimento do pedido", 
               description = "Atualiza o status de um pedido para confirmado") 
    @ApiResponses({ 
        @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso"), 
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado"), 
        @ApiResponse(responseCode = "400", description = "Transição de status inválida") 
    })
    @PreAuthorize("hasRole('RESTAURANTE')") 
    public ResponseEntity<PedidoResponseDTO> confirmarPedido(@PathVariable Long pedidoId) { 
  
        PedidoResponseDTO pedido = pedidoService.atualizarStatus(pedidoId, StatusPedido.CONFIRMADO, null); 
        return ResponseEntity.ok(pedido); 
    } 

    /** 
     * Preparar pedido 
     */ 
    @PatchMapping("/{pedidoId}/preperar") 
    @Operation(
        summary = "Informa o início do preparo do pedido", 
        description = "Atualiza o status de um pedido para preparando", 
        security = @SecurityRequirement(name = "Bearer Authentication"), 
        tags = {"Pedidos"}
        ) 
    @ApiResponses({ 
        @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso"), 
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado"), 
        @ApiResponse(responseCode = "400", description = "Transição de status inválida") 
    })
    @PreAuthorize("hasRole('RESTAURANTE')") 
    public ResponseEntity<PedidoResponseDTO> PrepararPedido(@PathVariable Long pedidoId) { 
  
        PedidoResponseDTO pedido = pedidoService.atualizarStatus(pedidoId, StatusPedido.PREPARANDO, null); 
        return ResponseEntity.ok(pedido); 
    } 

    /** 
     * Enviar para entrega pedido 
     */ 
    @PatchMapping("/{pedidoId}/enviar-para-entrega") 
    @Operation(
        summary = "Informa que o pedido saiu para entrega", 
        description = "Atualiza o status de um pedido para saiu para entrega",
        security = @SecurityRequirement(name = "Bearer Authentication"), 
        tags = {"Pedidos"}) 
    @ApiResponses({ 
        @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso"), 
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado"), 
        @ApiResponse(responseCode = "400", description = "Transição de status inválida") 
    })
    @PreAuthorize("hasRole('RESTAURANTE')") 
    public ResponseEntity<PedidoResponseDTO> EnviarParaEntregaPedido(@PathVariable Long pedidoId) { 
  
        PedidoResponseDTO pedido = pedidoService.atualizarStatus(pedidoId, StatusPedido.SAIU_PARA_ENTREGA, null); 
        return ResponseEntity.ok(pedido); 
    } 

    /** 
     * Cancelar pedido 
     */ 
    @PatchMapping("/{pedidoId}/cancelar") 
    @Operation(
        summary = "Informar o cancelamento de um pedido", 
        description = "Atualiza o status de um pedido para cancelado",
        security = @SecurityRequirement(name = "Bearer Authentication"), 
        tags = {"Pedidos"}) 
    @ApiResponses({ 
        @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso"), 
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado"), 
        @ApiResponse(responseCode = "400", description = "Transição de status inválida") 
    })
    @PreAuthorize("hasRole('CLIENTE')") 
    public ResponseEntity<PedidoResponseDTO> CancelarPedido(@PathVariable Long pedidoId, @Parameter(description = "Motivo da alteração. (obrigatório apenas para cancelamento.)") @RequestParam String motivo) { 
  
        PedidoResponseDTO pedido = pedidoService.atualizarStatus(pedidoId, StatusPedido.CANCELADO, motivo); 
        return ResponseEntity.ok(pedido); 
    } 

    /** 
     * Entregar pedido 
     */ 
    @PatchMapping("/{pedidoId}/Entregar") 
    @Operation(
        summary = "Informa que o pedido foi entregue", 
        description = "Atualiza o status de um pedido para entregue",
        security = @SecurityRequirement(name = "Bearer Authentication"), 
        tags = {"Pedidos"}) 
    @ApiResponses({ 
        @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso"), 
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado"), 
        @ApiResponse(responseCode = "400", description = "Transição de status inválida") 
    })
    @PreAuthorize("hasRole('ENTREGADOR')") 
    public ResponseEntity<PedidoResponseDTO> EntregarPedido(@PathVariable Long pedidoId) { 
  
        PedidoResponseDTO pedido = pedidoService.atualizarStatus(pedidoId, StatusPedido.ENTREGUE, null); 
        return ResponseEntity.ok(pedido); 
    } 
 
    /** 
     * Buscar pedido por ID 
     */ 
    @GetMapping("/{id}") 
    @Operation(
        summary = "Buscar pedido por ID", 
        description = "Recupera um pedido específico com todos os detalhes",
        security = @SecurityRequirement(name = "Bearer Authentication"), 
        tags = {"Pedidos"}) 
    @ApiResponses({ 
        @ApiResponse(responseCode = "200", description = "Pedido encontrado"), 
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado") 
    })
    public ResponseEntity<PedidoResponseDTO> buscarPorId( @Parameter(description = "ID do pedido")  @PathVariable Long id) { 
        PedidoResponseDTO pedido = pedidoService.buscarPorId(id); 
        return ResponseEntity.ok(pedido); 
    } 
 
    /** 
     * Listar pedidos por cliente 
     */ 
    @GetMapping("/{clienteId}") 
    @Operation(summary = "Histórico do cliente", 
        description = "Lista todos os pedidos de um cliente",
        security = @SecurityRequirement(name = "Bearer Authentication"), 
        tags = {"Pedidos"}
    ) 
    @ApiResponses({ 
        @ApiResponse(responseCode = "200", description = "Histórico recuperado com sucesso"), 
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado") 
    })
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<List<PedidoResponseDTO>> listarPorCliente( @Parameter(description = "ID do cliente")  @PathVariable Long clienteId) { 
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

    @PostMapping("/calcular")
    @Operation(summary = "Calcular total do pedido", 
               description = "Calcula o total de um pedido sem salvá-lo") 
    @ApiResponses({ 
        @ApiResponse(responseCode = "200", description = "Total calculado com sucesso"), 
        @ApiResponse(responseCode = "400", description = "Dados inválidos"), 
        @ApiResponse(responseCode = "404", description = "Produto não encontrado") 
    })
    public ResponseEntity<PedidoResponseDTO> calcularTotalPedido(
        @RequestBody 
        @Valid 
        @io.swagger.v3.oas.annotations.parameters.RequestBody( 
                description = "Itens para cálculo" 
            )  PedidoDTO pedidoDTO){
        PedidoResponseDTO pedido = pedidoService.calcularTotalPedido(pedidoDTO); 
        return ResponseEntity.status(HttpStatus.OK).body(pedido); 
    }

    @GetMapping 
    @Operation(summary = "Listar pedidos", 
               description = "Lista pedidos") 
    @ApiResponses({ 
        @ApiResponse(responseCode = "200", description = "Lista recuperada com sucesso") 
    }) 
    @PreAuthorize("hasRole('ADMIN')") 
    public ResponseEntity<List<PedidoResponseDTO>> listar( 
            @Parameter(description = "Status do pedido") 
            @RequestParam(required = false) StatusPedido status, 
            @Parameter(description = "Data e hora inicial") 
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime dataInicio, 
            @Parameter(description = "Data e hora final") 
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime dataFim 
            ) { 
 
    List<PedidoResponseDTO> pedidos = pedidoService.listaPedidos(status, dataInicio, dataFim); 
 
 
        return ResponseEntity.ok(pedidos); 
    } 
      
      @GetMapping("/restaurante/{restauranteId}") 
    @Operation(summary = "Pedidos do restaurante", 
               description = "Lista todos os pedidos de um restaurante") 
    @ApiResponses({ 
        @ApiResponse(responseCode = "200", description = "Pedidos recuperados com sucesso"), 
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado") 
    }) 
    @PreAuthorize("hasRole('RESTAURANTE')") 
    public ResponseEntity<List<PedidoResponseDTO>> buscarPorRestaurante( 
        @Parameter(description = "ID do restaurante") 
        @PathVariable Long restauranteId, 
        @Parameter(description = "Status do pedido") 
        @RequestParam(required = false) StatusPedido status) { 
 
            List<PedidoResponseDTO> pedidos = 
                pedidoService.buscarPedidosPorRestaurante(restauranteId, status); 
    
            return ResponseEntity.ok(pedidos); 
    }
}