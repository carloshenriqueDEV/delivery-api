package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.entity.*;
import com.deliverytech.delivery_api.enums.StatusPedido;
import com.deliverytech.delivery_api.repository.*; 
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Service; 
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivery_api.service.dtos.ItemPedidoDTO;
import com.deliverytech.delivery_api.service.dtos.PedidoDTO;
import com.deliverytech.delivery_api.service.dtos.PedidoResponseDTO;
import com.deliverytech.delivery_api.service.interfaces.PedidoServiceInterface;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Autowired
    private RestauranteService restauranteService;
 
    /** 
     * Criar novo pedido 
     */ 
    @Override
    public PedidoResponseDTO criarPedido( PedidoDTO pedidoDTO) { 
        Cliente cliente = clienteRepository.findByIdAndAtivoTrue(pedidoDTO.clienteId()) 
            .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado: " + pedidoDTO.clienteId())); 
 
        Restaurante restaurante = restauranteRepository.findByIdAndAtivoTrue(pedidoDTO.restauranteId()) 
            .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado: " + pedidoDTO.restauranteId()));   

        if(pedidoDTO.itens().isEmpty() || pedidoDTO.itens() == null ){
            throw new IllegalArgumentException("Itens do pedido não informados.");
        }
            
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
       
        
        BigDecimal taxaDeEntrega = restauranteService.calcularTaxaDeEntrega(restaurante.getId(), pedidoDTO.enderecoDeEntrega().cep());
            
        Pedido pedido = new Pedido(cliente,restaurante, itens,StatusPedido.PENDENTE, pedidoDTO.observacoes(), taxaDeEntrega, pedidoDTO.getEnderecoEntreDeEntraga()); 

        pedidoRepository.save(pedido);

        return  PedidoResponseDTO.fromEntity(pedido);
    } 
 
    /** 
     * Adicionar item ao pedido 
     */ 
    public PedidoResponseDTO adicionarItem(Long pedidoId, Long produtoId, Integer quantidade) { 
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
        return PedidoResponseDTO.fromEntity(pedido);
    } 
    
    /** 
     * Buscar por ID 
     */ 
    @Transactional(readOnly = true) 
    @Override
    public PedidoResponseDTO buscarPorId(Long id) { 
        Pedido pedido = pedidoRepository.buscarPedidoCompleto(id)
            .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado: " + id));        

        return PedidoResponseDTO.fromEntity(pedido);
    } 
 
    /** 
     * Listar pedidos por cliente 
     */ 
    @Transactional(readOnly = true) 
    @Override
    public List<PedidoResponseDTO> buscarPorCliente(Long clienteId) { 
        var cliente = clienteRepository.findById(clienteId)
            .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado: " + clienteId));
            
        return pedidoRepository.findByClienteOrderByDataPedidoDesc(cliente)
            .stream()
            .map(p -> PedidoResponseDTO.fromEntity(p))
            .toList(); 
    } 
 
    /** 
     * Buscar por número do pedido 
     */ 
    @Transactional(readOnly = true) 
    @Override
    public PedidoResponseDTO buscarPorNumero(String numeroPedido) { 
        Pedido pedido = pedidoRepository.findByNumeroPedido(numeroPedido)
            .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado: " + numeroPedido));

        return PedidoResponseDTO.fromEntity(pedido);
        
    } 
 
  
    @Override
    public PedidoResponseDTO atualizarStatus(Long pedidoId, StatusPedido status, String motivo) {
         Pedido pedido = pedidoRepository.buscarPedidoCompleto(pedidoId)
            .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado: " + pedidoId)); 
        
        pedido.setStatus(status, motivo);
        pedidoRepository.save(pedido);

        return PedidoResponseDTO.fromEntity(pedido);
    } 

    public  PedidoResponseDTO calcularTotalPedido(PedidoDTO pedidoDTO){
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
       
        BigDecimal taxaDeEntrega = restauranteService.calcularTaxaDeEntrega(restaurante.getId(), pedidoDTO.enderecoDeEntrega().cep());

        Pedido pedido = new Pedido(cliente,restaurante, itens,StatusPedido.PENDENTE, pedidoDTO.observacoes(), taxaDeEntrega, pedidoDTO.getEnderecoEntreDeEntraga()); 

        return PedidoResponseDTO.fromEntity(pedido);
    }

    @Override
    public List<PedidoResponseDTO> listaPedidos(StatusPedido status, LocalDateTime inicio, LocalDateTime fim) {
        List<Pedido> pedidos = pedidoRepository.findByStatusAndDataPedidoBetweenOrderByDataPedidoDesc(status, inicio, fim);

        return PedidoResponseDTO.fromEntities(pedidos);
    }

    @Override
    public List<PedidoResponseDTO> buscarPedidosPorRestaurante(Long restauranteId, StatusPedido status) {
        List<Pedido> pedidos = pedidoRepository.findByRestauranteId(restauranteId, status)
        .orElseThrow(() -> new IllegalArgumentException("Pedidos não encontrados para o restaurante de ID " + restauranteId));

        return PedidoResponseDTO.fromEntities(pedidos);

    }
}
