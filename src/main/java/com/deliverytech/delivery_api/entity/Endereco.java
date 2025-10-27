package com.deliverytech.delivery_api.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor // obrigatório para o JPA
@EqualsAndHashCode(of = {"logradouro", "numero", "cidade","bairro", "estado", "cep"})
/**
 * Classe Endereco representa um endereço físico.
 * Pode ser embutido em outras entidades, como Pedido, para armazenar o endereço de entrega.
 */
public class Endereco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;
    private String logradouro;
    private String numero;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;


    public Endereco(String logradouro, String numero, String bairro, String cidade, String estado, String cep){
        this.validarDados(logradouro, numero, bairro, cidade, estado, cep);
        this.logradouro = logradouro;
        this.numero = numero;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
    }

    private void validarDados(String logradouro, String numero, String bairro, String cidade, String estado, String cep){

        if (logradouro == null || logradouro.isBlank())
            throw new IllegalArgumentException("Logradouro é obrigatório.");
        if (cep == null || cep.isBlank())
            throw new IllegalArgumentException("CEP é obrigatório.");
        if(numero == null || numero.isBlank())
            throw new IllegalArgumentException("Número é obrigatório.");
    }
}