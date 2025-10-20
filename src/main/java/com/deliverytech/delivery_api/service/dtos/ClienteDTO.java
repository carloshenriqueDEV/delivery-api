package com.deliverytech.delivery_api.service.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
    @Pattern(regexp = "\\d{10,11}", message = "Telefone deve ter 10 ou 11 dígitos") 
    String telefone,
    boolean ativo,
    @NotBlank(message = "Endereço é obrigatório") 
    @Size(max = 200, message = "Endereço deve ter no máximo 200 caracteres") 
    String endereco) { }

