package com.deliverytech.delivery_api.service.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginDTO(
   @NotBlank(message = "Email é obrigatório") 
    @Email(message = "Email deve ter formato válido") 
    String email,  
    @NotBlank(message = "Senha é obrigatória") 
    String senha   
) {}
