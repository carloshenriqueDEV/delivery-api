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
    public ResponseEntity<RestauranteDTO> cadastraRestaurante(@RequestBody RestauranteDTO restauranteDTO) {
       
       RestauranteDTO restaurante = restauranteService.cadastrar(restauranteDTO);

       return ResponseEntity.status(HttpStatus.CREATED).body(restaurante);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<RestauranteDTO> buscarPorId(@PathVariable Long id) {
       RestauranteDTO restaurante = restauranteService.buscarPorId(id);
       return ResponseEntity.status(HttpStatus.CREATED).body(restaurante);
    }
    
    @GetMapping("/{id}/produtos")
    public ResponseEntity<RestauranteDTO> buscarProdutos(@PathVariable Long id) {
        RestauranteDTO restauranteDTO = restauranteService.buscarProdutos(id);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(restauranteDTO);
    }

    @GetMapping()
    public ResponseEntity<List<RestauranteDTO>> buscarRestaurantes() {
        List<RestauranteDTO> restauranteDTOs = restauranteService.listarDistponiveis();
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(restauranteDTOs);
    }
    
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<RestauranteDTO>> buscarPorCategoria(@PathVariable String categoria) {
        List<RestauranteDTO> restauranteDTOs = restauranteService.buscarPorCategoria(categoria);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(restauranteDTOs);
    }
    
    @PutMapping("")
    public ResponseEntity<RestauranteDTO> atualizarRestaurante( @RequestBody RestauranteDTO restauranteDTO) {
        RestauranteDTO restaurante = restauranteService.atualizar(restauranteDTO);
        
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(restaurante);
    }
    
    //Todo 
    @GetMapping("/{id}/taxa-entrega/{cep}")
    public ResponseEntity<BigDecimal> getMethodName(@PathVariable Long id, @PathVariable String cep) {
        BigDecimal taxaDeEntrega = restauranteService.calcularTaxaDeEntrega(id, cep);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(taxaDeEntrega);
    }
    
}
