package com.deliverytech.delivery_api.entity;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.*;

class ItemPedidoTest {

    @Test
    void deveCalcularSubtotalCorretamente() {
        Produto produto = new Produto("Pizza", "Mussarela", new BigDecimal("50.00"), "Pizza", true, null);
        ItemPedido item = new ItemPedido(2, produto.getPreco(), produto);

        assertThat(item.calcularSubtotal()).isEqualTo(new BigDecimal("100.00"));
    }

    @Test
    void deveCriarItemPedidoComValoresValidos() {
        Produto produto = new Produto("Coxinha", "Frango", new BigDecimal("8.00"), "Salgado", true, null);
        ItemPedido item = new ItemPedido(3, new BigDecimal("8.00"), produto);

        assertThat(item.getSubtotal()).isEqualTo(new BigDecimal("24.00"));
    }
}
