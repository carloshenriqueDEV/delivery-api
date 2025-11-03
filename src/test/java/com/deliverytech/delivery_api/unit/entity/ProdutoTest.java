package com.deliverytech.delivery_api.unit.entity;

import org.junit.jupiter.api.Test;

import com.deliverytech.delivery_api.entity.Endereco;
import com.deliverytech.delivery_api.entity.Produto;
import com.deliverytech.delivery_api.entity.Restaurante;

import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.*;

class ProdutoTest {

    @Test
    void deveCriarProdutoValido() {
        Restaurante restaurante = new Restaurante("Restaurante X", "Lanches", 
                new Endereco("Rua A", "1", "Bairro", "SP", "SP", "12345-000"),
                "11999999999", new BigDecimal("5.00"), "08:00-22:00");

        Produto produto = new Produto("X-Burger", "Hambúrguer artesanal", new BigDecimal("25.00"), 
                "Lanche", true, restaurante);

        assertThat(produto.getNome()).isEqualTo("X-Burger");
        assertThat(produto.getPreco()).isEqualTo(new BigDecimal("25.00"));
    }

    @Test
    void deveLancarExcecaoQuandoNomeForNulo() {
        assertThatThrownBy(() -> new Produto(null, "desc", new BigDecimal("10.0"), "cat", true, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Nome é obrigatório");
    }

    @Test
    void deveLancarExcecaoQuandoNomeMenorQue2Caracteres() {
        assertThatThrownBy(() -> new Produto("A", "desc", new BigDecimal("10.0"), "cat", true, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Nome deve ter pelo menos 2 caracteres");
    }

    @Test
    void deveLancarExcecaoQuandoPrecoForZeroOuNegativo() {
        assertThatThrownBy(() -> new Produto("Lanche", "desc", new BigDecimal("0"), "cat", true, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Preço deve ser maior que zero");
    }
}
