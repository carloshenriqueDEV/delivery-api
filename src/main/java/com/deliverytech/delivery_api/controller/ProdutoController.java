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

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/produtos")
@CrossOrigin("*")
public class ProdutoController {
    @Autowired
    private ProdutoService produtoService;

    @PostMapping()
    public ResponseEntity<ProdutoDTO> cadastrar(@Valid @RequestBody ProdutoDTO produtoDTO){
        ProdutoDTO produto = produtoService.cadastrar(produtoDTO, produtoDTO.restaurante().id());
        return ResponseEntity.status(HttpStatus.CREATED).body(produto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoDTO> buscarPorId(@PathVariable Long id) {
        ProdutoDTO produto = produtoService.buscarPorId(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(produto);
    }

    @GetMapping("/{restauranteId}/produtos")
    public ResponseEntity<List<ProdutoDTO>> produtosPorRestaurante(@PathVariable long restauranteId) {
        List<ProdutoDTO> produtos = produtoService.buscarProdutosPorRestaurante(restauranteId);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(produtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoDTO> atualizarProduto(@Valid @RequestBody ProdutoDTO produtoDTO){
        ProdutoDTO produto = produtoService.atualizar(produtoDTO);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(produto);
    }
    
    @PatchMapping("/{id}/disponibilidade/{value}")
    public ResponseEntity<ProdutoDTO> alterarDisponibilidade(@PathVariable Long id, @PathVariable boolean value){
        ProdutoDTO produto = produtoService.alterarDisponibilidade(id, value);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(produto);
    }
    
}
