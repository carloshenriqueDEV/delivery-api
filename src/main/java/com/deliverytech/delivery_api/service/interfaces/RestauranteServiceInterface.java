package com.deliverytech.delivery_api.service.interfaces;
import java.math.BigDecimal;
import java.util.List;

import com.deliverytech.delivery_api.service.dtos.RestauranteDTO;
import com.deliverytech.delivery_api.service.dtos.RestauranteResponseDTO;

public interface RestauranteServiceInterface {
    RestauranteResponseDTO cadastrar(RestauranteDTO restauranteDto);
    RestauranteResponseDTO buscarPorId(Long id);
    List<RestauranteResponseDTO> buscarPorCategoria(String categoria);
    List<RestauranteResponseDTO> listar(String categoria, Boolean ativo);
    RestauranteResponseDTO atualizar(Long id, RestauranteDTO restauranteDto);
    RestauranteResponseDTO ativarDesativar(Long id, Boolean ativo);
    RestauranteResponseDTO buscarProdutos(Long id);
    BigDecimal calcularTaxaDeEntrega(Long id, String cepCliente);
    List<RestauranteResponseDTO> buscarRestaurantesProximos(String cep, Integer raio);

}
