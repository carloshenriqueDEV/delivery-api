package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.entity.Produto;
import com.deliverytech.delivery_api.entity.Restaurante;
import com.deliverytech.delivery_api.repository.ProdutoRepository;
import com.deliverytech.delivery_api.repository.RestauranteRepository;
import com.deliverytech.delivery_api.service.dtos.ProdutoDTO;
import com.deliverytech.delivery_api.service.dtos.ProdutoResponseDTO;
import com.deliverytech.delivery_api.service.dtos.RestauranteDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private RestauranteRepository restauranteRepository;

    @Mock
    private RestauranteService restauranteService;

    @InjectMocks
    private ProdutoService produtoService;

    private Produto produto;
    private Restaurante restaurante;
    private ProdutoDTO produtoDTO;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setNome("Restaurante Teste");

        produto = new Produto("Produto Teste", "Descrição", BigDecimal.valueOf(10.0), "Categoria", true, restaurante);
        produto.setId(1L);

        RestauranteDTO restauranteDTO = new RestauranteDTO(1L, "Restaurante Teste", "", null, null, null, false, null, null,                
                "08:00-22:00");

        produtoDTO = new ProdutoDTO(
                produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getPreco(),
                produto.getCategoria(),
                produto.getDisponivel(),
                restauranteDTO
        );
    }

    @Test
    @DisplayName("Deve cadastrar produto com sucesso")
    void deveCadastrarProdutoComSucesso() {
        when(restauranteRepository.findById(restaurante.getId())).thenReturn(Optional.of(restaurante));
        when(produtoRepository.save(any(Produto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProdutoResponseDTO response = produtoService.cadastrar(produtoDTO, restaurante.getId());

        assertNotNull(response);
        assertEquals(produto.getNome(), response.nome());
        assertEquals(produto.getCategoria(), response.categoria());
        verify(produtoRepository, times(1)).save(any(Produto.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando restaurante não existe")
    void deveLancarExcecaoQuandoRestauranteNaoExiste() {
        when(restauranteRepository.findById(anyLong())).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> produtoService.cadastrar(produtoDTO, 99L)
        );

        assertEquals("Restaurante não encontrado: 99", exception.getMessage());
    }

    @Test
    @DisplayName("Deve atualizar produto com sucesso")
    void deveAtualizarProduto() {
        when(produtoRepository.findById(produto.getId())).thenReturn(Optional.of(produto));
        when(produtoRepository.save(any(Produto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RestauranteDTO restauranteDTO = new RestauranteDTO(1L, "Restaurante Teste", "", null, null, null, false, null, null,                
                "08:00-22:00");

        ProdutoDTO dtoAtualizado = new ProdutoDTO(
                produto.getId(),
                "Produto Atualizado",
                "Nova Descrição",
                BigDecimal.valueOf(20.0),
                "Categoria Atualizada",
                false,
                restauranteDTO
        );

        ProdutoResponseDTO response = produtoService.atualizar(1L,dtoAtualizado);

        assertEquals("Produto Atualizado", response.nome());
        assertFalse(response.disponivel());
        verify(produtoRepository, times(1)).save(produto);
    }

    @Test
    @DisplayName("Deve alterar disponibilidade do produto")
    void deveAlterarDisponibilidade() {
        when(produtoRepository.findById(produto.getId())).thenReturn(Optional.of(produto));
        when(produtoRepository.save(any(Produto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProdutoResponseDTO response = produtoService.alterarDisponibilidade(produto.getId(), false);

        assertFalse(response.disponivel());
        verify(produtoRepository, times(1)).save(produto);
    }

    @Test
    @DisplayName("Deve buscar produtos por categoria")
    void deveBuscarPorCategoria() {
        when(produtoRepository.findByCategoriaAndDisponivelTrue("Categoria"))
                .thenReturn(List.of(produto));

        List<ProdutoResponseDTO> response = produtoService.buscarPorCategoria("Categoria");

        assertEquals(1, response.size());
        assertEquals("Produto Teste", response.get(0).nome());
    }

    @Test
    @DisplayName("Deve lançar exceção se não houver produtos na categoria")
    void deveLancarExcecaoCategoriaVazia() {
        when(produtoRepository.findByCategoriaAndDisponivelTrue("Categoria Vazia"))
                .thenReturn(List.of());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> produtoService.buscarPorCategoria("Categoria Vazia")
        );

        assertEquals("Nenhum produto encontrado na categoria: Categoria Vazia", exception.getMessage());
    }

    @Test
    @DisplayName("Deve buscar produtos por faixa de preço")
    void deveBuscarPorFaixaPreco() {
        when(produtoRepository.findByPrecoBetweenAndDisponivelTrue(BigDecimal.valueOf(5), BigDecimal.valueOf(15)))
                .thenReturn(List.of(produto));

        var response = produtoService.buscarPorFaixaPreco(BigDecimal.valueOf(5), BigDecimal.valueOf(15));

        assertEquals(1, response.size());
        assertEquals(produto.getNome(), response.get(0).nome());
    }

    @Test
    @DisplayName("Deve remover produto com sucesso")
    void deveRemoverProduto() {
        when(produtoRepository.findById(produto.getId())).thenReturn(Optional.of(produto));

        produtoService.remover(produto.getId());

        verify(produtoRepository, times(1)).delete(produto);
    }

    @Test
    @DisplayName("Deve buscar produto por id")
    void deveBuscarPorId() {
        when(produtoRepository.findById(produto.getId())).thenReturn(Optional.of(produto));

        ProdutoResponseDTO response = produtoService.buscarPorId(produto.getId());

        assertEquals(produto.getNome(), response.nome());
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar produto inexistente por id")
    void deveLancarExcecaoProdutoInexistente() {
        when(produtoRepository.findById(anyLong())).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> produtoService.buscarPorId(99L)
        );

        assertEquals("Produto não encontrado: 99", exception.getMessage());
    }
}
