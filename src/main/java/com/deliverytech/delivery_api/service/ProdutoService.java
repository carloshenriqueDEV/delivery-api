package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.entity.Produto; 
import com.deliverytech.delivery_api.entity.Restaurante;
import com.deliverytech.delivery_api.exception.EntityNotFoundException;
import com.deliverytech.delivery_api.repository.ProdutoRepository; 
import com.deliverytech.delivery_api.repository.RestauranteRepository;
import com.deliverytech.delivery_api.service.dtos.ProdutoDTO;
import com.deliverytech.delivery_api.service.dtos.ProdutoResponseDTO;
import com.deliverytech.delivery_api.service.dtos.RestauranteResponseDTO;
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
    
    @Autowired
    private RestauranteService restauranteService;
    /** 
     * Cadastrar novo produto 
     */ 
    @Override
    public ProdutoResponseDTO cadastrar(ProdutoDTO produtoDTO, Long restauranteId) { 
        Restaurante restaurante = restauranteRepository.findById(restauranteId) 
            .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado: " + restauranteId)); 

        Produto produto = new Produto(
            produtoDTO.nome(),
            produtoDTO.descricao(),
            produtoDTO.preco(),
            produtoDTO.categoria(),
            produtoDTO.disponivel(),
            restaurante
        );

       produtoRepository.save(produto);
 
        return  ProdutoResponseDTO.fromEntity(produto);
    } 
 
 
    /** 
     * Listar produtos por restaurante 
     */ 
    @Transactional(readOnly = true) 
    @Override
    public List<ProdutoResponseDTO> buscarProdutosPorRestaurante(Long restauranteId) { 
       
        RestauranteResponseDTO restaurante = restauranteService.buscarProdutos(restauranteId);

        return restaurante.produtos();
    } 
 
    /** 
     * Buscar por categoria 
     */ 
    @Transactional(readOnly = true) 
    @Override
    public List<ProdutoResponseDTO> buscarPorCategoria(String categoria) { 
        List<Produto> produtos = produtoRepository.findByCategoriaAndDisponivelTrue(categoria);

        if(produtos.isEmpty()) {
            throw new EntityNotFoundException("Nenhum produto encontrado na categoria: " + categoria);
        }

        return ProdutoResponseDTO.fromEntities(produtos);
    } 
 
    /** 
     * Atualizar produto 
     */ 
    @Override
    public ProdutoResponseDTO atualizar(Long id, ProdutoDTO produtoAtualizado) { 

        produtoRepository.findById(id) 
            .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado: " + id)); 
        
        //Todo: validar se o produto pertence ao restaurante

        Produto novaVersaoProduto =  new Produto(
            produtoAtualizado.nome(),
            produtoAtualizado.descricao(),
            produtoAtualizado.preco(),
            produtoAtualizado.categoria(),
            produtoAtualizado.disponivel(),
            produtoAtualizado.getRestauranteEntity()
        );

        novaVersaoProduto.setId(id);

        return ProdutoResponseDTO.fromEntity(novaVersaoProduto);
    } 
 
    /** 
     * Alterar disponibilidade 
     */ 
    @Override
    public ProdutoResponseDTO alterarDisponibilidade(Long produtoId, boolean disponivel) { 
        Produto produto = produtoRepository.findById(produtoId) 
            .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado: " + produtoId)); 
 
        produto.setDisponivel(disponivel); 
        produtoRepository.save(produto); 

        return ProdutoResponseDTO.fromEntity(produto);
    } 
 
    /** 
     * Buscar por faixa de preço 
     */ 
    @Transactional(readOnly = true) 
    @Override
    public List<ProdutoResponseDTO> buscarPorFaixaPreco(BigDecimal precoMin, BigDecimal precoMax) { 
        List<Produto> produtos = produtoRepository.findByPrecoBetweenAndDisponivelTrue(precoMin, precoMax);
        return produtos.stream()
            .map(p ->  ProdutoResponseDTO.fromEntity(p)).toList(); 
    }


    @Override
    public void remover(Long id) {
        Produto produto = produtoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado: " + id));

        produtoRepository.delete(produto);
    }


    @Override
    public ProdutoResponseDTO buscarPorId(Long id) {
        Produto produto = produtoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado: " + id));

        return ProdutoResponseDTO.fromEntity(produto);
    } 

    @Override
    public List<ProdutoResponseDTO> buscarPorNome(String nome){
        List<Produto> produtos = produtoRepository.findByNomeContainingIgnoreCaseAndDisponivelTrue(nome.toLowerCase());

        return ProdutoResponseDTO.fromEntities(produtos);

    }
 
}