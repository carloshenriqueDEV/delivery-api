package com.deliverytech.delivery_api.unit.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.deliverytech.delivery_api.entity.Cliente;
import com.deliverytech.delivery_api.entity.Endereco;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ClienteTest {

    @Test
    @DisplayName("Deve criar cliente válido quando todos os dados são corretos")
    void deveCriarClienteComDadosValidos() {
        // Arrange & Act
        List<Endereco> enderecos = new ArrayList<Endereco>();
        enderecos.add(new Endereco("Rua A", "78", "Perinatal", "Desconhecida", "PA","2459230"));
        enderecos.add(new Endereco("Rua B", "15", "Outro", "Desconhecida", "PA","2459130"));

        Cliente cliente = new Cliente("Carlos", "carlos@email.com", "1199999999", enderecos);

        // Assert
        assertThat(cliente.getNome()).isEqualTo("Carlos");
        assertThat(cliente.getEmail()).isEqualTo("carlos@email.com");
        assertThat(cliente.getTelefone()).isEqualTo("1199999999");
        assertThat(cliente.getEnderecos().getFirst().getLogradouro()).isEqualTo("Rua A");
        assertThat(cliente.isAtivo()).isTrue();
        assertThat(cliente.getDataCadastro()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    // --------------------
    // 🔹 Testes de Nome
    // --------------------

    @Test
    @DisplayName("Deve lançar exceção quando nome for nulo")
    void deveLancarExcecaoQuandoNomeForNulo() {
       List<Endereco> enderecos = new ArrayList<Endereco>();
         enderecos.add(new Endereco("Rua A", "78", "Perinatal", "Desconhecida", "PA","2459230"));
        enderecos.add(new Endereco("Rua B", "15", "Outro", "Desconhecida", "PA","2459130"));
        assertThatThrownBy(() -> new Cliente(null, "email@email.com", "123", enderecos))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Nome é obrigatório.");
    }

    @Test
    @DisplayName("Deve lançar exceção quando nome for vazio")
    void deveLancarExcecaoQuandoNomeForVazio() {List<Endereco> enderecos = new ArrayList<Endereco>();
        enderecos.add(new Endereco("Rua A", "78", "Perinatal", "Desconhecida", "PA","2459230"));
        enderecos.add(new Endereco("Rua B", "15", "Outro", "Desconhecida", "PA","2459130"));
        assertThatThrownBy(() -> new Cliente("   ", "email@email.com", "123", enderecos))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Nome é obrigatório.");
    }

    @Test
    @DisplayName("Deve lançar exceção quando nome tiver menos de 2 caracteres")
    void deveLancarExcecaoQuandoNomeTiverMenosDe2OuMaiorQue100Caracteres() {
        List<Endereco> enderecos = new ArrayList<Endereco>();
        enderecos.add(new Endereco("Rua 357", "78", "Perinatal", "Desconhecida", "PA","2459230"));
        enderecos.add(new Endereco("Rua 735", "15", "Outro", "Desconhecida", "PA","2459130"));

        assertThatThrownBy(() -> new Cliente("A", "email@email.com", "123", enderecos))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Nome deve ter entre 2 a 100 caracteres.");
    }

    // --------------------
    // 🔹 Testes de Email
    // --------------------

    @Test
    @DisplayName("Deve lançar exceção quando email for nulo")
    void deveLancarExcecaoQuandoEmailForNulo() {
        List<Endereco> enderecos = new ArrayList<Endereco>();
        enderecos.add(new Endereco("Rua 357", "78", "Perinatal", "Desconhecida", "PA","2459230"));
        enderecos.add(new Endereco("Rua 735", "15", "Outro", "Desconhecida", "PA","2459130"));

        assertThatThrownBy(() -> new Cliente("Carlos", null, "123", enderecos))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email é obrigatório.");
    }

    @Test
    @DisplayName("Deve lançar exceção quando email for vazio")
    void deveLancarExcecaoQuandoEmailForVazio() {
        List<Endereco> enderecos = new ArrayList<Endereco>();
        enderecos.add(new Endereco("Rua 357", "78", "Perinatal", "Desconhecida", "PA","2459230"));
        enderecos.add(new Endereco("Rua 735", "15", "Outro", "Desconhecida", "PA","2459130"));

        assertThatThrownBy(() -> new Cliente("Carlos", "   ", "123", enderecos))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email é obrigatório.");
    }

    @Test
    @DisplayName("Deve lançar exceção quando email não tiver @")
    void deveLancarExcecaoQuandoEmailNaoTiverArroba() {
        List<Endereco> enderecos = new ArrayList<Endereco>();
        enderecos.add(new Endereco("Rua 357", "78", "Perinatal", "Desconhecida", "PA","2459230"));
        enderecos.add(new Endereco("Rua 735", "15", "Outro", "Desconhecida", "PA","2459130"));

        assertThatThrownBy(() -> new Cliente("Carlos", "emailemail.com", "123", enderecos))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email inválido.");
    }

    @Test
    @DisplayName("Deve lançar exceção quando email não tiver ponto")
    void deveLancarExcecaoQuandoEmailNaoTiverPonto() {
        List<Endereco> enderecos = new ArrayList<Endereco>();
        enderecos.add(new Endereco("Rua 357", "78", "Perinatal", "Desconhecida", "PA","2459230"));
        enderecos.add(new Endereco("Rua 735", "15", "Outro", "Desconhecida", "PA","2459130"));

        assertThatThrownBy(() -> new Cliente("Carlos", "email@emailcom", "123", enderecos))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email inválido.");
    }

    // --------------------
    // 🔹 Teste de inativação
    // --------------------

    @Test
    @DisplayName("Deve inativar cliente quando método inativar for chamado")
    void deveInativarCliente() {
        // Arrange
        List<Endereco> enderecos = new ArrayList<Endereco>();
        enderecos.add(new Endereco("Rua 357", "78", "Perinatal", "Desconhecida", "PA","2459230"));
        enderecos.add(new Endereco("Rua 735", "15", "Outro", "Desconhecida", "PA","2459130"));

        Cliente cliente = new Cliente("Carlos", "email@email.com", "123", enderecos);

        // Act
        cliente.inativar();

        // Assert
        assertThat(cliente.isAtivo()).isFalse();
    }

    // --------------------
    // 🔹 Teste de alteração de nome (setter com validação)
    // --------------------

    @Test
    @DisplayName("Deve lançar exceção ao tentar alterar nome para vazio")
    void deveLancarExcecaoAoAlterarNomeParaVazio() {
        List<Endereco> enderecos = new ArrayList<Endereco>();
        enderecos.add(new Endereco("Rua 357", "78", "Perinatal", "Desconhecida", "PA","2459230"));
        enderecos.add(new Endereco("Rua 735", "15", "Outro", "Desconhecida", "PA","2459130"));

        Cliente cliente = new Cliente("Carlos", "email@email.com", "123", enderecos);

        assertThatThrownBy(() -> cliente.setNome(" "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Nome é obrigatório.");
    }

    @Test
    @DisplayName("Deve alterar nome com sucesso quando valor for válido")
    void deveAlterarNomeComSucesso() {
        List<Endereco> enderecos = new ArrayList<Endereco>();
        enderecos.add(new Endereco("Rua 357", "78", "Perinatal", "Desconhecida", "PA","2459230"));
        enderecos.add(new Endereco("Rua 735", "15", "Outro", "Desconhecida", "PA","2459130"));

        Cliente cliente = new Cliente("Carlos", "email@email.com", "123", enderecos);
        cliente.setNome("Henrique");

        assertThat(cliente.getNome()).isEqualTo("Henrique");
    }
}
