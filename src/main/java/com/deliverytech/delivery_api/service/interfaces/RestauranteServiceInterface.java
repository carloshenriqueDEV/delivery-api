package com.deliverytech.delivery_api.service.interfaces;
import java.math.BigDecimal;
import java.util.List;

import com.deliverytech.delivery_api.service.dtos.RestauranteDTO;

public interface RestauranteServiceInterface {
    RestauranteDTO cadastrar(RestauranteDTO restauranteDto);
    RestauranteDTO buscarPorId(Long id);
    List<RestauranteDTO> buscarPorCategoria(String categoria);
    List<RestauranteDTO> listarDistponiveis();
    RestauranteDTO atualizar(RestauranteDTO restauranteDto);
    void inativar(Long id);
    RestauranteDTO buscarProdutos(Long id);
    BigDecimal calcularTaxaDeEntrega(Long id, String cepCliente);

}
