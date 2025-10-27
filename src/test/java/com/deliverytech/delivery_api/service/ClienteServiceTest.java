package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.entity.Cliente;
import com.deliverytech.delivery_api.entity.Endereco;
import com.deliverytech.delivery_api.repository.ClienteRepository;
import com.deliverytech.delivery_api.service.dtos.ClienteDTO;
import com.deliverytech.delivery_api.service.dtos.ClienteResponseDTO;
import com.deliverytech.delivery_api.service.dtos.EnderecoDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente cliente;
    private ClienteDTO clienteDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Endereco endereco = new Endereco("Rua A", "123", "Centro", "Cidade", "SP", "12345-678");
        EnderecoDTO enderecoDto = new EnderecoDTO(Long.valueOf(1),"Rua A", "123", "Centro", "Cidade", "SP", "12345-678");
        List<Endereco> enderecos = List.of(endereco);
        List<EnderecoDTO> enderecoDTOs = List.of(enderecoDto);

        cliente = new Cliente("Carlos", "carlos@email.com", "11999999999", enderecos);
        cliente.setId(1L);

        clienteDTO = new ClienteDTO(1L, "Carlos", "carlos@email.com", "11999999999", true,enderecoDTOs);
    }

    // --- CADASTRAR ---
    @Test
    @DisplayName("Deve cadastrar um cliente com sucesso")
    void deveCadastrarUmClienteComSucesso() {
        when(clienteRepository.existsByEmail(clienteDTO.email())).thenReturn(false);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        ClienteResponseDTO response = clienteService.cadastrar(clienteDTO);

        assertNotNull(response);
        assertEquals(cliente.getNome(), response.nome());
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar cadastrar cliente com e-mail já existente")
    void deveLancarExcecaoAoCadastrarComEmailExistente() {
        when(clienteRepository.existsByEmail(clienteDTO.email())).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> clienteService.cadastrar(clienteDTO));

        assertEquals("Email já cadastrado", ex.getMessage());
        verify(clienteRepository, never()).save(any());
    }

    // --- BUSCAR POR ID ---
    @Test
    @DisplayName("Deve buscar cliente por ID com sucesso")
    void deveBuscarClientePorIdComSucesso() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        Optional<ClienteResponseDTO> result = clienteService.buscarPorId(1L);

        assertTrue(result.isPresent());
        assertEquals(cliente.getNome(), result.get().nome());
        verify(clienteRepository).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar cliente inexistente por ID")
    void deveLancarExcecaoAoBuscarClienteInexistentePorId() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> clienteService.buscarPorId(1L));

        assertEquals("Cliente não encontrado: 1", ex.getMessage());
    }

    // --- BUSCAR POR EMAIL ---
    @Test
    @DisplayName("Deve buscar cliente por e-mail com sucesso")
    void deveBuscarClientePorEmailComSucesso() {
        when(clienteRepository.findByEmail(cliente.getEmail())).thenReturn(Optional.of(cliente));

        Optional<ClienteResponseDTO> result = clienteService.buscarPorEmail(cliente.getEmail());

        assertTrue(result.isPresent());
        assertEquals(cliente.getEmail(), result.get().email());
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar cliente inexistente por e-mail")
    void deveLancarExcecaoAoBuscarClienteInexistentePorEmail() {
        when(clienteRepository.findByEmail(cliente.getEmail())).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> clienteService.buscarPorEmail(cliente.getEmail()));

        assertEquals("Cliente não encontrado.", ex.getMessage());
    }

    // --- LISTAR ATIVOS ---
    @Test
    @DisplayName("Deve listar clientes ativos")
    void deveListarClientesAtivos() {
        when(clienteRepository.findByAtivoTrue()).thenReturn(List.of(cliente));

        List<ClienteResponseDTO> result = clienteService.listarAtivos();

        assertEquals(1, result.size());
        assertEquals(cliente.getNome(), result.get(0).nome());
        verify(clienteRepository).findByAtivoTrue();
    }

    // --- ATUALIZAR ---
    @Test
    @DisplayName("Deve atualizar cliente com sucesso")
    void deveAtualizarClienteComSucesso() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.existsByEmail(clienteDTO.email())).thenReturn(false);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        ClienteResponseDTO response = clienteService.atualizar(1L, clienteDTO);

        assertNotNull(response);
        assertEquals(clienteDTO.nome(), response.nome());
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar cliente com e-mail já cadastrado por outro cliente")
    void deveLancarExcecaoAoAtualizarComEmailDuplicado() {
        ClienteDTO clienteAtualizado = new ClienteDTO(1L, "Carlos", "novo@email.com", "11999999999", true, clienteDTO.enderecos());

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.existsByEmail(clienteAtualizado.email())).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> clienteService.atualizar(1L, clienteAtualizado));

        assertTrue(ex.getMessage().contains("Email já cadastrado"));
    }

    // --- ATIVAR / DESATIVAR ---
    @Test
    @DisplayName("Deve inativar cliente ativo")
    void deveInativarClienteAtivo() {
        cliente.setAtivo(true);
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        clienteService.ativarDesativar(1L, false);

        assertFalse(cliente.isAtivo());
        verify(clienteRepository).save(cliente);
    }

    @Test
    @DisplayName("Deve ativar cliente inativo")
    void deveAtivarClienteInativo() {
        cliente.setAtivo(false);
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        clienteService.ativarDesativar(1L, true);

        assertTrue(cliente.isAtivo());
        verify(clienteRepository).save(cliente);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar ativar/desativar cliente inexistente")
    void deveLancarExcecaoAoAtivarDesativarClienteInexistente() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> clienteService.ativarDesativar(1L, false));

        assertEquals("Cliente não encontrado: 1", ex.getMessage());
    }
}
