package com.deliverytech.delivery_api.entity;

import jakarta.persistence.*; 
import lombok.Data; 
 
import java.math.BigDecimal;
import java.util.List; 
 
@Entity 
@Data 
public class Produto { 
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id; 
    private String nome; 
    private String descricao; 
    private BigDecimal preco; 
    private String categoria; 
    private boolean disponivel; 
 
    @ManyToOne 
    @JoinColumn(name = "restaurante_id") 
    private Restaurante restaurante; 

        @OneToMany(mappedBy = "produto")
    private List<ItemPedido> itensPedido;


    public boolean getDisponivel() {
        return disponivel;
    }
}