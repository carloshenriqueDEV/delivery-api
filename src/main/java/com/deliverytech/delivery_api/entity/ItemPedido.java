package com.deliverytech.delivery_api.entity;

import jakarta.persistence.*; 
import lombok.Data; 
 
import java.math.BigDecimal; 
 
@Entity 
@Data 
public class ItemPedido { 
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id; 
 
    private int quantidade; 
    private BigDecimal precoUnitario; 
    private BigDecimal subtotal; 
 
    @ManyToOne 
    @JoinColumn(name = "pedido_id") 
    private Pedido pedido; 
 
    @ManyToOne 
    @JoinColumn(name = "produto_id") 
    private Produto produto; 

    public BigDecimal calcularSubtotal() {
        subtotal = precoUnitario.multiply(BigDecimal.valueOf(quantidade));
        return subtotal;
    }
}