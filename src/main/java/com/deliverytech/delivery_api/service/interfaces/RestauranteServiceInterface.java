package com.deliverytech.delivery_api.service.interfaces;
import java.math.BigDecimal;
import java.util.List;

import com.deliverytech.delivery_api.service.dtos.RestauranteDTO;
import com.deliverytech.delivery_api.service.dtos.RestauranteResponseDTO;

public interface RestauranteServiceInterface {
    RestauranteResponseDTO cadastrar(RestauranteDTO restauranteDto);
    RestauranteResponseDTO buscarPorId(Long id);
    List<RestauranteResponseDTO> buscarPorCategoria(String categoria);
    List<RestauranteResponseDTO> listarDistponiveis();
    RestauranteResponseDTO atualizar(Long id, RestauranteDTO restauranteDto);
    void inativar(Long id);
    RestauranteResponseDTO buscarProdutos(Long id);
    BigDecimal calcularTaxaDeEntrega(Long id, String cepCliente);

}
