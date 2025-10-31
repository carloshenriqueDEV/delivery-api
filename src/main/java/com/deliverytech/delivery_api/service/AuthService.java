package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.entity.Usuario;
import com.deliverytech.delivery_api.repository.UsuarioRepository;
import com.deliverytech.delivery_api.service.dtos.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService; // delega leitura de UserDetails
    private final PasswordEncoder passwordEncoder;

    public boolean existsByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    @Transactional
    public Usuario criarUsuario(RegisterRequest registerRequest) {
        if (existsByEmail(registerRequest.getEmail())) {
            throw new IllegalArgumentException("Email já está em uso");
        }

        Usuario novo =  new Usuario(registerRequest.getEmail(), passwordEncoder.encode(registerRequest.getSenha()), registerRequest.getNome(), registerRequest.getRole());
      

        return usuarioRepository.save(novo);
    }

    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) {
        // método disponibilizado para controllers que já usam AuthService;
        // delega diretamente para UsuarioService (único UserDetailsService)
        return usuarioService.loadUserByUsername(email);
    }
}