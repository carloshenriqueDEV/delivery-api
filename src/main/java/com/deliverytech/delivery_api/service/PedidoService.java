package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.entity.*;
import com.deliverytech.delivery_api.enums.StatusPedido;
import com.deliverytech.delivery_api.repository.*; 
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Service; 
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivery_api.service.dtos.ItemPedidoDTO;
import com.deliverytech.delivery_api.service.dtos.PedidoDTO;
import com.deliverytech.delivery_api.service.interfaces.PedidoServiceInterface;

import java.util.List;
@Service 
@Transactional 
public class PedidoService implements PedidoServiceInterface { 
 
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
    @Override
    public PedidoDTO criarPedido(PedidoDTO pedidoDTO) { 
        Cliente cliente = clienteRepository.findByIdAndAtivoTrue(pedidoDTO.clienteId()) 
            .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado: " + pedidoDTO.clienteId())); 
 
        Restaurante restaurante = restauranteRepository.findByIdAndAtivoTrue(pedidoDTO.restauranteId()) 
            .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado: " + pedidoDTO.restauranteId()));   
            
        List<Produto> produtos = produtoRepository.findAllById(
            pedidoDTO.itens().stream()
                .map(ItemPedidoDTO::produtoId)
                .toList()
        );

        if (produtos.isEmpty()) {
            throw new IllegalArgumentException("Nenhum produto encontrado para os itens informados.");
        }

        // Verifica se há algum produto inativo
        boolean algumInativo = produtos.stream()
            .anyMatch(produto -> !produto.getDisponivel());

        if (algumInativo) {
            throw new IllegalStateException("O pedido contém produtos inativos.");
        }

        List<ItemPedido> itens = pedidoDTO.itens().stream()
            .map(itemDTO -> {
                Produto produto = produtos.stream()
                    .filter(p -> p.getId().equals(itemDTO.produtoId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(
                        "Produto com ID " + itemDTO.produtoId() + " não encontrado."
                    ));

               
                ItemPedido item = new ItemPedido();
                item.setProduto(produto);
                item.setQuantidade(itemDTO.quantidade());
                item.setPrecoUnitario(produto.getPreco()); 
                item.calcularSubtotal(); 

                return item;
            })
            .toList();
       

        Pedido pedido = new Pedido(cliente,restaurante, itens,StatusPedido.PENDENTE, pedidoDTO.observacoes()); 

        pedidoRepository.save(pedido);

        return new PedidoDTO(
            pedido.getId(),
            pedido.getNumeroPedido(),
            pedido.getDataPedido(),
            pedido.getStatus(),
            pedido.getValorTotal(),
            pedido.getObservacoes(),
            pedido.getCliente().getId(),
            pedido.getRestaurante().getId(),
            pedidoDTO.itens()
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
            pedido.getRestaurante().getId(),
            pedido.getItens().stream()
                .map(i -> new ItemPedidoDTO(
                    item.getId(),
                    item.getQuantidade(),
                    item.getPrecoUnitario(),
                    item.getSubtotal(),
                    item.getProduto().getId()
                ))
                .toList()
        ); 
    } 
    
    /** 
     * Buscar por ID 
     */ 
    @Transactional(readOnly = true) 
    @Override
    public PedidoDTO buscarPorId(Long id) { 
        Pedido pedido = pedidoRepository.buscarPedidoCompleto(id)
            .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado: " + id));        

        return new PedidoDTO(
                pedido.getId(),
                pedido.getNumeroPedido(),
                pedido.getDataPedido(),
                pedido.getStatus(),
                pedido.getValorTotal(),
                pedido.getObservacoes(),
                pedido.getCliente().getId(),
                pedido.getRestaurante().getId(),
                pedido.getItens() == null ? 
                    List.of() : 
                    pedido.getItens().stream()
                        .map(item -> new ItemPedidoDTO(
                                item.getId(),
                                item.getQuantidade(),
                                item.getPrecoUnitario(),
                                item.getSubtotal(),
                                item.getProduto().getId()
                        ))
                        .toList()
        );
    } 
 
    /** 
     * Listar pedidos por cliente 
     */ 
    @Transactional(readOnly = true) 
    @Override
    public List<PedidoDTO> buscarPorCliente(Long clienteId) { 
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
                p.getRestaurante().getId(),
                null
            ))
            .toList(); 
    } 
 
    /** 
     * Buscar por número do pedido 
     */ 
    @Transactional(readOnly = true) 
    @Override
    public PedidoDTO buscarPorNumero(String numeroPedido) { 
        Pedido pedido = pedidoRepository.findByNumeroPedido(numeroPedido)
            .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado: " + numeroPedido));

        return new PedidoDTO(
            pedido.getId(),
            pedido.getNumeroPedido(),
            pedido.getDataPedido(),
            pedido.getStatus(),
            pedido.getValorTotal(),
            pedido.getObservacoes(),
            pedido.getCliente().getId(),
            pedido.getRestaurante().getId(),
            pedido.getItens().stream()
                .map(i -> new ItemPedidoDTO(
                    i.getId(),
                    i.getQuantidade(),
                    i.getPrecoUnitario(),
                    i.getSubtotal(),
                    i.getProduto().getId()
                ))
                .toList()
        );
        
    } 
 
  
    @Override
    public PedidoDTO atualizarStatus(Long pedidoId, StatusPedido status, String motivo) {
         Pedido pedido = pedidoRepository.buscarPedidoCompleto(pedidoId)
            .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado: " + pedidoId)); 
        
        pedido.setStatus(status, motivo);
        pedidoRepository.save(pedido);

        return new PedidoDTO(
            pedido.getId(),
            pedido.getNumeroPedido(),
            pedido.getDataPedido(),
            pedido.getStatus(),
            pedido.getValorTotal(),
            pedido.getObservacoes(),
            pedido.getCliente().getId(),
            pedido.getRestaurante().getId(),
            pedido.getItens().stream()
                .map(i -> new ItemPedidoDTO(
                    i.getId(),
                    i.getQuantidade(),
                    i.getPrecoUnitario(),
                    i.getSubtotal(),
                    i.getProduto().getId()
                ))
                .toList()
        ); 
    } 

    
}
