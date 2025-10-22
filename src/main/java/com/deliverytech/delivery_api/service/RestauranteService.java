package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.entity.Restaurante;
import com.deliverytech.delivery_api.external.DistanceApiClient;
import com.deliverytech.delivery_api.repository.RestauranteRepository;
import com.deliverytech.delivery_api.service.dtos.RestauranteDTO;
import com.deliverytech.delivery_api.service.dtos.RestauranteResponseDTO;
import com.deliverytech.delivery_api.service.interfaces.RestauranteServiceInterface;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Service; 
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List; 
@Service 
@Transactional 
public class RestauranteService implements RestauranteServiceInterface { 
 
    @Autowired 
    private RestauranteRepository restauranteRepository; 

    @Autowired
    private DistanceApiClient distanceApiClient;
 
    /** 
     * Cadastrar novo restaurante 
     */ 
    @Override
    public RestauranteResponseDTO cadastrar(RestauranteDTO restauranteDTO) { 
        // Validar nome único 
        if (restauranteRepository.findByNome(restauranteDTO.nome()).isPresent()) { 
            throw new IllegalArgumentException("Restaurante já cadastrado: " + 
                restauranteDTO.nome()); 
        } 
        Restaurante restaurante = new Restaurante(
            restauranteDTO.nome(),
            restauranteDTO.categoria(),
            restauranteDTO.getEndereco(),
            restauranteDTO.telefone(),
            restauranteDTO.taxaEntrega()
        );

        restauranteRepository.save(restaurante); 
 
        return RestauranteResponseDTO.fromEntity(restaurante);
    } 
 
    /** 
     * Buscar por ID 
     */ 
    @Transactional(readOnly = true) 
    @Override
    public RestauranteResponseDTO buscarPorId(Long id) { 
       Restaurante restaurante = restauranteRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado: " + id)); 
         
        return RestauranteResponseDTO.fromEntity(restaurante);
    } 
 
    /** 
     * Listar restaurantes a vos 
     */ 
    @Transactional(readOnly = true) 
    @Override
    public List<RestauranteResponseDTO> listarDistponiveis() { 
        return restauranteRepository.findByAtivoTrue()
            .stream() 
            .map(r -> RestauranteResponseDTO.fromEntity(r)) 
            .toList(); 
    } 
 
    /** 
     * Buscar por categoria 
     */ 
    @Transactional(readOnly = true) 
    @Override
    public List<RestauranteResponseDTO> buscarPorCategoria(String categoria) { 
        return restauranteRepository.findByCategoriaAndAtivoTrue(categoria)
            .stream() 
            .map(r -> RestauranteResponseDTO.fromEntity(r)) 
            .toList(); 
    } 
 
    /** 
     * Atualizar restaurante 
     */ 
    @Override
    public RestauranteResponseDTO atualizar(Long id, RestauranteDTO restauranteAtualizado) { 
        Restaurante restaurante = restauranteRepository.findByIdAndAtivoTrue(id)
            .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado: " + restauranteAtualizado.id())); 
 
        // Verificar nome único (se mudou) 
        if (!restaurante.getNome().equals(restauranteAtualizado.nome()) && 
            restauranteRepository.findByNome(restauranteAtualizado.nome()).isPresent()) { 
            throw new IllegalArgumentException("Nome já cadastrado: " + 
            restauranteAtualizado.nome()); 
        } 
        
        Restaurante novaVersaoRestaurante = new Restaurante(
            restauranteAtualizado.nome(),
            restauranteAtualizado.categoria(),
            restauranteAtualizado.getEndereco(),
            restauranteAtualizado.telefone(),
            restauranteAtualizado.taxaEntrega()
        );

        novaVersaoRestaurante.setId(id);

        restauranteRepository.save(novaVersaoRestaurante);

        return  RestauranteResponseDTO.fromEntity(novaVersaoRestaurante);
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

    @Override
    public RestauranteResponseDTO buscarProdutos(Long id) {
        Restaurante restaurante = restauranteRepository.findRestauranteComProdutosDisponiveis(id)
        .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado: " + id));

        return RestauranteResponseDTO.fromEntity(restaurante);
    } 

    public BigDecimal calcularTaxaDeEntrega(Long id, String cepCliente){
        Restaurante restaurante = restauranteRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado: " + id));

        BigDecimal distancia = this.distanceApiClient.calcularDistanciaKm(restaurante.getEndereco().getCep() ,cepCliente);
        
        return restaurante.calcularTaxaDeEntrega(distancia, BigDecimal.valueOf(1));
    }
  
} 