package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.entity.*;
import com.deliverytech.delivery_api.enums.StatusPedido;
import com.deliverytech.delivery_api.repository.*; 
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Service; 
import org.springframework.transaction.annotation.Transactional; 
import com.deliverytech.delivery_api.service.dtos.PedidoDTO;
 
import java.util.List; 
import java.util.Optional; 
 
@Service 
@Transactional 
public class PedidoService { 
 
    @Autowired 
    private PedidoRepository pedidoRepository; 
 
    @Autowired 
    private ClienteRepository clienteRepository; 
 
    @Autowired 
    private RestauranteRepository restauranteRepository; 
 
    @Autowired 
    private ProdutoRepository produtoRepository; 
 
    /** 
     * Criar novo pedido 
     */ 
    public PedidoDTO criarPedido(PedidoDTO pedidoDTO) { 
        //ElseThorw são valdações contra o banco, logo está ok estarem aqui.
        Cliente cliente = clienteRepository.findByIdAndAtivoTrue(pedidoDTO.clienteId()) 
            .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado: " + pedidoDTO.clienteId())); 
 
        Restaurante restaurante = restauranteRepository.findByIdAndAtivoTrue(pedidoDTO.restauranteId()) 
            .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado: " + pedidoDTO.restauranteId())); 
        
        //todo: refatorar para incluí-la na entidade. 
        if (!cliente.isAtivo()) { 
            throw new IllegalArgumentException("Cliente inativo não pode fazer pedidos"); 
        } 

        //todo: refatorar para incluí-la na entidade.
        if (!restaurante.getAtivo()) { 
            throw new IllegalArgumentException("Restaurante não está disponível"); 
        } 
 
        Pedido pedido = new Pedido(); 
        pedido.setCliente(cliente); 
        pedido.setRestaurante(restaurante); 
        pedido.setStatus(StatusPedido.PENDENTE); 

        pedidoRepository.save(pedido);

        return new PedidoDTO(
            pedido.getId(),
            pedido.getNumeroPedido(),
            pedido.getDataPedido(),
            pedido.getStatus(),
            pedido.getValorTotal(),
            pedido.getObservacoes(),
            pedido.getCliente().getId(),
            pedido.getRestaurante().getId()
        );
    } 
 
    /** 
     * Adicionar item ao pedido 
     */ 
    public PedidoDTO adicionarItem(Long pedidoId, Long produtoId, Integer quantidade) { 
        Pedido pedido = pedidoRepository.findById(pedidoId)
            .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado: " + pedidoId)); 
 
        Produto produto = produtoRepository.findById(produtoId) 
            .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: " + produtoId)); 
 
        if (!produto.getDisponivel()) { 
            throw new IllegalArgumentException("Produto não disponível: " + produto.getNome()); 
        } 
 
        if (quantidade <= 0) { 
            throw new IllegalArgumentException("Quantidade deve ser maior que zero"); 
        } 
 
        // Verificar se produto pertence ao mesmo restaurante do pedido 
        if (!produto.getRestaurante().getId().equals(pedido.getRestaurante().getId())) { 
            throw new IllegalArgumentException("Produto não pertence ao restaurante do pedido"); 
        } 
 
        ItemPedido item = new ItemPedido(); 
        item.setPedido(pedido); 
        item.setProduto(produto); 
        item.setQuantidade(quantidade); 
        item.setPrecoUnitario(produto.getPreco()); 
        item.calcularSubtotal(); 
 
        pedido.adicionarItem(item); 
        pedidoRepository.save(pedido);
        return new PedidoDTO(
            pedido.getId(),
            pedido.getNumeroPedido(),
            pedido.getDataPedido(),
            pedido.getStatus(),
            pedido.getValorTotal(),
            pedido.getObservacoes(),
            pedido.getCliente().getId(),
            pedido.getRestaurante().getId()
        ); 
    } 
 
    /** 
     * Confirmar pedido 
     */ 
    public PedidoDTO confirmarPedido(Long pedidoId) { 
        
        Pedido pedido = atualizarStatus(pedidoId, StatusPedido.CONFIRMADO, null);

        return new PedidoDTO(
            pedido.getId(),
            pedido.getNumeroPedido(),
            pedido.getDataPedido(),
            pedido.getStatus(),
            pedido.getValorTotal(),
            pedido.getObservacoes(),
            pedido.getCliente().getId(),
            pedido.getRestaurante().getId()
        ); 
    } 
 
    /** 
     * Buscar por ID 
     */ 
    @Transactional(readOnly = true) 
    public Optional<Pedido> buscarPorId(Long id) { 
        return pedidoRepository.findById(id); 
    } 
 
    /** 
     * Listar pedidos por cliente 
     */ 
    @Transactional(readOnly = true) 
    public List<PedidoDTO> listarPorCliente(Long clienteId) { 
        var cliente = clienteRepository.findById(clienteId)
            .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado: " + clienteId));
            
        return pedidoRepository.findByClienteOrderByDataPedidoDesc(cliente)
            .stream()
            .map(p -> new PedidoDTO(
                p.getId(),
                p.getNumeroPedido(),
                p.getDataPedido(),
                p.getStatus(),
                p.getValorTotal(),
                p.getObservacoes(),
                p.getCliente().getId(),
                p.getRestaurante().getId()
                // p.getItens().stream()
                //     .map(i -> new ItemPedidoDTO(
                //         i.getId(),
                //         i.getProduto().getId(),
                //         i.getQuantidade(),
                //         i.getPrecoUnitario(),
                //         i.getSubtotal()
                //     )).toList()
            ))
            .toList(); 
    } 
 
    /** 
     * Buscar por número do pedido 
     */ 
    @Transactional(readOnly = true) 
    public Optional<Pedido> buscarPorNumero(String numeroPedido) { 
        return Optional.ofNullable(pedidoRepository.findByNumeroPedido(numeroPedido)); 
    } 
 
    /** 
     * Cancelar pedido 
     */ 
    public PedidoDTO cancelarPedido(Long pedidoId, String motivo) { 
        Pedido pedido = atualizarStatus(pedidoId, StatusPedido.CANCELADO, motivo);
 
        return new PedidoDTO(
            pedido.getId(),
            pedido.getNumeroPedido(),
            pedido.getDataPedido(),
            pedido.getStatus(),
            pedido.getValorTotal(),
            pedido.getObservacoes(),
            pedido.getCliente().getId(),
            pedido.getRestaurante().getId()
        ); 
    } 

    /*
     * Atualizar status do pedido 
     */
    public Pedido atualizarStatus(Long pedidoId, StatusPedido novoStatus, String motivo) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
            .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado: " + pedidoId));

        // Todo: Refatorar para incluir na entidade da linha 202 a 245.
        if (pedido.getStatus() == StatusPedido.CANCELADO) {
            throw new IllegalArgumentException("Pedido cancelado não pode ter status alterado");
        }

        switch (novoStatus) {
            case CONFIRMADO:
                if (pedido.getStatus() != StatusPedido.PENDENTE) {
                    throw new IllegalArgumentException("Apenas pedidos pendentes podem ser confirmados");
                }

                if (pedido.getItens().isEmpty()) { 
                    throw new IllegalArgumentException("Pedido deve ter pelo menos um item"); 
                } 

                break;
            case PREPARANDO:
                if (pedido.getStatus() != StatusPedido.CONFIRMADO) {
                    throw new IllegalArgumentException("Apenas pedidos confirmados podem entrar em preparo");
                }
                break;
            case SAIU_PARA_ENTREGA:
                if (pedido.getStatus() != StatusPedido.PREPARANDO) {
                    throw new IllegalArgumentException("Apenas pedidos em preparo podem sair para entrega");
                }
                break;
            case ENTREGUE:
                if (pedido.getStatus() != StatusPedido.SAIU_PARA_ENTREGA) {
                    throw new IllegalArgumentException("Apenas pedidos que saíram para entrega podem ser entregues");
                }
                break;
            case CANCELADO:
                 if (pedido.getStatus() == StatusPedido.ENTREGUE) { 
                    throw new IllegalArgumentException("Pedido já entregue não pode ser cancelado"); 
                } 
 
                if (pedido.getStatus() == StatusPedido.CANCELADO) { 
                    throw new IllegalArgumentException("Pedido já está cancelado"); 
                } 

                if (motivo != null && !motivo.trim().isEmpty()) { 
                    pedido.setObservacoes(pedido.getObservacoes() + " | Cancelado: " + motivo); 
                }
                break;
            default:
                throw new IllegalArgumentException("Transição de status inválida");
        }

        pedido.setStatus(novoStatus);
        return pedidoRepository.save(pedido);
    }
}
