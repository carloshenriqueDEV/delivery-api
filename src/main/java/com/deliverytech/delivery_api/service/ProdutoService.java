package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.entity.Produto; 
import com.deliverytech.delivery_api.entity.Restaurante; 
import com.deliverytech.delivery_api.repository.ProdutoRepository; 
import com.deliverytech.delivery_api.repository.RestauranteRepository;
import com.deliverytech.delivery_api.service.dtos.ProdutoDTO;
import com.deliverytech.delivery_api.service.dtos.RestauranteDTO;
import com.deliverytech.delivery_api.service.interfaces.ProdutoServiceInterface;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Service; 
import org.springframework.transaction.annotation.Transactional; 
 
import java.math.BigDecimal; 
import java.util.List; 
 
@Service 
@Transactional 
public class ProdutoService implements ProdutoServiceInterface { 
 
    @Autowired 
    private ProdutoRepository produtoRepository; 
 
    @Autowired 
    private RestauranteRepository restauranteRepository; 
 
    /** 
     * Cadastrar novo produto 
     */ 
    @Override
    public ProdutoDTO cadastrar(ProdutoDTO produtoDTO, Long restauranteId) { 
        Restaurante restaurante = restauranteRepository.findById(restauranteId) 
            .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado: " + restauranteId)); 

        Produto produto = new Produto(
            produtoDTO.nome(),
            produtoDTO.descricao(),
            produtoDTO.preco(),
            produtoDTO.categoria(),
            produtoDTO.disponivel(),
            restaurante
        );

       produtoRepository.save(produto);
 
        return  new ProdutoDTO( 
            produto.getId(), 
            produto.getNome(), 
            produto.getDescricao(), 
            produto.getPreco(), 
            produto.getCategoria(), 
            produto.getDisponivel(),
            new RestauranteDTO(
                restaurante.getId(),
                restaurante.getNome(),
                restaurante.getCategoria(),
                restaurante.getEndereco(),
                restaurante.getTelefone(),
                restaurante.getTaxaEntrega(),
                restaurante.isAtivo(),
                null,
                null
            )
        );
    } 
 
 
    /** 
     * Listar produtos por restaurante 
     */ 
    @Transactional(readOnly = true) 
    @Override
    public List<ProdutoDTO> buscarProdutosPorRestaurante(Long restauranteId) { 
        Restaurante restaurante = restauranteRepository.findById(restauranteId)
            .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado: " + restauranteId));

        List<Produto> produtos = produtoRepository.findByRestauranteIdAndDisponivelTrue(restauranteId); 

        return produtos.stream()
            .map(p ->  new ProdutoDTO( 
            p.getId(), 
            p.getNome(), 
            p.getDescricao(), 
            p.getPreco(), 
            p.getCategoria(), 
            p.getDisponivel(),
            new RestauranteDTO(
                restaurante.getId(),
                restaurante.getNome(),
                restaurante.getCategoria(),
                restaurante.getEndereco(),
                restaurante.getTelefone(),
                restaurante.getTaxaEntrega(),
                restaurante.isAtivo(),
                null,
                null
            )
        )
        ).toList(); 
    } 
 
    /** 
     * Buscar por categoria 
     */ 
    @Transactional(readOnly = true) 
    @Override
    public List<ProdutoDTO> buscarPorCategoria(String categoria) { 
        List<Produto> produtos = produtoRepository.findByCategoriaAndDisponivelTrue(categoria);

        if(produtos.isEmpty()) {
            throw new IllegalArgumentException("Nenhum produto encontrado na categoria: " + categoria);
        }

        return produtos.stream()
            .map(p ->  new ProdutoDTO( 
            p.getId(), 
            p.getNome(), 
            p.getDescricao(), 
            p.getPreco(), 
            p.getCategoria(), 
            p.getDisponivel(),
            p.getRestaurante() == null ? null : new RestauranteDTO(
                p.getRestaurante().getId(),
                p.getRestaurante().getNome(),
                p.getRestaurante().getCategoria(),
                p.getRestaurante().getEndereco(),
                p.getRestaurante().getTelefone(),
                p.getRestaurante().getTaxaEntrega(),
                p.getRestaurante().isAtivo(),
                null,
                null
        )
        )).toList();
    } 
 
    /** 
     * Atualizar produto 
     */ 
    @Override
    public ProdutoDTO atualizar(ProdutoDTO produtoAtualizado) { 
        Produto produto = produtoRepository.findById(produtoAtualizado.id()) 
            .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: " + produtoAtualizado.id())); 
        
        
 
        produto.setNome(produtoAtualizado.nome()); 
        produto.setDescricao(produtoAtualizado.descricao()); 
        produto.setPreco(produtoAtualizado.preco()); 
        produto.setCategoria(produtoAtualizado.categoria()); 
        produto.setDisponivel(produtoAtualizado.disponivel());
        produtoRepository.save(produto);
        return new ProdutoDTO( 
            produto.getId(), 
            produto.getNome(), 
            produto.getDescricao(), 
            produto.getPreco(), 
            produto.getCategoria(), 
            produto.getDisponivel(),
           null
        );
    } 
 
    /** 
     * Alterar disponibilidade 
     */ 
    @Override
    public ProdutoDTO alterarDisponibilidade(Long produtoId, boolean disponivel) { 
        Produto produto = produtoRepository.findById(produtoId) 
            .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: " + produtoId)); 
 
        produto.setDisponivel(disponivel); 
        produtoRepository.save(produto); 

        return new ProdutoDTO( 
            produto.getId(), 
            produto.getNome(), 
            produto.getDescricao(), 
            produto.getPreco(), 
            produto.getCategoria(), 
            produto.getDisponivel(),
           null
        );
    } 
 
    /** 
     * Buscar por faixa de preço 
     */ 
    @Transactional(readOnly = true) 
    @Override
    public List<ProdutoDTO> buscarPorFaixaPreco(BigDecimal precoMin, BigDecimal precoMax) { 
        List<Produto> produtos = produtoRepository.findByPrecoBetweenAndDisponivelTrue(precoMin, precoMax);
        return produtos.stream()
            .map(p ->  new ProdutoDTO( 
            p.getId(), 
            p.getNome(), 
            p.getDescricao(), 
            p.getPreco(), 
            p.getCategoria(), 
            p.getDisponivel(),
            p.getRestaurante() == null ? null : new RestauranteDTO(
                p.getRestaurante().getId(),
                p.getRestaurante().getNome(),
                p.getRestaurante().getCategoria(),
                p.getRestaurante().getEndereco(),
                p.getRestaurante().getTelefone(),
                p.getRestaurante().getTaxaEntrega(),
                p.getRestaurante().isAtivo(),
                null,
                null
        )
        )).toList(); 
    }


    @Override
    public void remover(Long id) {
        Produto produto = produtoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: " + id));

        produtoRepository.delete(produto);
    }


    @Override
    public ProdutoDTO buscarPorId(Long id) {
        Produto produto = produtoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: " + id));

        return new ProdutoDTO(
            produto.getId(),
            produto.getNome(),
            produto.getDescricao(),
            produto.getPreco(),
            produto.getCategoria(),
            produto.getDisponivel(),
            produto.getRestaurante() == null ? null : new RestauranteDTO(
                produto.getRestaurante().getId(),
                produto.getRestaurante().getNome(),
                produto.getRestaurante().getCategoria(),
                produto.getRestaurante().getEndereco(),
                produto.getRestaurante().getTelefone(),
                produto.getRestaurante().getTaxaEntrega(),
                produto.getRestaurante().isAtivo(),
                null,
                null
        )
        );
    } 
 
}