package com.deliverytech.delivery_api.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivery_api.service.dtos.RestauranteDTO;
import com.deliverytech.delivery_api.service.dtos.RestauranteResponseDTO;
import com.deliverytech.delivery_api.service.interfaces.RestauranteServiceInterface;
import org.springframework.web.bind.annotation.PutMapping;





@RestController
@RequestMapping("/api/restaurantes")
@CrossOrigin("*")
public class RestauranteController {
    private final RestauranteServiceInterface  restauranteService;

    public RestauranteController(RestauranteServiceInterface restauranteServiceInterface){
        this.restauranteService = restauranteServiceInterface;
    }

    @PostMapping()
    public ResponseEntity<RestauranteResponseDTO> cadastraRestaurante(@RequestBody RestauranteDTO restauranteDTO) {
       
       RestauranteResponseDTO restaurante = restauranteService.cadastrar(restauranteDTO);

       return ResponseEntity.status(HttpStatus.CREATED).body(restaurante);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<RestauranteResponseDTO> buscarPorId(@PathVariable Long id) {
       RestauranteResponseDTO restaurante = restauranteService.buscarPorId(id);
       return ResponseEntity.status(HttpStatus.CREATED).body(restaurante);
    }
    
    @GetMapping("/{id}/produtos")
    public ResponseEntity<RestauranteResponseDTO> buscarProdutos(@PathVariable Long id) {
        RestauranteResponseDTO restauranteDTO = restauranteService.buscarProdutos(id);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(restauranteDTO);
    }

    @GetMapping()
    public ResponseEntity<List<RestauranteResponseDTO>> buscarRestaurantes() {
        List<RestauranteResponseDTO> restauranteDTOs = restauranteService.listarDistponiveis();
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(restauranteDTOs);
    }
    
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<RestauranteResponseDTO>> buscarPorCategoria(@PathVariable String categoria) {
        List<RestauranteResponseDTO> restauranteDTOs = restauranteService.buscarPorCategoria(categoria);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(restauranteDTOs);
    }
    
    @PutMapping("")
    public ResponseEntity<RestauranteResponseDTO> atualizarRestaurante(@PathVariable Long id, @RequestBody RestauranteDTO restauranteDTO) {
        RestauranteResponseDTO restaurante = restauranteService.atualizar(id, restauranteDTO);
        
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(restaurante);
    }
    
     
    @GetMapping("/{id}/taxa-entrega/{cep}")
    public ResponseEntity<BigDecimal> calcularTaxaEntrega(@PathVariable Long id, @PathVariable String cep) {
        BigDecimal taxaDeEntrega = restauranteService.calcularTaxaDeEntrega(id, cep);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(taxaDeEntrega);
    }
    
}
