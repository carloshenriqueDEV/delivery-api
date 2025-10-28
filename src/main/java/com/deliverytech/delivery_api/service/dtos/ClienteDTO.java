package com.deliverytech.delivery_api.service.dtos;

import java.util.List;

import com.deliverytech.delivery_api.entity.Cliente;
import com.deliverytech.delivery_api.entity.Endereco;
import com.deliverytech.delivery_api.validation.ValidTelefone;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ClienteDTO(
    Long id,
    @NotBlank(message = "Nome é obrigatório.")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres.")
    String nome,
    @NotBlank(message = "Email é obrigatório.")
    @Email(message = "Email deve ter o formatodo válido.")
    String email,
    @NotBlank(message = "Telefone é obrigatório") 
    @ValidTelefone 
    String telefone,
    boolean ativo, 
    @NotEmpty(message = "Endereço é obrigatório")   
    @Valid
    List<EnderecoDTO> enderecos) { 
        
        /**
         * Converte uma entidade ClienteDTO para Cliente
         */
        public static Cliente mountEntity(ClienteDTO cliente) {
            if (cliente == null) {
                return null;
            }

            return new Cliente(
                cliente.nome,
                cliente.email,
                cliente.telefone,
                EnderecoDTO.fromEntities(cliente.enderecos)
            );
        }
        
        /**
         * Converte uma lista de ClienteDTO para uma lista de Cliente
         */
        public static java.util.List<Cliente> fromEntities(java.util.List<ClienteDTO> itens) {
            if (itens == null || itens.isEmpty()) {
                return java.util.List.of();
            }

            return itens.stream()
                    .map(ClienteDTO::mountEntity)
                    .toList();
        }

        public List<Endereco> getEndereco(){
            if(this.enderecos != null){
                return EnderecoDTO.fromEntities(enderecos);
            }

            return null;
        }
    }