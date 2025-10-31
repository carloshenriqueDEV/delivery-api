package com.deliverytech.delivery_api.service.interfaces;

import com.deliverytech.delivery_api.entity.Usuario;
import com.deliverytech.delivery_api.service.dtos.RegisterRequest;
import com.deliverytech.delivery_api.service.dtos.UserResponse;

public interface UsuarioServiceInterface {
    Usuario loadUserByUsername(String nome);
    Usuario loadUserByUserEmail(String email);
    UserResponse criarUsuario(RegisterRequest usuario);

}
