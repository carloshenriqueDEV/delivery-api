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
    private String telefone; 
    private BigDecimal taxaEntrega; 
    private boolean ativo; 
    private Float avaliacao; 
     private String horarioFuncionamento; 

    public Restaurante() {
        // pode ficar vazio, o JPA só precisa dele para criar a instância
    }

    // 🔹 Construtor de uso da aplicação (Service, Controller etc.)
    public Restaurante(String nome, String categoria, Endereco endereco, String telefone, BigDecimal taxaEntrega, String horarioFuncionaento) {
        this.validarDadosRestaurante(nome, taxaEntrega);

        this.nome = nome;
        this.categoria = categoria;
        this.endereco = endereco;
        this.telefone = telefone;
        this.taxaEntrega = taxaEntrega;
        this.ativo = true;
        this.avaliacao = 0.0f; 
        this.horarioFuncionamento = horarioFuncionaento;
    }
 
    @OneToMany(mappedBy = "restaurante") 
    private List<Produto> produtos; 
 
    @OneToMany(mappedBy = "restaurante") 
    private List<Pedido> pedidos; 

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "endereco_id", referencedColumnName = "id")
    private Endereco endereco;

    public BigDecimal getTaxaEntrega() {
        return taxaEntrega;
    }

    public Long getId() {
        return id;
    }

     /** 
     * Validações de negócio 
     */
    private void validarDadosRestaurante(String nome, BigDecimal taxaEntrega) { 
         validarNome(nome);
         validarTaxaEntrega(taxaEntrega);
    }

    private void validarNome(String nome) { 
        if (nome == null || nome.trim().isEmpty()) { 
            throw new IllegalArgumentException("Nome é obrigatório"); 
        } 

        if (nome.length() < 2) {
            throw new IllegalArgumentException("Nome deve ter pelo menos 2 caracteres"); 
        } 
    }

    private void validarTaxaEntrega(BigDecimal taxaEntrega) { 
        if (taxaEntrega == null || taxaEntrega.compareTo(BigDecimal.ZERO) < 0) { 
            throw new IllegalArgumentException("Taxa de entrega não pode ser negativa"); 
        }
    }
    
    
     public BigDecimal calcularTaxaDeEntrega(BigDecimal distanciaKm, BigDecimal valorPorKm) {
        if (distanciaKm == null || distanciaKm.compareTo(BigDecimal.ZERO) <= 0)
            return BigDecimal.ZERO;

        BigDecimal acrescimoPorKm = valorPorKm.multiply(distanciaKm);
        return this.taxaEntrega.add(acrescimoPorKm);
    }
} 