package com.deliverytech.delivery_api.entity;

import jakarta.persistence.*; 
import lombok.Data; 
import java.math.BigDecimal; 
import java.util.List;

@Entity 
@Data 
public class Restaurante { 
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name = "restaurante_id")
    private Long id; 
    private String nome; 
    private String categoria; 
    private String endereco; 
    private String telefone; 
    private BigDecimal taxaEntrega; 
    private boolean ativo; 
    private Float avaliacao;
 
    @OneToMany(mappedBy = "restaurante") 
    private List<Produto> produtos; 
 
    @OneToMany(mappedBy = "restaurante") 
    private List<Pedido> pedidos; 

    public void ativar() { 
        this.ativo = true; 
    }
    public void inativar() { 
        this.ativo = false; 
    }

    public boolean getAtivo() {
        return ativo;
    }

    public BigDecimal getTaxaEntrega() {
        return taxaEntrega;
    }

    public Long getId() {
        return id;
    }

    
} 