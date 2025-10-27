package com.deliverytech.delivery_api.service.interfaces;

import java.math.BigDecimal;
import java.util.List;

import com.deliverytech.delivery_api.service.dtos.ProdutoDTO;
import com.deliverytech.delivery_api.service.dtos.ProdutoResponseDTO;

public interface ProdutoServiceInterface {
    ProdutoResponseDTO cadastrar(ProdutoDTO produtoDTO, Long restauranteId);
    ProdutoResponseDTO atualizar(Long id, ProdutoDTO produto);
    void remover(Long id);
    ProdutoResponseDTO buscarPorId(Long id);   
    List<ProdutoResponseDTO> buscarProdutosPorRestaurante(Long restauranteId);
    List<ProdutoResponseDTO> buscarPorCategoria(String categoria);
    List<ProdutoResponseDTO> buscarPorNome(String nome);
    ProdutoResponseDTO alterarDisponibilidade(Long produtoId, boolean disponivel);
    List<ProdutoResponseDTO> buscarPorFaixaPreco(BigDecimal precoMin, BigDecimal precoMax);
}
