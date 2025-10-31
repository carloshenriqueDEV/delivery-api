package com.deliverytech.delivery_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivery_api.service.ProdutoService;
import com.deliverytech.delivery_api.service.dtos.ProdutoDTO;
import com.deliverytech.delivery_api.service.dtos.ProdutoResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

//Todo: incluir paginação nos endpoints que trazem uma grande massa de dados
//Todo: incluir api response wrapper para todos os endpoint
//Todo: remover endpoints redundantes
//Todo: incluir versionamento de api
@RestController
@RequestMapping("/api/produtos")
@CrossOrigin("*")
public class ProdutoController {
    @Autowired
    private ProdutoService produtoService;

    @PostMapping()
    @Operation(summary = "Cadastrar produto", 
               description = "Cria um novo produto no sistema") 
    @ApiResponses({ 
        @ApiResponse(responseCode = "201", description = "Produto criado com sucesso"), 
        @ApiResponse(responseCode = "400", description = "Dados inválidos"), 
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado") 
    })
    @PreAuthorize("hasRole('RESTAURANTE') or hasRole('ADMIN')")
    public ResponseEntity<ProdutoResponseDTO> cadastrar(
        @io.swagger.v3.oas.annotations.parameters.RequestBody( 
                description = "Dados do produto a ser criado" 
            ) 
        @Valid @RequestBody ProdutoDTO produtoDTO){
        ProdutoResponseDTO produto = produtoService.cadastrar(produtoDTO, produtoDTO.restaurante().id());
        return ResponseEntity.status(HttpStatus.CREATED).body(produto);
    }

    @GetMapping("/{id}")
      @Operation(summary = "Buscar produto por ID", 
               description = "Recupera um produto específico pelo ID") 
    @ApiResponses({ 
        @ApiResponse(responseCode = "200", description = "Produto encontrado"), 
        @ApiResponse(responseCode = "404", description = "Produto não encontrado") 
    })
    public ResponseEntity<ProdutoResponseDTO> buscarPorId(@Parameter(description = "ID do produto")  @PathVariable Long id) {
        ProdutoResponseDTO produto = produtoService.buscarPorId(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(produto);
    }

    @GetMapping("restaurante/{restauranteId}")
    public ResponseEntity<List<ProdutoResponseDTO>> produtosPorRestaurante(@PathVariable long restauranteId) {
        List<ProdutoResponseDTO> produtos = produtoService.buscarProdutosPorRestaurante(restauranteId);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(produtos);
    }

    @PutMapping("/{id}")
      @Operation(summary = "Atualizar produto", 
               description = "Atualiza os dados de um produto existente") 
    @ApiResponses({ 
        @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso"), 
        @ApiResponse(responseCode = "404", description = "Produto não encontrado"), 
        @ApiResponse(responseCode = "400", description = "Dados inválidos") 
    }) 
     @PreAuthorize("hasRole('ADMIN') or @produtoService.isOwner(#id)")
    public ResponseEntity<ProdutoResponseDTO> atualizarProduto(@Parameter(description = "ID do produto") @PathVariable Long id, 
    @io.swagger.v3.oas.annotations.parameters.RequestBody( 
                description = "Dados do produto a ser atualizado" 
            )
    @Valid @RequestBody ProdutoDTO produtoDTO){
        ProdutoResponseDTO produto = produtoService.atualizar(id, produtoDTO);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(produto);
    }
    
    @PatchMapping("/{id}/disponibilidade/{value}")
     @Operation(summary = "Alterar disponibilidade", 
               description = "Alterna a disponibilidade do produto") 
    @ApiResponses({ 
        @ApiResponse(responseCode = "200", description = "Disponibilidade alterada com sucesso"), 
        @ApiResponse(responseCode = "404", description = "Produto não encontrado") 
    }) 
    public ResponseEntity<ProdutoResponseDTO> alterarDisponibilidade(@PathVariable Long id, @PathVariable boolean value){
        ProdutoResponseDTO produto = produtoService.alterarDisponibilidade(id, value);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(produto);
    }

      @GetMapping("/categoria/{categoria}") 
      @Operation(summary = "Buscar por categoria", 
              description = "Lista produtos de uma categoria específica") 
      @ApiResponses({ 
          @ApiResponse(responseCode = "200", description = "Produtos encontrados") 
      }) 
      public ResponseEntity<List<ProdutoResponseDTO>> buscarPorCategoria( 
        @Parameter(description = "Categoria do produto") 
        @PathVariable String categoria) { 
        
                List<ProdutoResponseDTO> produtos = 
                produtoService.buscarPorCategoria(categoria); 
        
                return ResponseEntity.ok(produtos); 
        } 

        @GetMapping("/buscar") 
        @Operation(summary = "Buscar por nome", description = "Busca produtos pelo nome") 
        @ApiResponses({ 
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso") 
        }) 
        public ResponseEntity<List<ProdutoResponseDTO>>  buscarPorNome( @Parameter(description = "Nome do produto") 
                    @RequestParam String nome) { 
        
                List<ProdutoResponseDTO> produtos = produtoService.buscarPorNome(nome); 
            
        return ResponseEntity.ok(produtos); 
        } 

}
