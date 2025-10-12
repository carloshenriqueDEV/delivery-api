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

    public Cliente() {
        // pode ficar vazio, o JPA só precisa dele para criar a instância
    }

    // 🔹 Construtor de uso da aplicação (Service, Controller etc.)
    public Cliente(String nome, String email, String telefone, String endereco) {

        this.validarDadosCliente();

        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.endereco = endereco;
        this.dataCadastro = LocalDateTime.now();
        this.ativo = true;
    }
 
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Pedido> pedidos; 

    public void inativar() { 
        this.ativo = false; 
    }

     /** 
     * Validações de negócio 
     */ 
    private void validarDadosCliente() { 
        if (this.nome == null || this.nome.trim().isEmpty()) { 
            throw new IllegalArgumentException("Nome é obrigatório"); 
        } 
 
        if (this.email == null || this.email.trim().isEmpty()) { 
            throw new IllegalArgumentException("Email é obrigatório"); 
        } 
 
        if (this.nome.length() < 2) { 
            throw new IllegalArgumentException("Nome deve ter pelo menos 2 caracteres"); 
        } 
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

}