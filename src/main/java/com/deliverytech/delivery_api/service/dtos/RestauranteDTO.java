package com.deliverytech.delivery_api.service.dtos;

import java.math.BigDecimal;
import java.util.List;

public record RestauranteDTO(
   Long id, 
   String nome, 
   String categoria, 
   String endereco, 
   String telefone, 
   BigDecimal taxaEntrega,
   boolean ativo, 
   Float avaliacao,
   List<ProdutoDTO> produtos
) {} 
