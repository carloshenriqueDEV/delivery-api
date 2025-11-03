package com.deliverytech.delivery_api.unit.service;

import com.deliverytech.delivery_api.entity.Endereco;
import com.deliverytech.delivery_api.entity.Restaurante;
import com.deliverytech.delivery_api.exception.ConflictException;
import com.deliverytech.delivery_api.exception.EntityNotFoundException;
import com.deliverytech.delivery_api.external.DistanceApiClient;
import com.deliverytech.delivery_api.repository.RestauranteRepository;
import com.deliverytech.delivery_api.service.RestauranteService;
import com.deliverytech.delivery_api.service.dtos.EnderecoDTO;
import com.deliverytech.delivery_api.service.dtos.ProdutoDTO;
import com.deliverytech.delivery_api.service.dtos.RestauranteDTO;
import com.deliverytech.delivery_api.service.dtos.RestauranteResponseDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

//Todo: análisar e corrigir os testes comentados
class RestauranteServiceTest {

    @Mock
    private RestauranteRepository restauranteRepository;

    @Mock
    private DistanceApiClient distanceApiClient;

    @InjectMocks
    private RestauranteService restauranteService;

    private Restaurante restaurante;
    private Endereco endereco;
    private EnderecoDTO enderecoDTO;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        endereco = new Endereco("Rua A", "123", "Centro", "São Paulo", "SP", "01000-000");
        enderecoDTO = new EnderecoDTO(1L,"Rua A", "123", "Centro", "São Paulo", "SP", "01000-000");
        restaurante = new Restaurante(
                "Restaurante Teste",
                "Brasileira",
                endereco,
                "11999999999",
                BigDecimal.valueOf(10.0),
                "08:00-22:00"
        );
        restaurante.setId(1L);
       
    }

    @Test
    @DisplayName("Deve cadastrar restaurante com sucesso")
    void deveCadastrarRestaurante() {
        RestauranteDTO dto = new RestauranteDTO(
                1L,
                "Novo Restaurante",
                "Japonesa",
                enderecoDTO,
                "11988887777",
                BigDecimal.valueOf(5.0),
                true,
                Float.valueOf("7.75"),
                new ArrayList<ProdutoDTO>(),
                "08:00-22:00"
                
        );

        when(restauranteRepository.findByNome(dto.nome())).thenReturn(Optional.empty());
        when(restauranteRepository.save(any(Restaurante.class))).thenReturn(restaurante);

        RestauranteResponseDTO response = restauranteService.cadastrar(dto);

        assertNotNull(response);
        assertEquals(dto.nome(), response.nome());
        verify(restauranteRepository, times(1)).save(any(Restaurante.class));
    }

    // @Test
    // @DisplayName("Deve lançar exceção ao tentar cadastrar restaurante com nome duplicado")
    // void deveLancarExcecaoQuandoNomeDuplicadoAoCadastrar() {
    //     RestauranteDTO dto = new RestauranteDTO(
    //             1L,
    //             "Restaurante Teste",
    //             "Brasileira",
    //             enderecoDTO,
    //             "11999999999",
    //             BigDecimal.valueOf(10.0), 
    //             true,
    //             Float.valueOf("7.75"),
    //             new ArrayList<ProdutoDTO>(),                               
    //             "08:00-22:00"
    //     );

    //     when(restauranteRepository.findByNome(dto.nome())).thenReturn(Optional.of(restaurante));

    //     assertThrows(ConflictException.class, () -> restauranteService.cadastrar(dto));
    // }

    @Test
    @DisplayName("Deve buscar restaurante por ID com sucesso")
    void deveBuscarRestaurantePorId() {
        when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));

        RestauranteResponseDTO response = restauranteService.buscarPorId(1L);

        assertNotNull(response);
        assertEquals(restaurante.getNome(), response.nome());
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar restaurante inexistente por ID")
    void deveLancarExcecaoAoBuscarPorIdInexistente() {
        when(restauranteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> restauranteService.buscarPorId(99L));
    }

    // @Test
    // @DisplayName("Deve listar restaurantes disponíveis")
    // void deveListarRestaurantesDisponiveis() {
    //     when(restauranteRepository.findByAtivoTrue()).thenReturn(List.of(restaurante));

    //     List<RestauranteResponseDTO> lista = restauranteService.listar(null, true);

    //     assertEquals(1, lista.size());
    //     verify(restauranteRepository).findByAtivoTrue();
    // }

    @Test
    @DisplayName("Deve buscar restaurantes por categoria")
    void deveBuscarPorCategoria() {
        when(restauranteRepository.findByCategoriaAndAtivoTrue("Brasileira"))
                .thenReturn(Optional.of(List.of(restaurante)));

        List<RestauranteResponseDTO> lista = restauranteService.buscarPorCategoria("Brasileira");

        assertEquals(1, lista.size());
        assertEquals("Restaurante Teste", lista.get(0).nome());
    }

    // @Test
    // @DisplayName("Deve atualizar restaurante com sucesso")
    // void deveAtualizarRestaurante() {
    //     RestauranteDTO dtoAtualizado = new RestauranteDTO(
    //             1L,
    //             "Restaurante Atualizado",
    //             "Italiana",
    //             enderecoDTO,
    //             "11888888888",
    //             BigDecimal.valueOf(15.0),
    //             true,
    //             Float.valueOf("7.75"),
    //             new ArrayList<ProdutoDTO>(),                               
    //             "08:00-22:00"
    //     );

    //     when(restauranteRepository.findById(dtoAtualizado.id())).thenReturn(Optional.of(restaurante));
    //     when(restauranteRepository.findByNome(dtoAtualizado.nome())).thenReturn(Optional.empty());
    //     when(restauranteRepository.save(any(Restaurante.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
    //     RestauranteResponseDTO response = restauranteService.atualizar(1L,dtoAtualizado);

    //     assertNotNull(response);
    //     assertEquals(dtoAtualizado.nome(), response.nome());
    //     verify(restauranteRepository, times(1)).save(restaurante);
    // }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar para nome duplicado")
    void deveLancarExcecaoAoAtualizarComNomeDuplicado() {
        RestauranteDTO dto = new RestauranteDTO(
                1L,
                "Restaurante Duplicado",
                "Italiana",
                enderecoDTO,
                "11888888888",
                BigDecimal.valueOf(15.0),
                true,
                Float.valueOf("7.75"),
                new ArrayList<ProdutoDTO>(),
                "08:00-22:00"
        );

        when(restauranteRepository.findById(dto.id())).thenReturn(Optional.of(restaurante));
        when(restauranteRepository.findByNome(dto.nome())).thenReturn(Optional.of(new Restaurante()));

        assertThrows(EntityNotFoundException.class, () -> restauranteService.atualizar(1L,dto));
    }

    @Test
    @DisplayName("Deve inativar restaurante com sucesso")
    void deveInativarRestaurante() {
        when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));

        restauranteService.ativarDesativar(1L, false);

        assertFalse(restaurante.isAtivo());
        verify(restauranteRepository).save(restaurante);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar inativar restaurante inexistente")
    void deveLancarExcecaoAoInativarRestauranteInexistente() {
        when(restauranteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> restauranteService.ativarDesativar(99L, false));
    }

    @Test
    @DisplayName("Deve buscar restaurante com produtos disponíveis")
    void deveBuscarRestauranteComProdutosDisponiveis() {
        when(restauranteRepository.findRestauranteComProdutosDisponiveis(1L))
                .thenReturn(Optional.of(restaurante));

        RestauranteResponseDTO response = restauranteService.buscarProdutos(1L);

        assertNotNull(response);
        assertEquals(restaurante.getNome(), response.nome());
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar restaurante com produtos inexistente")
    void deveLancarExcecaoAoBuscarRestauranteComProdutosInexistente() {
        when(restauranteRepository.findRestauranteComProdutosDisponiveis(1L))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> restauranteService.buscarProdutos(1L));
    }

    @Test
    @DisplayName("Deve calcular taxa de entrega corretamente")
    void deveCalcularTaxaDeEntrega() {
        when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));
        when(distanceApiClient.calcularDistanciaKm("01000-000", "02000-000"))
                .thenReturn(BigDecimal.valueOf(5.0));

        BigDecimal taxa = restauranteService.calcularTaxaDeEntrega(1L, "02000-000");

        assertNotNull(taxa);
        verify(distanceApiClient).calcularDistanciaKm("01000-000", "02000-000");
    }

    @Test
    @DisplayName("Deve lançar exceção ao calcular taxa para restaurante inexistente")
    void deveLancarExcecaoAoCalcularTaxaParaRestauranteInexistente() {
        when(restauranteRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> restauranteService.calcularTaxaDeEntrega(1L, "02000-000"));
    }
}
