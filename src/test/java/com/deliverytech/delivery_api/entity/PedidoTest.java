package com.deliverytech.delivery_api.entity;

import com.deliverytech.delivery_api.enums.StatusPedido;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.*;

class PedidoTest {

    private Restaurante criarRestauranteAtivo() {
        return new Restaurante("Sabor", "Comida", 
                new Endereco("Rua A", "1", "Centro", "SP", "SP", "12345-000"), 
                "119", new BigDecimal("10"),"08:00-22:00");
    }

    private Cliente criarClienteAtivo() {
        List<Endereco> enderecos = new ArrayList<Endereco>();
        enderecos.add(new Endereco("Rua A", "78", "Perinatal", "Desconhecida", "PA","2459230"));
        enderecos.add(new Endereco("Rua B", "15", "Outro", "Desconhecida", "PA","2459130"));

        Cliente cliente = new Cliente("Carlos", "carlos@email.com", "1199999999", enderecos);
       
        return cliente;
    }

    private ItemPedido criarItem() {
        Produto produto = new Produto("Pizza", "Mussarela", new BigDecimal("40.00"), "Pizza", true, criarRestauranteAtivo());
        return new ItemPedido(2, produto.getPreco(), produto);
    }

    @Test
    void deveCriarPedidoValido() {
        Pedido pedido = new Pedido(criarClienteAtivo(), criarRestauranteAtivo(), List.of(criarItem()), 
                StatusPedido.PENDENTE, "Sem cebola", new BigDecimal("5.00"), 
                new Endereco("Rua X", "12", "Centro", "SP", "SP", "11111-111"));

        assertThat(pedido.getValorTotal()).isGreaterThan(BigDecimal.ZERO);
        assertThat(pedido.getStatus()).isEqualTo(StatusPedido.PENDENTE);
    }

    @Test
    void deveLancarErroQuandoClienteInativo() {
        List<Endereco> enderecos = new ArrayList<Endereco>();
        enderecos.add(new Endereco("Rua A", "78", "Perinatal", "Desconhecida", "PA","2459230"));
        enderecos.add(new Endereco("Rua B", "15", "Outro", "Desconhecida", "PA","2459130"));

        Cliente cliente = new Cliente("Carlos", "carlos@email.com", "1199999999", enderecos);
        cliente.inativar();

        assertThatThrownBy(() -> new Pedido(cliente, criarRestauranteAtivo(), List.of(criarItem()), 
                StatusPedido.PENDENTE, null, new BigDecimal("5.0"), 
                new Endereco("Rua", "1", "Bairro", "Cidade", "ST", "00000-000")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Cliente inválido ou inativo.");
    }

    @Test
    void deveLancarErroQuandoRestauranteInativo() {
        Restaurante restaurante = criarRestauranteAtivo();
        restaurante.setAtivo(false);

        assertThatThrownBy(() -> new Pedido(criarClienteAtivo(), restaurante, List.of(criarItem()), 
                StatusPedido.PENDENTE, null, new BigDecimal("5.0"), 
                new Endereco("Rua", "1", "Bairro", "Cidade", "ST", "00000-000")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Restaurante inválido ou inativo.");
    }

    @Test
    void deveCalcularValorTotalCorretamente() {
        Pedido pedido = new Pedido(criarClienteAtivo(), criarRestauranteAtivo(), List.of(criarItem()), 
                StatusPedido.PENDENTE, null, new BigDecimal("5.00"), 
                new Endereco("Rua", "1", "Bairro", "Cidade", "ST", "00000-000"));

        assertThat(pedido.getValorTotal()).isEqualTo(new BigDecimal("85.00"));
    }

    @Test
    void devePermitirTransicoesDeStatusValidas() {
        Pedido pedido = new Pedido(criarClienteAtivo(), criarRestauranteAtivo(), List.of(criarItem()),
                StatusPedido.PENDENTE, null, new BigDecimal("5.0"),
                new Endereco("Rua", "1", "Bairro", "Cidade", "ST", "00000-000"));

        pedido.setStatus(StatusPedido.CONFIRMADO, null);
        assertThat(pedido.getStatus()).isEqualTo(StatusPedido.CONFIRMADO);
    }
}
