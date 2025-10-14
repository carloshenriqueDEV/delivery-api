package com.deliverytech.delivery_api.entity;

import jakarta.persistence.*; 
import lombok.Data;
import lombok.val;

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

    public Produto() {
        // pode ficar vazio, o JPA só precisa dele para criar a instância
    }

    // 🔹 Construtor de uso da aplicação (Service, Controller etc.
    public Produto(String nome, String descricao, BigDecimal preco, String categoria, boolean disponivel, Restaurante restaurante) {
        this.validarDadosProduto(nome, preco);

        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.categoria = categoria;
        this.disponivel = disponivel;
        this.restaurante = restaurante;
    }
 
    @ManyToOne 
    @JoinColumn(name = "restaurante_id") 
    private Restaurante restaurante; 

        @OneToMany(mappedBy = "produto")
    private List<ItemPedido> itensPedido;


    public boolean getDisponivel() {
        return disponivel;
    }

    public void setNome(String nome) {
        validarNome(nome);
        this.nome = nome;
    }

    public void setPreco(BigDecimal preco) {
        validarPreco(preco);
        this.preco = preco;
    }
 
     /** 
     * Validações de negócio 
     */

    private void validarDadosProduto(String nome, BigDecimal preco) { 
       validarNome(nome);
       validarPreco(preco);
    }

      private void validarNome(String nome) { 
        if (nome == null || nome.trim().isEmpty()) { 
            throw new IllegalArgumentException("Nome é obrigatório"); 
        } 

        if (nome.length() < 2) { 
            throw new IllegalArgumentException("Nome deve ter pelo menos 2 caracteres"); 
        } 
    }

    private void validarPreco(BigDecimal preco) { 
        if (preco == null || preco.compareTo(BigDecimal.ZERO) <= 0) { 
            throw new IllegalArgumentException("Preço deve ser maior que zero"); 
        }
    }
}