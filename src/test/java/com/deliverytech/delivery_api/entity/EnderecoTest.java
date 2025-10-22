package com.deliverytech.delivery_api.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class EnderecoTest {

    @Test
    @DisplayName("Deve criar um endereço válido")
    void deveCriarEnderecoValido() {
        Endereco endereco = new Endereco("Rua A", "123", "Centro", "SP", "SP", "12345-000");
        assertThat(endereco.getLogradouro()).isEqualTo("Rua A");
        assertThat(endereco.getCep()).isEqualTo("12345-000");
    }

    @Test
    @DisplayName("Deve lanção exceção quando logradouro for nulo")
    void deveLancarExcecaoQuandoLogradouroForNulo() {
        assertThatThrownBy(() -> new Endereco(null, "10", "Centro", "SP", "SP", "12345-000"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Logradouro é obrigatório.");
    }

    @Test
    @DisplayName("Deve lanção exceção quando logradouro for vazio")
    void deveLancarExcecaoQuandoCepForNuloOuVazio() {
        assertThatThrownBy(() -> new Endereco("Rua A", "10", "Centro", "SP", "SP", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("CEP é obrigatório.");
    }

    @Test
    @DisplayName("Deve lanção exceção quando número for nulo")
    void deveLancarExcecaoQuandoNumeroForNulo() {
        assertThatThrownBy(() -> new Endereco("Rua A", null, "Centro", "SP", "SP", "12345-000"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Número é obrigatório.");
    }

    @Test
    @DisplayName("Deve lançar exceção quando número for vazio")
    void deveLancarExcecaoQuandoNumeroForVazio() {
        assertThatThrownBy(() -> new Endereco("Rua A", " ", "Centro", "SP", "SP", "12345-000"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Número é obrigatório.");
    }

    @Test
    @DisplayName("Deve lançar execeção quando cep for nulo")
    void deveLancarExcecaoQuandoCepNulo(){
        assertThatThrownBy(() -> new Endereco("Rua A", "10", "Centro", "SP", "SP", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("CEP é obrigatório.");
    }

     @Test
    @DisplayName("Deve lançar execeção quando cep for vazio")
    void deveLancarExcecaoQuandoCepVazio(){
        assertThatThrownBy(() -> new Endereco("Rua A", "10", "Centro", "SP", "SP", ""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("CEP é obrigatório.");
    }
}
