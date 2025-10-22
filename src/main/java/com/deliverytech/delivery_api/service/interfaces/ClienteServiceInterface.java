package com.deliverytech.delivery_api.service.interfaces;

import java.util.List;
import java.util.Optional;
import com.deliverytech.delivery_api.service.dtos.ClienteDTO;
import com.deliverytech.delivery_api.service.dtos.ClienteResponseDTO;

public interface ClienteServiceInterface {
    ClienteResponseDTO cadastrar(ClienteDTO clienteDto);
    Optional<ClienteResponseDTO> buscarPorId(Long id);
    Optional<ClienteResponseDTO> buscarPorEmail(String email);
    ClienteResponseDTO atualizar(Long id, ClienteDTO clienteAtualizado);
    void ativarDesativar(Long id);
    Iterable<ClienteResponseDTO> listarAtivos();
    List<ClienteResponseDTO> buscarPorNome(String nome);

}
