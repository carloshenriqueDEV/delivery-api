package com.deliverytech.delivery_api.service.interfaces;

import java.util.List;
import java.util.Optional;
import com.deliverytech.delivery_api.service.dtos.ClienteDTO;

public interface ClienteServiceInterface {
    ClienteDTO cadastrar(ClienteDTO clienteDto);
    Optional<ClienteDTO> buscarPorId(Long id);
    Optional<ClienteDTO> buscarPorEmail(String email);
    ClienteDTO atualizar(ClienteDTO clienteAtualizado);
    void ativarDesativar(Long id);
    Iterable<ClienteDTO> listarAtivos();
    List<ClienteDTO> buscarPorNome(String nome);

}
