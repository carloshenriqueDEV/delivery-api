package com.deliverytech.delivery_api.service.dtos;

import java.math.BigDecimal;
import java.util.List;

import com.deliverytech.delivery_api.entity.Endereco;
import com.deliverytech.delivery_api.entity.Restaurante;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public record RestauranteDTO(
   Long id, 
   @NotBlank(message = "Nome é obrigatório.")
   String nome, 
   @NotBlank(message = "Categoria é obrigatória.")
   String categoria, 
   @NotEmpty(message = "Endereço é obrigatório")   
   @Valid
   EnderecoDTO endereco, 
   @NotBlank(message = "Telefone é obrigatório") 
   @Pattern(regexp = "\\d{10,11}", message = "Telefone deve ter 10 ou 11 dígitos") 
   String telefone, 
   BigDecimal taxaEntrega,
   boolean ativo, 
   Float avaliacao,
   List<ProdutoDTO> produtos
) {

    /**
         * Converte uma entidade RestauranteDTO para Restaurante
         */
        public static Restaurante fromEntity(RestauranteDTO restaurante) {
            if (restaurante == null) {
                return null;
            }

            return new Restaurante(
               restaurante.nome, 
               restaurante.categoria, 
               EnderecoDTO.fromEntity(restaurante.endereco),
               restaurante.telefone,
               restaurante.taxaEntrega);
        }
        
        /**
         * Converte uma lista de RestauranteDTO para uma lista de Restaurante
         */
        public static java.util.List<Restaurante> fromEntities(java.util.List<RestauranteDTO> itens) {
            if (itens == null || itens.isEmpty()) {
                return java.util.List.of();
            }

            return itens.stream()
                    .map(RestauranteDTO::fromEntity)
                    .toList();
        }

        public Endereco getEndereco(){
            if(this.endereco != null){
                return EnderecoDTO.fromEntity(endereco);
            }

            return null;
        } 
} 
