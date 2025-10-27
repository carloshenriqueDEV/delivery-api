package com.deliverytech.delivery_api.service.dtos;

import java.math.BigDecimal;
import java.util.List;

import com.deliverytech.delivery_api.entity.Endereco;
import com.deliverytech.delivery_api.entity.Restaurante;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Dados para cadastro de restaurante") 
public record RestauranteDTO(
   Long id, 
     @Schema(description = "Nome do restaurante", 
            example = "Pizza Express", 
            required = true) 
   @NotBlank(message = "Nome é obrigatório.")
   String nome, 

    @Schema(description = "Categoria do restaurante", 
            example = "Italiana", 
            allowableValues = {"Italiana", "Brasileira", "Japonesa", "Mexicana", "Árabe"}) 
   @NotBlank(message = "Categoria é obrigatória.")
   String categoria, 

     @Schema(description = "Endereço completo do restaurante", 
            example = "Rua das Flores, 123 - Centro")
   @NotEmpty(message = "Endereço é obrigatório")   
   @Valid
   EnderecoDTO endereco, 

   @Schema(description = "Telefone para contato", 
            example = "11999999999") 
   @NotBlank(message = "Telefone é obrigatório") 
   @Pattern(regexp = "\\d{10,11}", message = "Telefone deve ter 10 ou 11 dígitos") 
   String telefone, 

    @Schema(description = "Taxa de entrega em reais", 
            example = "5.50", 
            minimum = "0")
    @NotEmpty(message = "Taxa de entrega é obrigatória") 
    @DecimalMin(value = "0.0", message = "Taxa de entrega deve ser posi va") 
    BigDecimal taxaEntrega, 
    boolean ativo, 
    Float avaliacao,
    List<ProdutoDTO> produtos,
   
    @Schema(description = "Horário de funcionamento", 
            example = "08:00-22:00") 
    @NotBlank(message = "Horário de funcionamento é obrigatório") 
    String horarioFuncionamento
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
               restaurante.taxaEntrega,
               restaurante.horarioFuncionamento);
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
