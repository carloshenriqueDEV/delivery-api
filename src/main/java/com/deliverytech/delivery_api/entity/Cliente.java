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
    private boolean ativo; 
    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro;

    public Cliente() {
        // pode ficar vazio, o JPA só precisa dele para criar a instância
    }

    // 🔹 Construtor de uso da aplicação (Service, Controller etc.)
    public Cliente(String nome, String email, String telefone, List<Endereco> enderecos) {

        this.validarDadosCliente(nome, email, telefone, enderecos);

        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.enderecos = enderecos;
        this.dataCadastro = LocalDateTime.now();
        this.ativo = true;
    }
 
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Pedido> pedidos; 

    public void inativar() { 
        this.ativo = false; 
    }

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "cliente_id")
    private List<Endereco> enderecos;

     /** 
     * Validações de negócio 
     */ 
    private void validarDadosCliente(String nome, String email, String telefone, List<Endereco> enderecos) { 
        validarNome(nome); 
        validarEmail(email);
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
    public List<Endereco> getEnderecos() {
        return enderecos;
    }
    public boolean isAtivo() {
        return ativo;
    }

    public void setNome(String nome) {
        validarNome(nome);
        this.nome = nome;
    }

    public void setEnderecos(List<Endereco> enderecos){
        this.enderecos = enderecos;
    }

    private void validarNome(String nome) { 
        if (nome == null || nome.trim().isEmpty()) { 
            throw new IllegalArgumentException("Nome é obrigatório."); 
        } 

        if (nome.length() < 2 || nome.length() > 100) { 
            throw new IllegalArgumentException("Nome deve ter entre 2 a 100 caracteres."); 
        } 
    }

    private void validarEmail(String email) { 
        if (email == null || email.trim().isEmpty()) { 
            throw new IllegalArgumentException("Email é obrigatório."); 
        } 

        if (!email.contains("@") || !email.contains(".")) { 
            throw new IllegalArgumentException("Email inválido."); 
        } 
    }

}