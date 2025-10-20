package com.deliverytech.delivery_api.service.dtos;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RestauranteDTO(
   Long id, 
   @NotBlank(message = "Nome é obrigatório.")
   String nome, 
   @NotBlank(message = "Categoria é obrigatória.")
   String categoria, 
    @NotBlank(message = "Endereço é obrigatório") 
    @Size(max = 200, message = "Endereço deve ter no máximo 200 caracteres") 
   String endereco, 
   @NotBlank(message = "Telefone é obrigatório") 
   @Pattern(regexp = "\\d{10,11}", message = "Telefone deve ter 10 ou 11 dígitos") 
   String telefone, 
   BigDecimal taxaEntrega,
   boolean ativo, 
   Float avaliacao,
   List<ProdutoDTO> produtos
) {} 
