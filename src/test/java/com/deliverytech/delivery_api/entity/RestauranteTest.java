package com.deliverytech.delivery_api.entity;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.*;

class RestauranteTest {

    @Test
    void deveCriarRestauranteValido() {
        Endereco endereco = new Endereco("Rua A", "1", "Centro", "SP", "SP", "12345-000");
        Restaurante restaurante = new Restaurante("SaborCaseiro", "Comida Brasileira", endereco, "11988887777", new BigDecimal("10.00"));

        assertThat(restaurante.getNome()).isEqualTo("SaborCaseiro");
        assertThat(restaurante.isAtivo()).isTrue();
    }

    @Test
    void deveLancarExcecaoQuandoNomeForInvalido() {
        Endereco endereco = new Endereco("Rua A", "1", "Centro", "SP", "SP", "12345-000");

        assertThatThrownBy(() -> new Restaurante("A", "Comida", endereco, "119", new BigDecimal("10.0")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Nome deve ter pelo menos 2 caracteres");
    }

    @Test
    void deveLancarExcecaoQuandoTaxaEntregaForNegativa() {
        Endereco endereco = new Endereco("Rua A", "1", "Centro", "SP", "SP", "12345-000");

        assertThatThrownBy(() -> new Restaurante("Sabor", "Comida", endereco, "119", new BigDecimal("-5.0")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Taxa de entrega não pode ser negativa");
    }

    @Test
    void deveCalcularTaxaEntregaComDistancia() {
        Restaurante restaurante = new Restaurante("Sabor", "Comida", 
                new Endereco("Rua A", "1", "Centro", "SP", "SP", "12345-000"), 
                "119", new BigDecimal("5.00"));

        BigDecimal total = restaurante.calcularTaxaDeEntrega(new BigDecimal("10"), new BigDecimal("2"));
        assertThat(total).isEqualTo(new BigDecimal("25.00"));
    }
}
