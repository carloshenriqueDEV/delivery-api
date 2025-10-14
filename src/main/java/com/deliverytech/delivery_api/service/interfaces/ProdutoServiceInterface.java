package com.deliverytech.delivery_api.service.interfaces;

import java.math.BigDecimal;
import java.util.List;

import com.deliverytech.delivery_api.service.dtos.ProdutoDTO;

public interface ProdutoServiceInterface {
    ProdutoDTO cadastrar(ProdutoDTO produtoDTO, Long restauranteId);
    ProdutoDTO atualizar(ProdutoDTO produto);
    void remover(Long id);
    ProdutoDTO buscarPorId(Long id);   
    List<ProdutoDTO> buscarProdutosPorRestaurante(Long restauranteId);
    List<ProdutoDTO> buscarPorCategoria(String categoria);
    ProdutoDTO alterarDisponibilidade(Long produtoId, boolean disponivel);
    List<ProdutoDTO> buscarPorFaixaPreco(BigDecimal precoMin, BigDecimal precoMax);
}
