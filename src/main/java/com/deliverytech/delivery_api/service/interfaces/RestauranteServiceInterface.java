package com.deliverytech.delivery_api.service.interfaces;
import java.util.List;
import java.util.Optional;

import com.deliverytech.delivery_api.service.dtos.RestauranteDTO;

public interface RestauranteServiceInterface {
    RestauranteDTO cadastrar(RestauranteDTO restauranteDto);
    Optional<RestauranteDTO> buscarPorId(Long id);
    List<RestauranteDTO> buscarPorCategoria(String categoria);
    List<RestauranteDTO> listarDistponiveis();
    RestauranteDTO atualizar(RestauranteDTO restauranteDto);
    void inativar(Long id);

}
