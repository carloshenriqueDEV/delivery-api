package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.entity.Restaurante;
import com.deliverytech.delivery_api.external.DistanceApiClient;
import com.deliverytech.delivery_api.repository.RestauranteRepository;
import com.deliverytech.delivery_api.service.dtos.ProdutoDTO;
import com.deliverytech.delivery_api.service.dtos.RestauranteDTO;
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
    public RestauranteDTO buscarPorId(Long id) { 
       var restaurante = restauranteRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado: " + id)); 
         
        return new RestauranteDTO( 
            restaurante.getId(), 
            restaurante.getNome(), 
            restaurante.getCategoria(), 
            restaurante.getEndereco(), 
            restaurante.getTelefone(), 
            restaurante.getTaxaEntrega(),
            restaurante.isAtivo(),
            restaurante.getAvaliacao(),
            null
        );
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

    @Override
    public RestauranteDTO buscarProdutos(Long id) {
        Restaurante restaurante = restauranteRepository.findRestauranteComProdutosDisponiveis(id)
        .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado: " + id));

        List<ProdutoDTO> produtos =  restaurante.getProdutos().stream().map(p -> new ProdutoDTO(
                p.getId(),
                p.getNome(), p.getDescricao(), p.getPreco(), p.getCategoria(), p.getDisponivel(), null))
                .toList();

        return new RestauranteDTO( 
            restaurante.getId(), 
            restaurante.getNome(), 
            restaurante.getCategoria(), 
            restaurante.getEndereco(), 
            restaurante.getTelefone(), 
            restaurante.getTaxaEntrega(),
            restaurante.isAtivo(),
            restaurante.getAvaliacao(),
            produtos
        );
    } 

    public BigDecimal calcularTaxaDeEntrega(Long id, String cepCliente){
        Restaurante restaurante = restauranteRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado: " + id));

        BigDecimal distancia = this.distanceApiClient.calcularDistanciaKm(restaurante.getEndereco(),cepCliente);
        
        return restaurante.calcularTaxaDeEntrega(distancia, BigDecimal.valueOf(1));
    }
  
} 