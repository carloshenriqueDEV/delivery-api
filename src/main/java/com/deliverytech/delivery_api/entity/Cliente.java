package com.deliverytech.delivery_api.entity;

import jakarta.persistence.*; 
import lombok.Data;


import java.time.LocalDateTime;
import java.util.List;

@Entity 
@Data 
public class Cliente { 
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name = "cliente_id")
    private Long id; 
    private String nome; 
    private String email; 
    private String telefone; 
    private String endereco; 
    private boolean ativo; 
    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro;
    
 
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Pedido> pedidos; 

    public void inativar() { 
        this.ativo = false; 
    }

    //getters de todas as propriedades
    public Long getId() {
        return id;
    }
    public String getNome() {
        return nome;
    }
    public String getEmail() {
        return email;
    }
    public String getTelefone() {
        return telefone;
    }
    public String getEndereco() {
        return endereco;
    }
    public boolean isAtivo() {
        return ativo;
    }

      @PrePersist
    public void prePersist() {
        this.dataCadastro = LocalDateTime.now();
    }

}