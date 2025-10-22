package com.deliverytech.delivery_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivery_api.service.ProdutoService;
import com.deliverytech.delivery_api.service.dtos.ProdutoDTO;
import com.deliverytech.delivery_api.service.dtos.ProdutoResponseDTO;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/produtos")
@CrossOrigin("*")
public class ProdutoController {
    @Autowired
    private ProdutoService produtoService;

    @PostMapping()
    public ResponseEntity<ProdutoResponseDTO> cadastrar(@Valid @RequestBody ProdutoDTO produtoDTO){
        ProdutoResponseDTO produto = produtoService.cadastrar(produtoDTO, produtoDTO.restaurante().id());
        return ResponseEntity.status(HttpStatus.CREATED).body(produto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> buscarPorId(@PathVariable Long id) {
        ProdutoResponseDTO produto = produtoService.buscarPorId(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(produto);
    }

    @GetMapping("/{restauranteId}")
    public ResponseEntity<List<ProdutoResponseDTO>> produtosPorRestaurante(@PathVariable long restauranteId) {
        List<ProdutoResponseDTO> produtos = produtoService.buscarProdutosPorRestaurante(restauranteId);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(produtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> atualizarProduto(@PathVariable Long id, @Valid @RequestBody ProdutoDTO produtoDTO){
        ProdutoResponseDTO produto = produtoService.atualizar(id, produtoDTO);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(produto);
    }
    
    @PatchMapping("/{id}/disponibilidade/{value}")
    public ResponseEntity<ProdutoResponseDTO> alterarDisponibilidade(@PathVariable Long id, @PathVariable boolean value){
        ProdutoResponseDTO produto = produtoService.alterarDisponibilidade(id, value);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(produto);
    }
    
}
