package com.deliverytech.delivery_api.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivery_api.service.dtos.RestauranteDTO;
import com.deliverytech.delivery_api.service.dtos.RestauranteResponseDTO;
import com.deliverytech.delivery_api.service.interfaces.RestauranteServiceInterface;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content; 
import io.swagger.v3.oas.annotations.media.Schema; 
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PutMapping;




//Todo: incluir paginação nos endpoints que trazem uma grande massa de dados
//Todo: incluir api response wrapper para todos os endpoint
//Todo: Excluir endpoint's redundantes
//Todo: incluir versionamento de api
//Todo: Unificar os endpoints de busca em um único endpoint com múltiplos filtros

@RestController
@RequestMapping("/api/restaurantes")
@Tag(name = "Restaurantes", description = "Operações relacionadas ao gerenciamento de restaurantes") 
@CrossOrigin("*")
@Tag(name = "Restaurantes", description="API de Gereciamento de Restaurantes")
public class RestauranteController {
    private final RestauranteServiceInterface  restauranteService;

    public RestauranteController(RestauranteServiceInterface restauranteServiceInterface){
        this.restauranteService = restauranteServiceInterface;
    }

    @PostMapping()
    @Operation(summary = "Cadastrar restaurante", description = "Cria um novo restaurante no sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Restaurante criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "500", description = "Erro interno"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"), 
        @ApiResponse(responseCode = "403", description = "Acesso negado") 
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RestauranteResponseDTO> cadastrar(
        @RequestBody 
        @Valid 
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Dados do restaurante a ser criado"
        )
    RestauranteDTO restauranteDTO) {
       
       RestauranteResponseDTO restaurante = restauranteService.cadastrar(restauranteDTO);

       return ResponseEntity.status(HttpStatus.CREATED).body(restaurante);
    }
    
    @GetMapping("/{id}")
    @Operation(
        summary = "Buscar restaurante por ID", 
        description = "Recupera um restaurante específico pelo ID",
        tags = {"Restaurantes"} 
    ) 
    @ApiResponses({ 
        @ApiResponse(responseCode = "200", description = "Restaurante encontrado"), 
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado") 
    })

    public ResponseEntity<RestauranteResponseDTO> buscarPorId(
       @Parameter(description = "ID do restaurante")  
       @PathVariable Long id) {
       RestauranteResponseDTO restaurante = restauranteService.buscarPorId(id);
       return ResponseEntity.status(HttpStatus.CREATED).body(restaurante);
    }
    
    @GetMapping("/{id}/produtos")
    public ResponseEntity<RestauranteResponseDTO> buscarProdutos(@PathVariable Long id) {
        RestauranteResponseDTO restauranteDTO = restauranteService.buscarProdutos(id);

        return ResponseEntity.status(HttpStatus.OK).body(restauranteDTO);
    }

    @GetMapping()
    @Operation(
        summary = "Listar restaurantes", 
        description = "Lista restaurantes com filtros opcionais e paginação",
        tags = {"Restaurantes"}
        ) 
    @ApiResponses({ 
        @ApiResponse(responseCode = "200", description = "Lista recuperada com sucesso",
         content = @Content( 
                mediaType = "applica on/json", 
                schema = @Schema(implementation = RestauranteResponseDTO.class) 
            )
        ) 
    })
    public ResponseEntity<List<RestauranteResponseDTO>> buscarRestaurantes(
        @Parameter(description = "Categoria do restaurante") 
        @RequestParam(required = false) String categoria, 
        @Parameter(description = "Status a vo do restaurante") 
        @RequestParam(required = false) Boolean ativo 
            )  {
        List<RestauranteResponseDTO> restauranteDTOs = restauranteService.listar(categoria, ativo);
        return ResponseEntity.status(HttpStatus.OK).body(restauranteDTOs);
    }
    
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<RestauranteResponseDTO>> buscarPorCategoria(@PathVariable String categoria) {
        List<RestauranteResponseDTO> restauranteDTOs = restauranteService.buscarPorCategoria(categoria);
        return ResponseEntity.status(HttpStatus.OK).body(restauranteDTOs);
    }
    
    @PutMapping("")
    @Operation(summary = "Atualizar restaurante", 
        description = "Atualiza os dados de um restaurante existente"
    ) 
    @ApiResponses({ 
        @ApiResponse(responseCode = "200", description = "Restaurante atualizado com sucesso"), 
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado"), 
        @ApiResponse(responseCode = "400", description = "Dados inválidos") 
    })
    @PreAuthorize("hasRole('ADMIN') or (hasRole('RESTAURANTE') and @restauranteService.isOwner(#id))") 
    public ResponseEntity<RestauranteResponseDTO> atualizarRestaurante(
        @Parameter(description = "ID do restaurante") @PathVariable Long id, 
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Dados do restaurante a ser criado"
        )
        @RequestBody RestauranteDTO restauranteDTO) {
        RestauranteResponseDTO restaurante = restauranteService.atualizar(id, restauranteDTO);
        
        return ResponseEntity.status(HttpStatus.OK).body(restaurante);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Ativar/Desativar restaurante", 
        description = "Alternar o status ativo/inativo do restaurante."
    ) 
    @ApiResponses({ 
        @ApiResponse(responseCode = "200", description = "Restaurante atualizado com sucesso"), 
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado"), 
    })
     @PreAuthorize("hasRole('ADMIN')") 
    public ResponseEntity<RestauranteResponseDTO> ativarOuInativarRestaurante(@PathVariable Long id, @RequestBody Boolean ativo){
        RestauranteResponseDTO restaurante = restauranteService.ativarDesativar(id, ativo);

        return ResponseEntity.status(HttpStatus.OK).body(restaurante);
    }
    
     
    @GetMapping("/{id}/taxa-entrega/{cep}")
       @Operation(summary = "Calcular taxa de entrega", 
               description = "Calcula a taxa de entrega para um CEP específico") 
    @ApiResponses({ 
        @ApiResponse(responseCode = "200", description = "Taxa calculada com sucesso"), 
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado") 
    })
    @PreAuthorize("hasRole('ADMIN') or (hasRole('RESTAURANTE') and @restauranteService.isOwner(#id))") 
    public ResponseEntity<BigDecimal> calcularTaxaEntrega(@PathVariable Long id, @PathVariable String cep) {
        BigDecimal taxaDeEntrega = restauranteService.calcularTaxaDeEntrega(id, cep);
        return ResponseEntity.status(HttpStatus.OK).body(taxaDeEntrega);
    }

    @GetMapping("/proximos/{cep}") 
    @Operation(summary = "Restaurantes próximos", 
               description = "Lista restaurantes próximos a um CEP") 
    @ApiResponses({ 
        @ApiResponse(responseCode = "200", description = "Restaurantes próximos encontrados"), 
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado") 
    }) 
    public ResponseEntity<List<RestauranteResponseDTO>> buscarProximos( 
            @Parameter(description = "CEP de referência") 
            @PathVariable String cep, 
            @Parameter(description = "Raio em km") 
            @RequestParam(defaultValue = "10") Integer raio) { 
 
        List<RestauranteResponseDTO> restaurantes = 
            restauranteService.buscarRestaurantesProximos(cep, raio); 
 
        return ResponseEntity.status(HttpStatus.OK).body(restaurantes); 
    }
    
}
