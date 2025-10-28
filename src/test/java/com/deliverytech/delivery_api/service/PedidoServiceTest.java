package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.entity.*;
import com.deliverytech.delivery_api.enums.StatusPedido;
import com.deliverytech.delivery_api.repository.*;
import com.deliverytech.delivery_api.service.dtos.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
class PedidoServiceTest {

    @InjectMocks
    private PedidoService pedidoService;

    @Mock
    private PedidoRepository pedidoRepository;
    @Mock
    private ClienteRepository clienteRepository;
    @Mock
    private RestauranteRepository restauranteRepository;
    @Mock
    private ProdutoRepository produtoRepository;
    @Mock
    private RestauranteService restauranteService;

    private Cliente cliente;
    private Restaurante restaurante;
    private Produto produto;
    private Endereco endereco;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        endereco = new Endereco("Rua Teste", "123", "Centro", "São Paulo", "SP", "01000-000");

        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Carlos");
        cliente.setAtivo(true);
        cliente.setEnderecos(List.of(endereco));

        restaurante = new Restaurante();
        restaurante.setId(10L);
        restaurante.setNome("Restaurante Teste");
        restaurante.setAtivo(true);

        produto = new Produto();
        produto.setId(1L);
        produto.setNome("Hambúrguer");
        produto.setPreco(new BigDecimal("25.00"));
        produto.setDisponivel(true);
        produto.setRestaurante(restaurante);
    }

    @Test
void deveCriarPedidoComSucesso() {
    // Arrange
    ItemPedidoDTO itemDTO = new ItemPedidoDTO(
        1L,
        2,
        BigDecimal.valueOf(3.5),
        BigDecimal.valueOf(7.0),
        produto.getId(),
        ""
    );

    EnderecoDTO enderecoDTO = new EnderecoDTO(1L,
        "Rua Teste",
        "123",
        "Centro",
        "São Paulo",
        "SP",
        "01000-000"
    );

    PedidoDTO pedidoDTO = new PedidoDTO(
        1L,                      
        null,                      
        null,                       
        null,                       
        null,                       
        "Sem cebola",              
        enderecoDTO,                
        cliente.getId(),           
        restaurante.getId(),        
        List.of(itemDTO),
        "CARTAO_CREDITO"            
    );

    when(clienteRepository.findByIdAndAtivoTrue(cliente.getId()))
            .thenReturn(Optional.of(cliente));

    when(restauranteRepository.findByIdAndAtivoTrue(restaurante.getId()))
            .thenReturn(Optional.of(restaurante));

    when(produtoRepository.findAllById(List.of(produto.getId())))
            .thenReturn(List.of(produto));

    when(restauranteService.calcularTaxaDeEntrega(eq(restaurante.getId()), anyString()))
            .thenReturn(new BigDecimal("5.00"));

    when(pedidoRepository.save(any(Pedido.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

    // Act
    PedidoResponseDTO response = pedidoService.criarPedido(pedidoDTO);

    // Assert
    assertNotNull(response);
    assertEquals(StatusPedido.PENDENTE, response.status());
    assertEquals("Restaurante Teste", response.restaurante().nome());
    assertEquals(1, response.itens().size());
    assertEquals(new BigDecimal("25.00"), response.itens().get(0).precoUnitario());
    verify(pedidoRepository, times(1)).save(any(Pedido.class));
}


    @Test
void deveLancarExcecaoQuandoClienteNaoExiste() {
    // Arrange
    EnderecoDTO enderecoDTO = new EnderecoDTO(1L,
        "Rua Teste",
        "123",
        "Centro",
        "São Paulo",
        "SP",
        "01000-000"
    );

    PedidoDTO pedidoDTO = new PedidoDTO(
        1L,                    
        null,                   
        null,                   
        null,                    
        null,                  
        "Teste",                
        enderecoDTO,            
        99L,                    
        restaurante.getId(),    
        List.of(                 
            new ItemPedidoDTO(
                1L,
                1,
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(10),
                1L,
                ""
            )
        ),
        "CARTAO_CREDITO"
    );

    when(clienteRepository.findByIdAndAtivoTrue(anyLong()))
        .thenReturn(Optional.empty());

    // Act + Assert
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> pedidoService.criarPedido(pedidoDTO)
    );

    assertEquals("Cliente não encontrado: 99", exception.getMessage());
}


    @Test
    void deveLancarExcecaoQuandoProdutoInativo() {
        // deixamos o produto como indisponível
        produto.setDisponivel(false);

        // EnderecoDTO (sem id no construtor, adapte se o seu tiver)
        EnderecoDTO enderecoDTO = new EnderecoDTO(
            1L,
            "Rua Teste",
            "123",
            "Centro",
            "São Paulo",
            "SP",
            "01000-000"
        );

        // Importante: usar produto.getId() aqui para que o findAllById seja chamado com o mesmo id
        ItemPedidoDTO itemDto = new ItemPedidoDTO(
            null,                       // id do item (não usado no serviço ao criar)
            1,                          // quantidade
            BigDecimal.valueOf(10),     // precoUnitario (dto pode ter esse campo)
            BigDecimal.valueOf(10),     // subtotal (não usado para busca)
            produto.getId(),             // produtoId -> **MUST MATCH** produto.getId()
            ""
        );

        PedidoDTO pedidoDTO = new PedidoDTO(
            null,                       // id
            null,                       // numeroPedido
            null,                       // dataPedido
            null,                       // status
            null,                       // valorTotal
            "Teste",                    // observacoes
            enderecoDTO,                // enderecoDeEntrega
            cliente.getId(),            // clienteId -> garantir cliente existente
            restaurante.getId(),        // restauranteId -> garantir restaurante existente
            List.of(itemDto),            // itens -> não vazio
            "CARTAO_CREDITO"
        );

        // mocks necessários para o fluxo chegar na verificação de disponibilidade
        when(clienteRepository.findByIdAndAtivoTrue(cliente.getId()))
                .thenReturn(Optional.of(cliente));

        when(restauranteRepository.findByIdAndAtivoTrue(restaurante.getId()))
                .thenReturn(Optional.of(restaurante));

        // NOTE: aqui usamos exatamente List.of(produto.getId())
        when(produtoRepository.findAllById(List.of(produto.getId())))
                .thenReturn(List.of(produto));

        // chamar e validar que cai na exceção de produto inativo
        assertThrows(IllegalStateException.class, () -> pedidoService.criarPedido(pedidoDTO));
    }

}
