package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.entity.Restaurante; 
import com.deliverytech.delivery_api.repository.RestauranteRepository;
import com.deliverytech.delivery_api.service.dtos.RestauranteDTO;
import com.deliverytech.delivery_api.service.interfaces.RestauranteServiceInterface;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Service; 
import org.springframework.transaction.annotation.Transactional; 
import java.util.List; 
import java.util.Optional; 
 
@Service 
@Transactional 
public class RestauranteService implements RestauranteServiceInterface { 
 
    @Autowired 
    private RestauranteRepository restauranteRepository; 
 
    /** 
     * Cadastrar novo restaurante 
     */ 
    @Override
    public RestauranteDTO cadastrar(RestauranteDTO restauranteDTO) { 
        // Validar nome único 
        if (restauranteRepository.findByNome(restauranteDTO.nome()).isPresent()) { 
            throw new IllegalArgumentException("Restaurante já cadastrado: " + 
                restauranteDTO.nome()); 
        } 
        var restaurante = new Restaurante(
            restauranteDTO.nome(),
            restauranteDTO.categoria(),
            restauranteDTO.endereco(),
            restauranteDTO.telefone(),
            restauranteDTO.taxaEntrega()
        );

        restauranteRepository.save(restaurante); 
 
        return new RestauranteDTO( 
            restaurante.getId(), 
            restaurante.getNome(), 
            restaurante.getCategoria(), 
            restaurante.getEndereco(), 
            restaurante.getTelefone(), 
            restaurante.getTaxaEntrega(),
            restaurante.isAtivo(),
            null,
            null
        );
    } 
 
    /** 
     * Buscar por ID 
     */ 
    @Transactional(readOnly = true) 
    @Override
    public Optional<RestauranteDTO> buscarPorId(Long id) { 
       var restaurante = restauranteRepository.findById(id); 
       
        if (restaurante.isEmpty()) {
            throw new IllegalArgumentException("Restaurante não encontrado: " + id);
        }

        return restaurante.map(r -> new RestauranteDTO( 
            r.getId(), 
            r.getNome(), 
            r.getCategoria(), 
            r.getEndereco(), 
            r.getTelefone(), 
            r.getTaxaEntrega(),
            r.isAtivo(),
            r.getAvaliacao(),
            null
        ));
    } 
 
    /** 
     * Listar restaurantes a vos 
     */ 
    @Transactional(readOnly = true) 
    @Override
    public List<RestauranteDTO> listarDistponiveis() { 
        return restauranteRepository.findByAtivoTrue()
            .stream() 
            .map(r -> new RestauranteDTO( 
                r.getId(), 
                r.getNome(), 
                r.getCategoria(), 
                r.getEndereco(), 
                r.getTelefone(), 
                r.getTaxaEntrega(),
                r.isAtivo(),
                r.getAvaliacao(),
                null
            )) 
            .toList(); 
    } 
 
    /** 
     * Buscar por categoria 
     */ 
    @Transactional(readOnly = true) 
    @Override
    public List<RestauranteDTO> buscarPorCategoria(String categoria) { 
        return restauranteRepository.findByCategoriaAndAtivoTrue(categoria)
            .stream() 
            .map(r -> new RestauranteDTO( 
                r.getId(), 
                r.getNome(), 
                r.getCategoria(), 
                r.getEndereco(), 
                r.getTelefone(), 
                r.getTaxaEntrega(),
                r.isAtivo(),
                r.getAvaliacao(),
                null
            )) 
            .toList(); 
    } 
 
    /** 
     * Atualizar restaurante 
     */ 
    @Override
    public RestauranteDTO atualizar(RestauranteDTO restauranteAtualizado) { 
        Restaurante restaurante = restauranteRepository.findById(restauranteAtualizado.id())
            .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado: " + restauranteAtualizado.id())); 
 
        // Verificar nome único (se mudou) 
        if (!restaurante.getNome().equals(restauranteAtualizado.nome()) && 
            restauranteRepository.findByNome(restauranteAtualizado.nome()).isPresent()) { 
            throw new IllegalArgumentException("Nome já cadastrado: " + 
            restauranteAtualizado.nome()); 
        } 
 
        restaurante.setNome(restauranteAtualizado.nome()); 
        restaurante.setCategoria(restauranteAtualizado.categoria()); 
        restaurante.setEndereco(restauranteAtualizado.endereco()); 
        restaurante.setTelefone(restauranteAtualizado.telefone()); 
        restaurante.setTaxaEntrega(restauranteAtualizado.taxaEntrega());
        restaurante.ativar(); 

        restauranteRepository.save(restaurante);

        return  restauranteAtualizado;
    } 
 
    /** 
     * Inativar restaurante 
     */ 
    @Override
    public void inativar(Long id) { 
        Restaurante restaurante = restauranteRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado: " + id)); 
 
        restaurante.setAtivo(false); 
        restauranteRepository.save(restaurante); 
    } 
  
} 